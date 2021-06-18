package com.leyou.search.listener;

import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "leyou.create.index.queue",durable = "true"),

            exchange = @Exchange(value = "leyou.item.exchange",
            ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC
            ),
            key = {"item.insert","item.update"}
            ))
    public  void ListenerCreate(Long id ) throws  Exception{
        if(id == null){
            return;
        }
        this.searchService.createIndex(id);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "leyou.delete.index.queue",durable = "true"),

            exchange = @Exchange(value = "leyou.item.exchange",
                    ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC
            ),
            key = {"item.delete"}
    ))
    public  void ListenerDelete(Long id ){
        if(id == null){
            return;
        }
        this.searchService.deleteIndex(id);
    }
}
