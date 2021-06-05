package com.leyou.search.client;

import com.leyou.LeyouSearchApplication;
import com.leyou.search.pojo.Goods;
import com.leyou.search.reponsitory.GoodsReponsitory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

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
public class ElasticSearchTest {
    @Autowired
    private GoodsReponsitory goodsReponsitory;
    @Autowired
    private ElasticsearchTemplate template;
    @Test
    public  void importIndex(){
      this.template.createIndex(Goods.class);
      this.template.putMapping(Goods.class);
    }
}
