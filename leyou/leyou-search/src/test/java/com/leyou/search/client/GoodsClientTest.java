package com.leyou.search.client;

import com.leyou.LeyouSearchApplication;
import net.minidev.json.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.util.Arrays;
import java.util.List;

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
public class GoodsClientTest {
   @Autowired
    private CategoryClient categoryClient;
   @Test
    public void testQueryCategories(){
       List<String> list = this.categoryClient.queryCategoriesName(Arrays.asList(1l, 2l, 3l));
       list.forEach(System.out::println);

   }
}