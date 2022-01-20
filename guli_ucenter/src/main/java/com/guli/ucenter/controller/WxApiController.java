package com.guli.ucenter.controller;


import com.google.gson.Gson;
import com.guli.common.exception.EduException;
import com.guli.ucenter.entity.Member;
import com.guli.ucenter.service.EduUserService;
import com.guli.ucenter.utils.ConstantPropertiesUtil;
import com.guli.ucenter.utils.HttpClientUtils;
import com.guli.ucenter.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx")
@CrossOrigin
public class WxApiController {

    @GetMapping("login")
    public String toLogin(){

        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        String redirectUrl = ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL;

        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数
        //String state = "imhelen";//为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        String state = "qtstudy"; //测试用
        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        baseUrl = String.format(
                baseUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                redirectUrl,
                state);
        return "redirect:" + baseUrl;

    }

    @Autowired
    EduUserService userService;

    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {


        //得到授权临时票据code
        System.out.println("code = " + code);
        System.out.println("state = " + state);

        //从redis中将state获取出来，和当前传入的state作比较
        //如果一致则放行，如果不一致则抛出异常：非法访问

        //向认证服务器发送请求换取access_token
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";

        baseAccessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);

        String result = null;

        try {
            result = HttpClientUtils.get(baseAccessTokenUrl);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EduException(20001, "获取access_token失败");
        }

        //解析获得result数据
        Gson gson = new Gson();
        HashMap hashMap = gson.fromJson(result, HashMap.class);

        String accessToken = (String) hashMap.get("access_token");
        String openid = (String) hashMap.get("openid");

        //查询数据库当前用用户是否曾经使用过微信登录
        Member member = userService.getMemberByOpenid(openid);
        if(member == null){
            System.out.println("新用户注册");
            //访问微信的资源服务器，获取用户信息
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            baseUserInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);

            String resultUserInfo = null;
            try {
                resultUserInfo = HttpClientUtils.get(baseUserInfoUrl);
                System.out.println("resultUserInfo==========" + resultUserInfo);
            } catch (Exception e) {
                e.printStackTrace();
                throw new EduException(20001, "获得用户信息失败");
            }

            //解析返回数据
            HashMap userInfoMap = gson.fromJson(resultUserInfo, HashMap.class);
            String nickname = (String) userInfoMap.get("nickname"); // 微信名
            String headimgurl = (String) userInfoMap.get("headimgurl"); // 微信头像

            member = new Member();
            member.setNickname(nickname);
            member.setOpenid(openid);
            member.setAvatar(headimgurl);

            String id = userService.saveMember(member);
            member.setId(id);

        }
        // TODO 登录


        // 生成jwt
        String token = JwtUtils.geneJsonWebToken(member);

        //存入cookie 非空判断
        //CookieUtils.setCookie(request, response, "guli_jwt_token", token);

        //因为端口号不同存在蛞蝓问题，cookie不能跨域，所以这里使用url重写

        return "redirect:http://localhost:3000?token=" + token;
    }
}
