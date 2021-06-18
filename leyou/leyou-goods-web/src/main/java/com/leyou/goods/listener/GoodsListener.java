package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@Component
public class GoodsListener {

    @Autowired
    public AmqpTemplate amqpTemplate;
    @Autowired
    GoodsHtmlService goodsHtmlService;
    @RabbitListener(bindings = @QueueBinding(value = @Queue(
            value = "leyou.create.web.queue",durable = "true"
    ),
            exchange = @Exchange(value = "leyou.item.exchange",
            ignoreDeclarationExceptions = "true",
            type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}))
    public void ListenerCreate(Long id ){
        if(id == null){
            return;
        }
        this.goodsHtmlService.createHtml(id);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(
            value = "leyou.delete.web.queue",durable = "true"
    ),
            exchange = @Exchange(value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = "item.delete"))
    public void ListenerDelete(Long id ){
        if(id == null){
            return;
        }
        this.goodsHtmlService.deleteHtml(id);
    }
}
