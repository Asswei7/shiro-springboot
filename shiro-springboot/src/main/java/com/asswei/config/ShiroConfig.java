package com.asswei.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    // shiroFilterFactoryBean负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);
        //添加shiro的内置过滤器
        /*
            anon:无需认证就可以访问
            authc:必须认证才能访问
            user:必须有  记住我  才能访问
            perms: 拥有对某个资源的权限才能访问
            role: 拥有对某个角色权限才能访问
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        //受限的资源有哪些
        filterMap.put("/user/add","perms[user:admin]");
        filterMap.put("/user/update","perms[user:admin]");
        //filterMap.put("/user/add","anon");
        bean.setFilterChainDefinitionMap(filterMap);
        //访问受限的资源，会跳到这设置登录的请求，点击页面后会跳转到login页面
        bean.setLoginUrl("/toLogin");
        //未经授权登陆后跳到该页面
        bean.setUnauthorizedUrl("/noauthor");
        return bean;
    }
    //DefaultWebSecurityManager
    //方法名就是对象名，将UserRealm绑定
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    //创建 realm 对象，需要自定义类
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    //整合ShiroDialect:用来整合shiro thymleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
