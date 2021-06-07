package com.atguigu.guli.service.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.CommonRoaRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.guli.common.base.result.ExceptionUtils;
import com.atguigu.guli.common.base.result.FormUtils;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.sms.service.SmsService;
import com.atguigu.guli.service.sms.util.SmsProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.joda.time.format.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    //发送短信
    @Override
    public void send(String phone, Map<String, Object> param) {
        if (StringUtils.isEmpty(phone) || !FormUtils.isMobile(phone)) {
            log.error("短信发送失败----请输入正确的手机号");
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }

        Gson gson = new Gson();

        DefaultProfile profile = DefaultProfile.getProfile(
                smsProperties.getRegionid(),
                smsProperties.getKeyid(),
                smsProperties.getKeysecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);


        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionid());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", smsProperties.getSignname());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplatecode());
        request.putQueryParameter("TemplateParam", gson.toJson(param));

        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            HashMap<String, String> map = gson.fromJson(data, HashMap.class);
            String code = map.get("Code");
            String message = map.get("Message");
            //错误码参考
            if ("isv.BUSINESS_LIMIT_CONTROL".equals(code)) {
                log.error("短信发送过于频繁 " + " - code: " + code + ", message: " + message);
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
            }

            if (!"OK".equals(code)) {
                log.error("短信发送失败 " + " - code: " + code + ", message: " + message);
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
            }
        } catch (ServerException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        } catch (ClientException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
        }


    }
}
