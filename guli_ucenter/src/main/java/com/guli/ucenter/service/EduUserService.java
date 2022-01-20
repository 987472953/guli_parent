package com.guli.ucenter.service;

import com.guli.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author guli
 * @since 2020-10-30
 */
public interface EduUserService extends IService<Member> {

    Integer getCountRegisterByDay(String day);

    /**
     * 根据openId查询用户
     * @param openid
     * @return
     */
    Member getMemberByOpenid(String openid);

    /**
     * 保存用户，并返回id
     * @param insertMember
     * @return
     */
    String saveMember(Member insertMember);
}
