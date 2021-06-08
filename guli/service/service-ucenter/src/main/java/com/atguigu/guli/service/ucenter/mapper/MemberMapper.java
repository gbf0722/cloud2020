package com.atguigu.guli.service.ucenter.mapper;

import com.atguigu.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author jack
 * @since 2020-01-11
 */
public interface MemberMapper extends BaseMapper<Member> {
    Integer countRegisterByDay(String day);
}