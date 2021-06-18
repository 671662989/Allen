package com.leyou.goods.service;

import com.leyou.goods.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;


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
public class GoodsHtmlService {
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private GoodsService goodsService;
    private final static Logger LOGGER= LoggerFactory.getLogger(GoodsHtmlService.class);

    public  void createHtml(Long spuId) {
        PrintWriter writer=null;
        try {
            Map<String, Object> data = this.goodsService.loadData(spuId);
            Context context=new Context();
            context.setVariables(data);
            File file=new File("E:\\gitDepository\\nginx-1.14.0\\html\\item\\"+spuId+".html");
            writer=new PrintWriter(file);
            templateEngine.process("item",context,writer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(null!=writer) {

                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
      }

      public  void asyncExecute(Long spuId){

        ThreadUtils.execute(()->createHtml(spuId));
      }

      public void deleteHtml(Long spuId){
          File file=new File("E:\\gitDepository\\nginx-1.14.0\\html\\item\\"+spuId+".html");
          file.deleteOnExit();

      }
}
