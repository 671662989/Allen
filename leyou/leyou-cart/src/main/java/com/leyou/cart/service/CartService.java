package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFFIX = "leyou:cart:uid:";

    public void addCart(Cart cart) {

        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFFIX + userInfo.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        Long skuId = cart.getSkuId();
        int num = cart.getNum();
        Boolean bo = hashOps.hasKey(skuId.toString());

        if (bo) {
            String json = hashOps.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        } else {
            cart.setUserId(userInfo.getId());
            Sku sku = this.goodsClient.querySkuById(cart.getSkuId());
            cart.setImage(sku.getImages() == null ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());

        }

        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));


    }

    public List<Cart> getCart() {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFFIX + userInfo.getId();

        if (!this.redisTemplate.hasKey(key)) {
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        if (CollectionUtils.isEmpty(carts)) {
            return null;
        }


        return carts.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
    }

    public void updateCart(Cart cart) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFFIX + userInfo.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        Cart newCart = JsonUtils.parse(hashOps.get(cart.getSkuId().toString()).toString(), Cart.class);
        newCart.setNum(cart.getNum());

        hashOps.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    public void deleteCart(Long skuId) {
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFFIX + userInfo.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());
    }
}
