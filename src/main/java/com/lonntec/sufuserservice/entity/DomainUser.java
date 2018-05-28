package com.lonntec.sufuserservice.entity;

import javax.persistence.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "t_domain_domainuser")
public class DomainUser {
    @Id
    @Column(name = "frowid",length = 36)
    String rowId;

    @Column(name = "fusername")
    String userName;

    @Transient
    String Password;

    @Column(name = "fpasswordhash")
    String passwordHash;

    @Column(name = "fmobile")
    String mobile;

    @Column(name = "femail")
    String email;

    public DomainUser() {
        rowId= UUID.randomUUID().toString();
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String Password) {
        this.Password = Password;
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5Data = digest.digest(this.Password.getBytes("utf-8"));
            this.passwordHash =  Base64.getEncoder().encodeToString(md5Data);
        }catch (Exception ex){
            ex.printStackTrace();
            this.passwordHash = this.Password;
        }
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
