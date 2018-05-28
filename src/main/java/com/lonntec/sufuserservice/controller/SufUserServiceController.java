package com.lonntec.sufuserservice.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lonntec.framework.annotation.RequestSufToken;
import com.lonntec.sufuserservice.service.SufUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@CrossOrigin
@RestController
@RequestMapping("/sufuser")
public class SufUserServiceController {
    @Autowired
    SufUserService sufUserService;

    /**
     *
     *创建用户
     */
    @RequestSufToken
    @RequestMapping("/create")
    public JSONObject createDomainUser(@RequestBody JSONObject jsonObject){
        String userName=jsonObject.getString("userName");
        String password=jsonObject.getString("password");
        String mobile=jsonObject.getString("mobile");
        String email=jsonObject.getString("email");
        String domainNumber=jsonObject.getString("domainNumber");
        return sufUserService.createDomainUser(userName,password,mobile,email,domainNumber);
    }
    /**
     *
     * 获取suf用户列表
     */
    @RequestSufToken
    @RequestMapping("/list")
    public JSONArray getDomainUserList(
            @PathParam("domainNumber") @Nullable String domainNumber,
            @PathParam("keyword") @Nullable String keyword,
            @PathParam("page") @Nullable Integer page,
            @PathParam("size") @Nullable Integer size
    ){
        return sufUserService.getDomainUserList(domainNumber,keyword,page,size);
    }
    /**
     *
     * 获取suf用户数量
     */
    @RequestSufToken
    @RequestMapping("/listcount")
    public Integer getDomainUserCount(
            @PathParam("domainNumber") @Nullable String domainNumber,
            @PathParam("keyword") @Nullable String keyword
    ){
        return sufUserService.getDomainUserCount(domainNumber,keyword);
    }
    /**
     *
     * 修改suf用户
     */
    @RequestSufToken
    @RequestMapping("/modify")
    public JSONObject modifyDomainUser(@RequestBody JSONObject jsonObject){
        String rowId=jsonObject.getString("rowId");
        String userName=jsonObject.getString("userName");
        String mobile=jsonObject.getString("mobile");
        String email=jsonObject.getString("email");
        String domainNumber=jsonObject.getString("domainNumber");
        return sufUserService.modifyDomainUser(rowId,userName,mobile,email,domainNumber);
    }
    /**
     *
     * 删除suf用户
     */
    @RequestSufToken
    @RequestMapping("/remove")
    public void removeDomainUser(@RequestBody JSONObject jsonObject){
        String rowId=jsonObject.getString("rowId");
        String domainNumber=jsonObject.getString("domainNumber");
        sufUserService.delDomainUser(rowId,domainNumber);
    }
    /**
     *
     * 修改suf用户密码
     */
    @RequestSufToken
    @RequestMapping("/modifypassword")
    public void modifyDomainUserPassword(@RequestBody JSONObject jsonObject){
        String rowId=jsonObject.getString("rowId");
        String domainNumber=jsonObject.getString("domainNumber");
        String oldPassword=jsonObject.getString("oldPassword");
        String newPassword=jsonObject.getString("newPassword");
        sufUserService.modifyDomainUserPassword(rowId,domainNumber,oldPassword,newPassword);
    }
    /**
     *
     * 重置suf用户密码
     */
    @RequestSufToken
    @RequestMapping("/resetpassword")
    public void resetDomainUserPassword(@RequestBody JSONObject jsonObject){
        String rowId=jsonObject.getString("rowId");
        String domainNumber=jsonObject.getString("domainNumber");
        String newPassword=jsonObject.getString("newPassword");
        sufUserService.resetDomainUserPassword(rowId,domainNumber,newPassword);
    }
    /**
     *
     * 启用/禁用suf用户
     */
    @RequestSufToken
    @RequestMapping("/setenable")
    public void setDomainUserEnable(@RequestBody JSONObject jsonObject){
        String rowId=jsonObject.getString("rowId");
        String domainNumber=jsonObject.getString("domainNumber");
        Boolean isEnable=jsonObject.getBoolean("isEnable");
        sufUserService.setDomainUserEnable(rowId,domainNumber,isEnable);
    }

    /**
     *
     * 设置suf管理员权限
     */
    @RequestSufToken
    @RequestMapping("/setadmin")
    public void setDomainUserAdmin(@RequestBody JSONObject jsonObject){
        String rowId=jsonObject.getString("rowId");
        String domainNumber=jsonObject.getString("domainNumber");
        Boolean isAdmin=jsonObject.getBoolean("isAdmin");
        sufUserService.setDomainUserAdmin(rowId,domainNumber,isAdmin);
    }

}
