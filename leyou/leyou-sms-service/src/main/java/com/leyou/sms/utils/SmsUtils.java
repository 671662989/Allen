package com.leyou.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.leyou.sms.config.SmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtils {

    @Autowired
    private SmsProperties smsProperties;
    static  final String product="Dysmsapi";
    static  final String domain="dysmsapi.aliyuncs.com";
    static  final Logger logger= LoggerFactory.getLogger(SmsUtils.class);

    public SendSmsResponse sendSms(String phone,String code,String signName,String template) throws ClientException {

        System.setProperty("sun.net.client.defaultConnectTimeout","10000");
        System.setProperty("sun.net.client.defaultReadTimeout","10000");

        IClientProfile profile= DefaultProfile.getProfile("cn-hangzhou",smsProperties.getAccessKeyId(),smsProperties.getAccessKeySecret());
        DefaultProfile.addEndpoint("cn-hangzhou","cn-hangzhou",product,domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request=new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setPhoneNumbers(phone);
        request.setSignName(signName);
        request.setTemplateCode(template);
        request.setTemplateParam("{\"code\":\""+code+"\"}");

        request.setOutId("123456");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        logger.info("发送短信状态:{}",sendSmsResponse.getCode());
        logger.info("发送短信消息:{}",sendSmsResponse.getMessage());

        return sendSmsResponse;


    }
}
