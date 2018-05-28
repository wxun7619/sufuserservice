package com.lonntec.sufuserservice.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "t_domain_domainuser_rel")
public class Domain_DomainUser_Rel {
    @Id
    @Column(name = "frowid",length = 36)
    String rowId;

    @Column(name = "fdomainid")
    String domainId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fdomainuserid")
    DomainUser domainUser;

    @Column(name = "fismaster")
    Boolean isMaster;

    @Column(name = "fmustmodifypassword")
    Boolean mustModifyPassword;

    @Column(name = "flastlogintime")
    Date lastLoginTime;

    @Column(name = "fisenable")
    Boolean isEnable;

    @Column(name = "fisadmin")
    Boolean isAdmin;

    public Domain_DomainUser_Rel() {
        rowId= UUID.randomUUID().toString();
        isMaster=false;
        isAdmin=false;
        isEnable=false;
        mustModifyPassword=false;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public DomainUser getDomainUser() {
        return domainUser;
    }

    public void setDomainUser(DomainUser domainUser) {
        this.domainUser = domainUser;
    }

    public Boolean getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(Boolean master) {
        isMaster = master;
    }

    public Boolean getIsMustModifyPassword() {
        return mustModifyPassword;
    }

    public void setIsMustModifyPassword(Boolean mustModifyPassword) {
        this.mustModifyPassword = mustModifyPassword;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @JsonProperty("isEnable")
    @JSONField(name = "isEnable")
    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean enable) {
        isEnable = enable;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
