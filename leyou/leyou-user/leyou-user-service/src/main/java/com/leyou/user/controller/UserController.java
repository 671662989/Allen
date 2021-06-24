package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

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
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data")String data
    ,@PathVariable("type")Integer type){

       Boolean boo= userService.checkUserData(data,type);
       if(boo == null){
           return  ResponseEntity.badRequest().build();
       }
       return ResponseEntity.ok(boo);


    }

    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(String phone){
       Boolean boo = this.userService.sendVerifyCode(phone);
       if(!boo || boo==null){
           return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
       return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
       Boolean boo =  this.userService.register(user,code);

       if(!boo || boo == null){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
       return  new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("query")
    public  ResponseEntity<User> query(@RequestParam("username")String username,@RequestParam("password")String password){
        User user=this.userService.queryUser(username,password);

        if(null == user){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
