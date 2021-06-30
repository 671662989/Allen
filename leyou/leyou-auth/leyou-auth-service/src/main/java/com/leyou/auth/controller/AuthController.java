package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties properties;

    @PostMapping("accredit")
    public ResponseEntity<Void> authtication(
            @RequestParam("username") String username, @RequestParam("password") String password,
            HttpServletRequest request, HttpServletResponse response) {

        String token = this.authService.authtication(username,password);
        if(StringUtils.isBlank(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        CookieUtils.setCookie(request,response,properties.getCookieName(),token,properties.getCookieMaxAge()*60,null,true);
        return ResponseEntity.ok().build();
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("LY_TOKEN") String token,HttpServletRequest request,HttpServletResponse response) {

        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, properties.getPublicKey());

            String generateToken = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getCookieMaxAge() );
            CookieUtils.setCookie(request, response, properties.getCookieName(), generateToken, properties.getCookieMaxAge()*60, null, true);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
