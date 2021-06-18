package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
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
public interface BrandClient extends BrandApi {

}
