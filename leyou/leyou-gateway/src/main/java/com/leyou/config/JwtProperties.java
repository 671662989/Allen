package com.leyou.config;

import com.leyou.auth.utils.RsaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

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


    private PublicKey publicKey;

    private String cookieName;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init(){
        File pubKey = new File(pubKeyPath);

        try {

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


    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }


    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

}
