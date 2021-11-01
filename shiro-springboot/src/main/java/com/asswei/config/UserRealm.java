package com.asswei.config;

import com.asswei.pojo.User;
import com.asswei.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;


//自定义的UserRealm，都需要继承自AuthorizingRealm，需要实现他的两个方法
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权的功能");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //拿到当前登录的这个对象
        Subject subject = SecurityUtils.getSubject();
        //根据用户获取其权限信息
        User currentUser = (User) subject.getPrincipal();
        //设置当前用户的权限
        info.addStringPermission(currentUser.getPerms());
        return info;
    }

    //认证,从subject.login(token)跳过来的，将token与ground truth的token进行比较，进行了用户名的校验
    //SimpleAccountRealm中的doGetAuthenticationInfo进行用户名校验
    //AuthenticatingRealm中的 doCredentialMatch进行密码的校验，是自动完成的
    //从token中获取用户名，根据用户名查询密码并返回
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了认证过程");
//        String name = "root";
//        String password = "123";
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        User user = userService.queryUserByName(userToken.getUsername());
        if(user == null){
            return null;
        }
//        if (!userToken.getUsername().equals(name)){
//            return null;
//        }
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser",user);
        return new SimpleAuthenticationInfo(user,user.getPwd(),"");
//        return new SimpleAuthenticationInfo("",password,"");
    }
}
