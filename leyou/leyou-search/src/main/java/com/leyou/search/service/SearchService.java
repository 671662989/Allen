package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.SpecificationApi;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.reponsitory.GoodsReponsitory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

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
public class SearchService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsReponsitory reponsitory;

    public  static final ObjectMapper MAPPER=new ObjectMapper();

    public PageResult<Goods> searchPage(SearchRequest request) {
        String key=request.getKey();
        if(StringUtils.isBlank(key)){
            return  null;
        }
        NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();

        builder.withQuery(QueryBuilders.matchQuery("all",key).operator(Operator.AND));

        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        Integer page=request.getPage();
        Integer size=request.getSize();
        builder.withPageable(PageRequest.of(page-1,size));
        Page<Goods> search = this.reponsitory.search(builder.build());

        return  new PageResult<>(search.getTotalElements(),search.getTotalPages(),search.getContent());
    }

    public Goods buildGoods(Spu spu) throws IOException {

        Goods goods=new Goods();
        Brand brand=this.brandClient.queryBrandName(spu.getBrandId());
        List<String> names = this.categoryClient.queryCategoriesName(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<Sku> skuList = this.goodsClient.querySkuBySpuId(spu.getId());
        List<Map<String,Object>> skus=new ArrayList<>();
        List<Long> prices=new ArrayList<>();
        skuList.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("images",StringUtils.isNotBlank(sku.getImages())?StringUtils.split(sku.getImages(),",")[0]:"");
            map.put("price",sku.getPrice());
           skus.add(map);
        });
        List<SpecParam> specParams = this.specClient.queryParams(null, spu.getCid3(), null, true);
        SpuDetail spuDetail = this.goodsClient.querySpuDetail(spu.getId());
        Map<String,Object> generic = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<String, Object>>() {});
        Map<String,List<String>> specParam=MAPPER.readValue(spuDetail.getSpecialSpec(),new TypeReference<Map<String,List<String>>>(){});
        Map<String,Object> specs=new HashMap<String, Object>();
        specParams.forEach(spec ->{
            if(spec.getGeneric()){
                String value = generic.get(spec.getId())==null?"":generic.get(spec.getId()).toString();
                if(spec.getNumeric()){
                   value= chooseRange(value,spec);
                }
                specs.put(spec.getName(),value);

            }else{

                specs.put(spec.getName(),specParam.get(spec.getId())==null?new ArrayList<>():specParam.get(spec.getId()));
            }
        });


        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setPrice(prices);
        goods.setAll(spu.getTitle()+brand.getName()+ StringUtils.join(names," "));

        goods.setSkus(MAPPER.writeValueAsString(skus));
        goods.setSpecs(specs);

        return goods;

    }

    private String chooseRange(String value, SpecParam spec) {
        double val= NumberUtils.toDouble(value);
        String result="其他";
         for(String segement:spec.getSegments().split(",")){
             String[] segements=segement.split("-");
              double begin=NumberUtils.toDouble(segements[0]);
              double end=Double.MAX_VALUE;
             if (segement.length()==2){
                 end=NumberUtils.toDouble(segements[1]);
             }
             if(val>=begin&&val<=end){
                 if(segements.length==2){
                     result=segement+spec.getUnit();
                     if(begin==0){
                         result=end+spec.getUnit()+"以下";
                     }
                 }else{

                         result=begin+spec.getUnit()+"以上";

                 }
                 break;
             }
         }

         return  result;
    }


}
