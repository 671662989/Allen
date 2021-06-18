package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryApi {
}
