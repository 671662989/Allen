package com.leyou.search.service;

import com.leyou.LeyouSearchApplication;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Spu;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.reponsitory.GoodsReponsitory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchApplication.class)
public class SearchServiceTest {
    @Autowired
    private ElasticsearchTemplate template;
    @Autowired
    private SearchService service;
    @Autowired
    private GoodsReponsitory goodsReponsitory;
    @Autowired
    private GoodsClient client;

    @Test
    public void createIndex(){
        Integer page=1;
        Integer rows=100;
        this.template.createIndex(Goods.class);
        this.template.putMapping(Goods.class);
        do {
            PageResult<SpuBo> pageResult = this.client.querySpu(null, true, page, rows);
            List<Goods> goods = pageResult.getItems().stream().map(spuBo -> {
                try {
                  return   this.service.buildGoods((Spu) spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            this.goodsReponsitory.saveAll(goods);
            rows=pageResult.getItems().size();
            page++;
        }while(rows==100);

    }
}