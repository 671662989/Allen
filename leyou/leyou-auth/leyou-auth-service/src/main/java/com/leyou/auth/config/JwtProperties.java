package com.leyou.auth.config;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@ConfigurationProperties(prefix = "leyou.jwt")
public class JwtProperties {

    private String pubKeyPath;

    private String priKeyPath;

    private String secret;

    private Integer expire;

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private String cookieName;
    private Integer cookieMaxAge;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        File pubKey = new File(pubKeyPath);
        File priKey = new File(priKeyPath);
        try {
            if( !pubKey.exists() || !priKey.exists()){

                RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
            }

            privateKey = RsaUtils.getPrivateKey(priKeyPath);
            publicKey = RsaUtils.getPublicKey(pubKeyPath);

        } catch (Exception e) {
            LOGGER.error("初始化公钥和秘钥失败！"+e);
            e.printStackTrace();
        }
    }

    public String getPubKeyPath() {
        return pubKeyPath;
    }

    public void setPubKeyPath(String pubKeyPath) {
        this.pubKeyPath = pubKeyPath;
    }

    public String getPriKeyPath() {
        return priKeyPath;
    }

    public void setPriKeyPath(String priKeyPath) {
        this.priKeyPath = priKeyPath;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public Integer getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }
}
