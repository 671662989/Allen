package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    public PageResult<SpuBo> querySpu(String key, Boolean saleable, Integer page, Integer rows) {
        Example example=new Example(Spu.class);
        Example.Criteria criteria=example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }
        PageHelper.startPage(page,rows);
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo=new PageInfo<>(spus);

        List<SpuBo> spuBos=new ArrayList<>();

        spus.forEach(spu -> {
            SpuBo spuBo=new SpuBo();
            BeanUtils.copyProperties(spu,spuBo);
           List<String> cname=this.categoryService.queryNamesById(Arrays.asList(spuBo.getCid1(),spuBo.getCid2(),spuBo.getCid3()));
            String bname=this.brandMapper.selectByPrimaryKey(spuBo.getBrandId()).getName();
            spuBo.setCname(StringUtils.join(cname,"/"));
            spuBo.setBname(bname);
           spuBos.add(spuBo);
        });

        return new PageResult<>(pageInfo.getTotal(),spuBos);



    }
    @Transactional
    public void saveSpuAndSkuInfo(SpuBo spuBo) {
        Spu spu=new Spu();
        //保存spu
        BeanUtils.copyProperties(spuBo,spu);
        spu.setSaleable(true);
        spu.setValid(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        this.spuMapper.insert(spu);
        Long spuId = spu.getId();
        //保存suDetail
        SpuDetail spuDetail=new SpuDetail();
        BeanUtils.copyProperties(spuBo.getSpuDetail(),spuDetail);
        spuDetail.setSpuId(spuId);
        this.spuDetailMapper.insert(spuDetail);
        //保存sku
        List<Sku> skuList=spuBo.getSkus();
        skuList.forEach(sku -> {
            Sku sku1=new Sku();
            BeanUtils.copyProperties(sku,sku1);
            sku1.setSpuId(spuId);
            sku1.setCreateTime(spu.getCreateTime());
            sku1.setLastUpdateTime(spu.getCreateTime());
            this.skuMapper.insert(sku1);
            Long sku1Id = sku1.getId();
            Stock stock=new Stock();
            stock.setSkuId(sku1Id);
            stock.setStock(sku.getStock());
            this.stockMapper.insert(stock);

        });
        this.sendMessage(spu.getId(),"insert");
    }

    public SpuBo querySpuById(Long spuId) {
       SpuBo spuBo=new SpuBo();
        //查询spu
        BeanUtils.copyProperties(this.spuMapper.selectByPrimaryKey(spuId),spuBo);
        //查询sku信息

        List<Sku> skuList = this.querySkuBySpuId(spuId);
        spuBo.setSkus(skuList);
        //查询stock信息
        spuBo.setSpuDetail(this.spuDetailMapper.selectByPrimaryKey(spuId));
        return  spuBo;
    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku=new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = this.skuMapper.select(sku);
        skuList.forEach(sku1->{
            sku1.setStock(this.stockMapper.selectByPrimaryKey(sku1.getId()).getStock());
        });
         return  skuList;
    }

    public SpuDetail querySpuDetail(Long spuId) {

        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }
   @Transactional
    public void updateGoods(SpuBo spuBo) {
        List<Sku> skuList=this.skuMapper.querySkusBySpuId(spuBo.getId());
        if(!CollectionUtils.isEmpty(skuList)){
             List<Long> ids=skuList.stream().map(sku ->sku.getId()).collect(Collectors.toList());
             Example example=new Example(Stock.class);
             Example.Criteria criteria=example.createCriteria();
             criteria.andIn("skuId",ids);
             this.stockMapper.deleteByExample(example);

             Sku record=new Sku();
             record.setSpuId(spuBo.getId());
             this.skuMapper.delete(record);
        }
        spuBo.setLastUpdateTime(new Date());
        saveSkuAndStock(spuBo);
        spuBo.setCreateTime(null);
        spuBo.setValid(null);
        spuBo.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        this.spuDetailMapper.updateByPrimaryKey(spuBo.getSpuDetail());

        this.sendMessage(spuBo.getId(),"update");

    }

    private void saveSkuAndStock(SpuBo spuBo) {
        List<Sku> skuList=spuBo.getSkus();
        if(!CollectionUtils.isEmpty(skuList)){
            skuList.forEach(sku -> {
                sku.setSpuId(spuBo.getId());
                sku.setCreateTime(spuBo.getCreateTime());
                sku.setLastUpdateTime(spuBo.getLastUpdateTime());
                this.skuMapper.insertSelective(sku);
                Stock record=new Stock();
                record.setStock(sku.getStock());
                record.setSkuId(sku.getId());
                this.stockMapper.insertSelective(record);
            });
        }
    }
  @Transactional
    public void deleteSpu(Long id) {
      deleteSkuAndStock(id);
    this.spuDetailMapper.deleteByPrimaryKey(id);
    this.spuMapper.deleteByPrimaryKey(id);
    }

    private void deleteSkuAndStock(Long spuId) {
        List<Sku> skus=this.skuMapper.querySkusBySpuId(spuId);
        if(!CollectionUtils.isEmpty(skus)){
            List<Long> ids = skus.stream().map(sku -> sku.getId()).collect(Collectors.toList());
            Example example=new Example(Stock.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andIn("skuId",ids);
            this.stockMapper.deleteByExample(example);
            Sku sku1=new Sku();
            sku1.setSpuId(spuId);
            this.skuMapper.delete(sku1);
        }
    }

    public Spu querySpuByID(Long id) {
        return   this.spuMapper.selectByPrimaryKey(id);

    }

    public void sendMessage(Long id,String type){

        try {
            this.amqpTemplate.convertAndSend("item."+type,id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }

    }

    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }
}
