package com.guli.ucenter.controller;


import com.guli.common.Result;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.service.EduUserService;
import com.guli.ucenter.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/ucenter/member")
@CrossOrigin
public class EduUserController {

    @Autowired
    EduUserService userService;


    //根据token获得用户信息
    @PostMapping("/token/{token}") // get请求有长度限制
    public Result getMemberByToken(@PathVariable String token){

        Claims claims = JwtUtils.checkJWT(token);
        String nickname = (String) claims.get("nickname");
        String avatar = (String) claims.get("avatar");
        String id = (String) claims.get("id");

        Member member = new Member();
        member.setAvatar(avatar);
        member.setNickname(nickname);
        member.setId(id);

        return Result.ok().data("member",member);
    }

    /**
     * 获得今日注册数
     */
    @GetMapping("count/{day}")
    public Result getRegisterNumByDay(@PathVariable String day){
        Integer count = userService.getCountRegisterByDay(day);
        return Result.ok().data("registerCount", count);
    }

}

