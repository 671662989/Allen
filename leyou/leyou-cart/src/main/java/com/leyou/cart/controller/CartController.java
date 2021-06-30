package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){

        this.cartService.addCart(cart);
       return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getCart(){
       List<Cart> cart = this.cartService.getCart();
       if(CollectionUtils.isEmpty(cart) ){
           return ResponseEntity.noContent().build();
       }
        return ResponseEntity.ok(cart);
    }

    @PutMapping
    public  ResponseEntity<Void> updateCart(@RequestBody Cart cart){
        this.cartService.updateCart(cart);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")Long skuId ){

        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
