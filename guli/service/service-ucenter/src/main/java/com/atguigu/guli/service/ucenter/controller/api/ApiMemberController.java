package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.vo.LoginInfoVo;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.atguigu.guli.service.ucenter.service.MemberService;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author helen
 * @since 2020/1/12
 */
@CrossOrigin
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 会员注册
     * @param registerVo
     * @return
     */
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){

        memberService.register(registerVo);
        return R.ok().message("注册成功");
    }

    /**
     * 会员登录
     * @param loginVo
     * @return
     */
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo){

        String jwt = memberService.login(loginVo);
        return R.ok().message("登录成功").data("token", jwt);
    }


    /**
     * 根据token获取用户信息
     * @param request
     * @return
     */
    @GetMapping("auth/get-login-info")
    public R getLoginInfo(HttpServletRequest request){

        try {
            String token = request.getHeader("token");
            LoginInfoVo loginInfoVo = memberService.getLoginInfoByJwtToken(token);
            return R.ok().data("item", loginInfoVo);
        } catch (Exception e) {
            log.error("解析用户信息失败：" + e.getMessage());
            throw  new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

}
