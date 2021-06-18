package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.api.BrandApi;
import com.leyou.item.api.SpecificationApi;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.reponsitory.GoodsReponsitory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    public static final ObjectMapper MAPPER = new ObjectMapper();

    public PageResult<Goods> searchPage(SearchRequest request) {
        String key = request.getKey();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
      String cateAgg="categories";
      String brandAgg="brands";
       // MatchQueryBuilder basic=QueryBuilders.matchQuery("all", key).operator(Operator.AND);
        BoolQueryBuilder booleanQuery=builderBooleanQuery(request);
        builder.withQuery(booleanQuery);
        if (StringUtils.isNotBlank(request.getSortBy())) {
            builder.withSort(SortBuilders.fieldSort(request.getSortBy()).order(request.getDescending() ? SortOrder.DESC : SortOrder.ASC));
        }
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));

         builder.addAggregation(AggregationBuilders.terms(cateAgg).field("cid3"));
         builder.addAggregation(AggregationBuilders.terms(brandAgg).field("brandId"));

        Integer page = request.getPage();
        Integer size = request.getSize();
        builder.withPageable(PageRequest.of(page - 1, size));

        AggregatedPage<Goods> search =(AggregatedPage) this.reponsitory.search(builder.build());
        List<Map<String,Object>> spec=new ArrayList<>();
        List<Map<String, Object>> category = getCategoryAndAggregation(search.getAggregation(cateAgg));
        List<Brand> brands = getBrandsAndAggregation(search.getAggregation(brandAgg));
        if(category.size()==1){
          spec =  this.getSpecAndAggregation((Long)category.get(0).get("id"),booleanQuery);
        }


        return new SearchResult(search.getTotalElements(), search.getTotalPages(), search.getContent(),category,brands,spec);
    }

    private BoolQueryBuilder builderBooleanQuery(SearchRequest request) {
        BoolQueryBuilder builder=QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        Map<String, Object> filters = request.getFilter();
        if(CollectionUtils.isEmpty(filters)){
            return builder;
        }
        filters.entrySet().forEach(set ->{
            String key = set.getKey();
            if(StringUtils.equals(key,"品牌")){
                key="brandId";
            }else if(StringUtils.equals(key,"分类")){

                key="cid3";
            }else{
                key="specs."+key+".keyword";
            }
            builder.filter(QueryBuilders.termQuery(key,set.getValue()));
        });

        return builder;
    }

    private List<Map<String,Object>> getSpecAndAggregation(Long id, BoolQueryBuilder basic) {
      List<Map<String,Object>> specParams=new ArrayList<>();
       NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();
       builder.withQuery(basic);

        List<SpecParam> specList = this.specClient.queryParams(null, id, null, true);
        specList.forEach(specParam -> {
            builder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
        });

        AggregatedPage<Goods> specPage=(AggregatedPage<Goods>)this.reponsitory.search(builder.build());

        Map<String, Aggregation> aggregationMap = specPage.getAggregations().asMap();

        for(Map.Entry<String,Aggregation> entry: aggregationMap.entrySet() ){
            Map<String,Object> map=new HashMap();

               map.put("k",entry.getKey());
             List<Object> value=new ArrayList<>();
            StringTerms terms=(StringTerms) entry.getValue();
            List<StringTerms.Bucket> buckets = terms.getBuckets();
            buckets.forEach(bucket -> {
                value.add(bucket.getKeyAsString());
            });
            map.put("options",value);

               specParams.add(map);
        }


        return  specParams;
    }

    private List<Brand> getBrandsAndAggregation(Aggregation aggregation) {
        LongTerms terms=(LongTerms)aggregation;
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        List<Brand> brands=new ArrayList<>();
        buckets.forEach(bucket -> {
            Long id=bucket.getKeyAsNumber().longValue();

            Brand brand = this.brandClient.queryBrandName(id);
            brands.add(brand);

        });
       return  brands;
    }

    private List<Map<String,Object>> getCategoryAndAggregation(Aggregation aggregation) {
        LongTerms terms=(LongTerms) aggregation;
        List<LongTerms.Bucket> buckets = terms.getBuckets();

        List<Map<String,Object>> result=new ArrayList<>();

       List<Long> ids=new ArrayList<>();

        buckets.forEach(bucket -> {
            long l = bucket.getKeyAsNumber().longValue();
            ids.add(l);
        });

        List<String> list = this.categoryClient.queryCategoriesName(ids);
        for(int i=0;i<list.size();i++){
            Map<String ,Object> map=new HashMap<>();
            map.put("id",ids.get(i));
            map.put("name",list.get(i));
            result.add(map);
        }
        return result;
    }

    public Goods buildGoods(Spu spu) throws IOException {

        Goods goods = new Goods();
        Brand brand = this.brandClient.queryBrandName(spu.getBrandId());
        List<String> names = this.categoryClient.queryCategoriesName(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<Sku> skuList = this.goodsClient.querySkuBySpuId(spu.getId());
        List<Map<String, Object>> skus = new ArrayList<>();
        List<Long> prices = new ArrayList<>();
        skuList.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("images", StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");
            map.put("price", sku.getPrice());
            skus.add(map);
        });
        List<SpecParam> specParams = this.specClient.queryParams(null, spu.getCid3(), null, true);
        SpuDetail spuDetail = this.goodsClient.querySpuDetail(spu.getId());
        Map<Long, Object> generic = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        Map<Long, List<Object>> specParam = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {
        });
        Map<String, Object> specs = new HashMap<String, Object>();
        specParams.forEach(spec -> {
            if (spec.getGeneric()) {
                String value = generic.get(spec.getId()) == null ? "" : generic.get(spec.getId()).toString();
                if (spec.getNumeric()) {
                    value = chooseRange(value, spec);
                }
                specs.put(spec.getName(), value);

            } else {

                specs.put(spec.getName(), specParam.get(spec.getId()) == null ? new ArrayList<>() : specParam.get(spec.getId()));
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
        goods.setAll(spu.getTitle() + brand.getName() + StringUtils.join(names, " "));

        goods.setSkus(MAPPER.writeValueAsString(skus));
        goods.setSpecs(specs);

        return goods;

    }

    private String chooseRange(String value, SpecParam spec) {
        double val = NumberUtils.toDouble(value);
        String result = "其他";
        for (String segement : spec.getSegments().split(",")) {
            String[] segements = segement.split("-");
            double begin = NumberUtils.toDouble(segements[0]);
            double end = Double.MAX_VALUE;
            if (segements.length == 2) {
                end = NumberUtils.toDouble(segements[1]);
            }
            if (val >= begin && val <= end) {
                if (segements.length == 2) {
                    result = segement + spec.getUnit();
                    if (begin == 0d) {
                        result = end + spec.getUnit() + "以下";
                    }
                } else {

                    result = begin + spec.getUnit() + "以上";

                }
                break;
            }
        }

        return result;
    }


    public void createIndex(Long id) throws Exception{

        Spu spu = this.goodsClient.querySpuById(id);

        Goods goods = this.buildGoods(spu);

        this.reponsitory.save(goods);

    }

    public void deleteIndex(Long id) {

        this.reponsitory.deleteById(id);
    }
}
