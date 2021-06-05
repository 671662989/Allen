package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByKey(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example=new Example(Brand.class);
        Example.Criteria criteria=example.createCriteria();
        if(!StringUtils.isEmpty(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        //存在page超过查询数量显示异常
        PageHelper.startPage(page,rows);
        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc?"desc":"asc"));
        }
        List<Brand> brands=this.brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo=new PageInfo<>(brands);

        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //先新增brand
        this.brandMapper.insertSelective(brand);
        //再新增中间表
        cids.forEach(cid ->{
            this.brandMapper.insertBrandAndCategory(cid,brand.getId());

        });
    }
    public void updateBrand(Brand brand, List<Long> cids) {
        this.brandMapper.updateByPrimaryKey(brand);

    }

    @Transactional
    public void deleteBrand(Brand brand) {
        this.brandMapper.deleteCategoryByBrandId(brand.getId());
         this.brandMapper.delete(brand);
    }

    public List<Brand> queryBrandByCid(Long cid) {
       return  this.brandMapper.selectBrandByCid(cid);
    }

    public Brand queryBrandName(Long bid) {
        Brand brand = this.brandMapper.selectByPrimaryKey(bid);
        return brand;
    }
}
