package com.nd.controller;

import com.nd.service.UserService;
import com.nd.utils.AppConstant;
import com.nd.vo.User;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 登录
     *
     * @param uname 用户名
     * @param pwd   密码
     */
    @RequestMapping("/login")
    public Object login(String uname, String pwd, HttpServletRequest req) {
        try {
            User user = userService.getByUname(uname);
            if (user == null) {
                return super.fail(4001, "用户不存在！");
            }
            if (!user.getPwd().equals(pwd)) {
                return super.fail(4002, "密码错误！");
            }
            req.getSession().setAttribute(AppConstant.CURRENT_USER, user);
            return super.success("登录成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4003, "登录失败！");
    }

    /**
     * 注销
     */
    @RequestMapping("/logout")
    public Object logout(HttpServletRequest req) {
        req.getSession().removeAttribute(AppConstant.CURRENT_USER);
        return super.success("注销成功！");
    }

    /**
     * 注册
     *
     * @param uname 用户名
     * @param pwd   密码
     * @param phone 手机号
     * @param email 邮箱
     */
    @RequestMapping("/register")
    public Object register(String uname, String pwd, String phone, String email) {
        if (StringUtils.isEmpty(uname)) {
            return super.fail(4001, "用户名不能为空");
        }
        if (StringUtils.isEmpty(pwd)) {
            return super.fail(4002, "密码不能为空");
        }
        if (StringUtils.isEmpty(phone)) {
            return super.fail(4003, "手机号不能为空");
        }
        if (StringUtils.isEmpty(email)) {
            return super.fail(4004, "邮箱不能为空");
        }
        User user = userService.getByUname(uname);
        if (user != null) {
            return super.fail(4001, "用户名已存在！");
        }
        user = new User();
        user.setUname(uname);
        user.setPwd(pwd);
        user.setPhone(phone);
        user.setEmail(email);
        userService.save(user);
        return super.success("注册成功！");
    }

}
