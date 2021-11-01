package com.asswei.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
    @RequestMapping({"/", "/index"})
    public String toIndex(Model model){
        model.addAttribute("msg", "hello,shiro");
        return "index";
    }
    @RequestMapping("/user/add")
    public String add(){
        return "user/add";
    }
    @RequestMapping("/user/update")
    public String update(){
        return "user/update";
    }
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        //已经自动注入了securityManager
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            //调用securityManager的认证，根据用户名查询密码，并比较
            subject.login(token);
            return "index";
        } catch (UnknownAccountException uae){
            model.addAttribute("msg","用户名错误");
            return "login";
        } catch (IncorrectCredentialsException ice){
            model.addAttribute("msg","密码错误");
            return "login";
        }
    }

    @RequestMapping("/noauthor")
    @ResponseBody
    public String unauthorized(){
        return "未经授权无法访问此页面";
    }
}
