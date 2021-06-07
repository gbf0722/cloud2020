package com.atguigu.guli.service.ucenter.controller.api;


import com.atguigu.guli.common.base.result.ExceptionUtils;
import com.atguigu.guli.common.base.result.JwtUtils;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.util.HttpClientUtils;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

@CrossOrigin
@Controller//注意这里没有配置 @RestController
@RequestMapping("/api/ucenter/wx")
@Slf4j
public class ApiWxController {
    @Autowired
    private UcenterProperties ucenterProperties;

    @Autowired
    private MemberService memberService;


    @GetMapping("login")
    public String genQrConnect(HttpSession session) {
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //处理回调url
        String redirectUrl = null;
        try {
            redirectUrl = URLEncoder.encode(ucenterProperties.getRedirecturl(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }

        //处理state,生成随机数，存入session
        String state = UUID.randomUUID().toString();
        System.out.println("生成的state" + state);
        session.setAttribute("wx-open-state", state);

        String qrcodeUrl = String.format(
                baseUrl,
                ucenterProperties.getAppid(),
                redirectUrl,
                state
        );

        return "redirect:" + qrcodeUrl;

    }
    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session){

        System.out.println("callback 被调用");
        System.out.println("state:" + state);
        System.out.println("code:" + code);

        if(StringUtils.isEmpty(code) || StringUtils.isEmpty(state) ){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        String sessionState = (String)session.getAttribute("wx-open-state");
        if(!state.equals(sessionState)){
            log.error("非法回调请求");
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //携带code加上app_id和app_secret换取access_token
        System.out.println("携带code加上app_id和app_secret换取access_token");

        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ucenterProperties.getAppid(),
                ucenterProperties.getAppsecret(),
                code);

        String result = "";
        try {
            result = HttpClientUtils.get(accessTokenUrl);
            System.out.println("result：" + result);
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        HashMap<String, Object> resultMap = gson.fromJson(result, HashMap.class);
        String accessToken = (String)resultMap.get("access_token");
        String openid = (String)resultMap.get("openid");

        System.out.println("accessToken：" + accessToken);
        System.out.println("openid：" + openid);

        Member member = memberService.getByOpenid(openid);
        if(member == null){
            //使用access_token获取用户个人信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";

            String userInfoUrl = String.format(
                    baseUserInfoUrl,
                    accessToken,
                    openid);

            String resultUserInfo = "";
            try {
                resultUserInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println("user info：" + resultUserInfo);
            } catch (Exception e) {
                log.error("获取用户信息失败");
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            HashMap<String, Object> resultInfoMap = gson.fromJson(resultUserInfo, HashMap.class);

            //使用微信账号在系统中注册用户
            member = new Member();
            member.setOpenid(openid);
            member.setAvatar((String) resultInfoMap.get("headimgurl"));
            member.setNickname((String) resultInfoMap.get("nickname"));
            member.setSex(((Double) resultInfoMap.get("sex")).intValue());
            memberService.save(member);

        }

        System.out.println(member.getNickname());
        String jwtToken = JwtUtils.generateJWT(member.getId(), member.getNickname(), member.getAvatar());

        System.out.println(jwtToken);

        //方案1:将jwt存入cookie//TODO
        //方案2：将jwt通过url传入前端
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }

}
