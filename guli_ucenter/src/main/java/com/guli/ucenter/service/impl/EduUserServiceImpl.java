package com.guli.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.mapper.MemberMapper;
import com.guli.ucenter.service.EduUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author guli
 * @since 2020-10-30
 */
@Service
public class EduUserServiceImpl extends ServiceImpl<MemberMapper, Member> implements EduUserService {

    @Override
    public Integer getCountRegisterByDay(String day) {
        Integer count =  baseMapper.getRegisterCount(day);
        return count;
    }

    @Override
    public Member getMemberByOpenid(String openid) {

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        Member member = baseMapper.selectOne(wrapper);

        Member newMember = new Member();
        newMember.setOpenid(member.getOpenid());
        newMember.setNickname(member.getNickname());
        newMember.setAvatar(member.getAvatar());

        return newMember;
    }

    @Override
    public String saveMember(Member insertMember) {

        int insert = baseMapper.insert(insertMember);
        if(insert<=0){
            return null;
        }

        QueryWrapper<Member> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", insertMember.getOpenid());
        Member member = baseMapper.selectOne(wrapper);
        if(member!=null){
            return member.getId();
        }
        return null;
    }
}
