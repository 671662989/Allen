package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties prop;

    public String authtication(String username, String password) {
        try {
            User user = userClient.query(username, password);
            if (user == null) {
                return  null;
            }
            UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());

            return JwtUtils.generateToken(userInfo,prop.getPrivateKey(),prop.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
