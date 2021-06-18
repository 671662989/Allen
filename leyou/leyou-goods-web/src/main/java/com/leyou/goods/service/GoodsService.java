package com.leyou.goods.service;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecificationClient;
import com.leyou.item.api.SpecificationApi;
import com.leyou.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class GoodsService {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private BrandClient brandClient;

    public Map<String,Object> loadData(Long id) {

        HashMap<String, Object> data = new HashMap<>();
        Spu spu = this.goodsClient.querySpuById(id);

        SpuDetail spuDetail = this.goodsClient.querySpuDetail(id);

        List<SpecGroup> specGroups = this.specClient.querySpecByCid(spu.getCid3());

        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());

        List<String> categoriesName = this.categoryClient.queryCategoriesName(cids);

        List<Map<String,Object>> categories=new ArrayList<>();

        for (int i = 0; i <cids.size() ; i++) {
            Map<String,Object> category=new HashMap<>();
            category.put("id",cids.get(i));
            category.put("name",categoriesName.get(i));
            categories.add(category);
        }
         Map<Long,String> paraMap=new HashMap<>();
        List<SpecParam> specParams = this.specClient.queryParams(null, spu.getCid3(), null, null);
        specParams.forEach(specParam -> {
            paraMap.put(specParam.getId(),specParam.getName());
        });
        List<Sku> skuList = this.goodsClient.querySkuBySpuId(id);

        Brand brand = this.brandClient.queryBrandName(spu.getBrandId());
        data.put("spu",spu);
        data.put("skus",skuList);
        data.put("spuDetail",spuDetail);
        data.put("categories",categories);
        data.put("brand",brand);
        data.put("groups",specGroups);
        data.put("paramMap",paraMap);
        return data;
    }
}
