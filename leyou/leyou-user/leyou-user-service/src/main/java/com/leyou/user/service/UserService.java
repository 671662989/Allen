package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import com.sun.org.apache.bcel.internal.generic.SWITCH;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    static  final String KEY_PREFIX="user:code:phone";

    public Boolean checkUserData(String data, Integer type) {
        User record= new User();
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
                break;
            default:
                return null;

        }


        return  this.userMapper.select(record).size()==0;


    }

    public Boolean sendVerifyCode(String phone) {
        String generateCode = NumberUtils.generateCode(6);

        try {
            Map<String ,String > map = new HashMap<>();
            map.put("phone",phone);
            map.put("code",generateCode);
           // this.amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",map);

            this.redisTemplate.opsForValue().set(KEY_PREFIX+phone,generateCode,5, TimeUnit.MINUTES);
            return true;
        } catch (AmqpException e) {
            e.printStackTrace();
            return  false;
        }
    }

    public Boolean register(User user, String code) {

        String verifyCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if(!StringUtils.equals(code,verifyCode)){
            return false;
        }

        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        String password = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(password);
        user.setCreated(new Date());
        user.setId(null);
        Boolean bo = this.userMapper.insertSelective(user) == 1;
        if(bo){
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }

        return bo;

    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        if(null == user){
            return null;
        }
        if(!StringUtils.equals(CodecUtils.md5Hex(password,user.getSalt()), user.getPassword())){
            return  null;
        }
        return user;
    }
}
