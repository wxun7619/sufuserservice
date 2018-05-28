package com.lonntec.sufuserservice.lang;

import com.lonntec.framework.lang.StateCode;

public enum SufUserSystemStateCode implements StateCode{

    UserName_IsEmpty (100001,"请输入用户名"),
    DomainNumber_IsEmpty(100002,"请输入企业域编码"),
    Password_IsEmpty(100003,"请输入密码"),
    Mobile_IsEmpty(100004,"请输入手机号"),
    Mobile_IsRepeat(100005,"手机号码已占用"),
    Email_IsEmpty(100006,"请输入邮箱"),
    Email_ISRepeat(100007,"邮箱已注册"),
    Domain_IsNotExist(100008,"企业域不存在"),
    User_IsNotExist(100009,"用户不存在"),
    OldPassword_IsEmpty(100020,"旧密码不能为空"),
    OldPassword_IsErr(100021,"旧密码不正确"),
    NewPassword_IsEmpty(100022,"新密码不能为空"),
    Password_IsRepeat(100023,"新密码与旧密码不能相同"),
    IsEnable_IsEmpty(100024,"启用/禁用没有传值"),
    IsAdmin_IsEmpty(100025,"设置管理员权限没有传值")

    ;
    private Integer code;
    private String message;
    private String codeName;
    SufUserSystemStateCode(Integer code,String message) {
        this.code=code;
        this.message=message;
        this.codeName=codeName;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getCodeName() {
        return codeName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
