package com.lonntec.sufuserservice.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public interface SufUserService {
    JSONArray getDomainUserList(String domainNumber,String keyword, Integer page, Integer size);

    Integer getDomainUserCount(String domainNumber,String keyword);

    JSONObject createDomainUser(String userName, String password, String mobile, String email, String domainNumber);

    JSONObject modifyDomainUser(String rowId,String userName,String mobile,String email,String domainNumber);

    void delDomainUser(String rowId,String domainNumber);

    void modifyDomainUserPassword(String rowId,String domainNumber,String oldPassword,String newPassword);

    void resetDomainUserPassword(String rowId,String domainNumber,String newPassword);

    void setDomainUserEnable(String rowId,String domainNumber,Boolean isEnable);

    void setDomainUserAdmin(String rowId,String domainNumber,Boolean isAdmin);
}
