package com.leyou.auth.utils;

import com.leyou.auth.entity.UserInfo;
import io.jsonwebtoken.*;
import org.joda.time.DateTime;

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
public class JwtUtils {
    public static String generateToken(UserInfo userInfo, PrivateKey privateKey,int expireMinutes ){
        return Jwts.builder().claim(JwtConstants.JWT_KEY_ID,userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME,userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256,privateKey)
                .compact();
    }

    public static  String generateToken(UserInfo userInfo,byte[] privateKey,int expireMinutes) throws Exception {
        return Jwts.builder().claim(JwtConstants.JWT_KEY_ID,userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME,userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256,RsaUtils.getPrivateKey(privateKey))
                .compact();
    }

    private static Jws<Claims> parserToken(String token, PublicKey publicKey){
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    private static Jws<Claims> parserToken(String token,byte[] publicKey) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtils.getPublicKey(publicKey)).parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static UserInfo getInfoFromToken(String token, PublicKey publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstants.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstants.JWT_KEY_USER_NAME))
        );
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static UserInfo getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstants.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstants.JWT_KEY_USER_NAME))
        );
    }
}
