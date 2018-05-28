package com.lonntec.sufuserservice.lang;

import com.lonntec.framework.lang.StateCode;

public enum SufUserSystemStateCode implements StateCode{

    UserName_IsEmpty (100001,"请输入用户名"),
    DomainNumber_IsEmpty(100002,"请输入企业域编码"),
    Password_IsEmpty(100003,"请输入密码"),
    Mobile_IsEmpty(100004,"请输入手机号"),
    Email_IsEmpty(100005,"请输入邮箱"),
    Email_ISRepeat(100006,"邮箱已注册"),
    Domain_IsNotExist(100007,"企业域不存在"),
    User_IsNotExist(100008,"用户不存在")

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
