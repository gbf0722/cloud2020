package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.base.result.FormUtils;
import com.atguigu.guli.common.base.result.JwtUtils;
import com.atguigu.guli.common.base.result.MD5;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginInfoVo;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author jack
 * @since 2020-01-11
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Integer countRegisterByDay(String day) {

        return baseMapper.countRegisterByDay(day);
    }

    //会员注册
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterVo registerVo) {


        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();


        //校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(nickname)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码
        String mobileCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(mobileCode)) {
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }

        //验证是否被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);

        }

        //注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://online-teach-file-helen.oss-cn-beijing.aliyuncs.com/avatar/default.jpg");
        baseMapper.insert(member);

    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);

        }

        //登录验证
        QueryWrapper<Member> querywrapper = new QueryWrapper<>();
        querywrapper.eq("mobile", mobile);
        Member member = baseMapper.selectOne(querywrapper);
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBLE_ERROR);
        }

        //校验密码
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(ResultCodeEnum.LOGIN_MOBLE_ERROR);
        }

        //校验用户是否被禁用
        if (member.getDisabled()) {
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        String jwtToken = JwtUtils.generateJWT(
                member.getId(),
                member.getNickname(),
                member.getAvatar()
        );

        return jwtToken;
    }

    /**
     * 根据token获取会员登录信息
     *
     * @param jwtToken
     * @return 用户登录信息
     */
    @Override
    public LoginInfoVo getLoginInfoByJwtToken(String jwtToken) {
        Claims claims = JwtUtils.checkJWT(jwtToken);
        String id = (String) claims.get("id");
        String nickname = (String) claims.get("nickname");
        String avatar = (String) claims.get("avatar");

        LoginInfoVo loginInfoVo = new LoginInfoVo();
        loginInfoVo.setId(id);
        loginInfoVo.setNickname(nickname);
        loginInfoVo.setAvatar(avatar);

        return loginInfoVo;
    }

    @Override
    public Member getByOpenid(String openid) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        return baseMapper.selectOne(queryWrapper);
    }


}
