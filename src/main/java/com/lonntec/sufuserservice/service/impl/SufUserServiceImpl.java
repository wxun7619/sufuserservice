package com.lonntec.sufuserservice.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lonntec.framework.lang.MicroServiceException;
import com.lonntec.sufuserservice.entity.Domain;
import com.lonntec.sufuserservice.entity.DomainUser;
import com.lonntec.sufuserservice.entity.Domain_DomainUser_Rel;
import com.lonntec.sufuserservice.lang.SufUserSystemException;
import com.lonntec.sufuserservice.lang.SufUserSystemStateCode;
import com.lonntec.sufuserservice.repository.DomainRepository;
import com.lonntec.sufuserservice.repository.RelRepository;
import com.lonntec.sufuserservice.repository.SufUserRepository;
import com.lonntec.sufuserservice.service.SufUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Transactional(rollbackOn = {MicroServiceException.class, RuntimeException.class})
@Service
public class SufUserServiceImpl implements SufUserService{
    @Autowired
    SufUserRepository sufUserRepository;
    @Autowired
    DomainRepository domainRepository;
    @Autowired
    RelRepository relRepository;
    /**
     *
     * 获取suf用户列表
     */
    @Override
    public JSONArray getDomainUserList(String domainNumber,String keyword, Integer page, Integer size) {
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if(!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        String domainId=domainOptional.get().getRowId();
        Integer queryPage = page==null || page <= 0 ? 1 : page;
        Integer querySize = size==null || size <= 0 ? 25 : size;
        PageRequest pageable = new PageRequest(queryPage -1, querySize);
        String queryKeywork = keyword== null || keyword.replaceAll("\\s*","").equals("") ? "%" : keyword;
        if(!queryKeywork.contains("%")){
            queryKeywork = "%" + queryKeywork + "%";
        }
        List<Domain_DomainUser_Rel> list=relRepository.findAllByMyQuery(domainId,queryKeywork);
        JSONArray jsonArray=new JSONArray();
        for (Domain_DomainUser_Rel item:list){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("rowId",item.getDomainUser().getRowId());
            jsonObject.put("userName",item.getDomainUser().getUserName());
            jsonObject.put("mobile",item.getDomainUser().getMobile());
            jsonObject.put("email",item.getDomainUser().getEmail());
            jsonObject.put("isMaster",item.getIsMaster());
            jsonObject.put("isAdmin",item.getIsAdmin());
            jsonObject.put("isEnable",item.getIsEnable());
            jsonObject.put("lastLoginTime",item.getLastLoginTime());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
    /**
     *
     * 获取suf用户数量
     */
    @Override
    public Integer getDomainUserCount(String domainNumber, String keyword) {
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if(!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        String domainId=domainOptional.get().getRowId();
        String queryKeywork = keyword== null || keyword.replaceAll("\\s*","").equals("") ? "%" : keyword;
        if(!queryKeywork.contains("%")){
            queryKeywork = "%" + queryKeywork + "%";
        }
        return relRepository.countByMyQuery(domainId,queryKeywork);
    }

    /**
     *
     *新建suf用户
     */
    @Override
    public JSONObject createDomainUser(String userName, String password, String mobile, String email, String domainNumber) {
        //判断用户名，企业域编码，密码，手机号，邮箱是否为空
        if(userName==null||userName.replaceAll("\\s*","").equals("")){
             throw new SufUserSystemException(SufUserSystemStateCode.UserName_IsEmpty);
        }else if (password==null||password.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.Password_IsEmpty);
        }else if (mobile==null||mobile.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.Mobile_IsEmpty);
        }else if (email==null||email.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.Email_IsEmpty);
        }else if (domainNumber==null||domainNumber.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.DomainNumber_IsEmpty);
        }
        // 邮箱是否冲突
        Optional<DomainUser> domainUserByEmail=sufUserRepository.findByEmail(email);
        if(domainUserByEmail.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Email_ISRepeat);
        }
        //获取企业域信息
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if(!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        //判断手机号是否存在,添加到企业域中
        Optional<DomainUser> domainUserByMobile=sufUserRepository.findByMobile(mobile);
        DomainUser domainUser=new DomainUser();
        domainUser.setUserName(userName);
        domainUser.setPassword(password);
        domainUser.setMobile(mobile);
        domainUser.setEmail(email);
        if(!domainUserByMobile.isPresent()){
            sufUserRepository.save(domainUser);
        }
        Domain_DomainUser_Rel domain_domainUser_rel=new Domain_DomainUser_Rel();
        domain_domainUser_rel.setDomainId(domainOptional.get().getRowId());
        domain_domainUser_rel.setDomainUser(domainUser);
        Calendar calendar=Calendar.getInstance();
        domain_domainUser_rel.setLastLoginTime(calendar.getTime());
        relRepository.save(domain_domainUser_rel);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("rowId",domainUser.getRowId());
        jsonObject.put("userName",userName);
        jsonObject.put("mobile",mobile);
        jsonObject.put("email",email);
        jsonObject.put("isAdmin",false);
        jsonObject.put("isEnable",false);
        return jsonObject;
    }
    /**
     *
     * 修改suf用户
     */
    @Override
    public JSONObject modifyDomainUser(String rowId,String userName,String mobile,String email,String domainNumber) {
        //判断用户内码，用户名，手机号，邮箱，企业域编码
        if(rowId==null){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }else if (userName==null||userName.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.UserName_IsEmpty);
        }else if (mobile==null||mobile.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.Mobile_IsEmpty);
        }else if (email==null||email.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.Email_IsEmpty);
        }else if (domainNumber==null||domainNumber.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.DomainNumber_IsEmpty);
        }
        //用户是否存在
        Optional<DomainUser> domainUserById=sufUserRepository.findById(rowId);
        if(!domainUserById.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        //获取企业域信息,判断企业域是否存在
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if(!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        // 邮箱是否冲突
        Optional<DomainUser> domainUserByEmail=sufUserRepository.findByEmail(email);
        if(domainUserByEmail.isPresent()&&domainUserByEmail.get().getRowId()!=rowId){
            throw new SufUserSystemException(SufUserSystemStateCode.Email_ISRepeat);
        }
        //判断手机号是否占用
        Optional<DomainUser> domainUserByMobile=sufUserRepository.findByMobile(mobile);
        if(domainUserByMobile.isPresent()&&domainUserByMobile.get().getRowId()!=rowId){
            throw new SufUserSystemException(SufUserSystemStateCode.Mobile_IsRepeat);
        }
        //修改信息
        DomainUser domainUser=domainUserById.get();
        domainUser.setUserName(userName);
        domainUser.setMobile(mobile);
        domainUser.setEmail(email);
        sufUserRepository.save(domainUser);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("rowId",domainUser.getRowId());
        jsonObject.put("userName",userName);
        jsonObject.put("mobile",mobile);
        jsonObject.put("email",email);
        jsonObject.put("isAdmin",false);
        jsonObject.put("isEnable",false);
        return jsonObject;

    }
    /**
     *
     * 删除suf用户
     */
    @Override
    public void delDomainUser(String rowId, String domainNumber) {
        //判断rowId,domainNumber是否为空
        if(rowId==null){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }else if (domainNumber==null){
            throw new SufUserSystemException(SufUserSystemStateCode.DomainNumber_IsEmpty);
        }
        //判断用户，企业域是否存在
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if (!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        String domainId=domainOptional.get().getRowId();
        Optional<Domain_DomainUser_Rel> rel=relRepository.findByMyQuery(rowId,domainId);
        if(!rel.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        //删除关联表中的数据
        relRepository.deleteById(rel.get().getRowId());
    }
    /**
     *
     * 修改suf用户密码
     */
    @Override
    public void modifyDomainUserPassword(String rowId, String domainNumber, String oldPassword, String newPassword) {
        //判断rowId，domainNumber,oldPassword,newPassword是否为空
        if(rowId==null){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }else if (domainNumber==null){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }else if (oldPassword==null||oldPassword.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.OldPassword_IsEmpty);
        }else if (newPassword==null||newPassword.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.NewPassword_IsEmpty);
        }
        //判断企业域，用户是否存在
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if (!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        String domainId=domainOptional.get().getRowId();
        Optional<Domain_DomainUser_Rel> rel=relRepository.findByMyQuery(rowId,domainId);
        if(!rel.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        Optional<DomainUser> domainUser=sufUserRepository.findById(rowId);
        if(!domainUser.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        DomainUser dbDomainUser=domainUser.get();
        //判断旧密码是否正确
        DomainUser paramDomainUser=new DomainUser();
        paramDomainUser.setPassword(oldPassword);
        if(!paramDomainUser.getPasswordHash().equals(dbDomainUser.getPasswordHash())){
            throw new SufUserSystemException(SufUserSystemStateCode.OldPassword_IsErr);
        }else if(oldPassword.equals(newPassword)){
            throw new SufUserSystemException(SufUserSystemStateCode.Password_IsRepeat);
        }
        dbDomainUser.setPassword(newPassword);
        sufUserRepository.save(dbDomainUser);
    }
    /**
     *
     * 重置suf用户密码
     */
    @Override
    public void resetDomainUserPassword(String rowId, String domainNumber, String newPassword) {
        //判断rowId，domainNumber,oldPassword,newPassword是否为空
        if(rowId==null){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }else if (domainNumber==null){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }else if (newPassword==null||newPassword.replaceAll("\\s*","").equals("")){
            throw new SufUserSystemException(SufUserSystemStateCode.NewPassword_IsEmpty);
        }
        //判断企业域，用户是否存在
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if (!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        String domainId=domainOptional.get().getRowId();
        Optional<Domain_DomainUser_Rel> rel=relRepository.findByMyQuery(rowId,domainId);
        if(!rel.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        Optional<DomainUser> domainUser=sufUserRepository.findById(rowId);
        if(!domainUser.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        DomainUser dbDomainUser=domainUser.get();
        dbDomainUser.setPassword(newPassword);
        sufUserRepository.save(dbDomainUser);
    }
    /**
     *
     * 启用/禁用suf用户
     */
    @Override
    public void setDomainUserEnable(String rowId, String domainNumber, Boolean isEnable) {
        if(rowId==null){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }else if(isEnable==null){
            throw new SufUserSystemException(SufUserSystemStateCode.IsEnable_IsEmpty);
        }else if (domainNumber==null){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        //判断企业域，用户是否存在
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if (!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        String domainId=domainOptional.get().getRowId();
        Optional<Domain_DomainUser_Rel> rel=relRepository.findByMyQuery(rowId,domainId);
        if(!rel.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        Domain_DomainUser_Rel domain_domainUser_rel=rel.get();
        domain_domainUser_rel.setIsEnable(isEnable);
        relRepository.save(domain_domainUser_rel);
    }
    /**
     *
     * 设置suf管理员权限
     */
    @Override
    public void setDomainUserAdmin(String rowId, String domainNumber, Boolean isAdmin) {
        if(rowId==null){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }else if(isAdmin==null){
            throw new SufUserSystemException(SufUserSystemStateCode.IsAdmin_IsEmpty);
        }else if (domainNumber==null){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        //判断企业域，用户是否存在
        Optional<Domain> domainOptional=domainRepository.findByDomainNumber(domainNumber);
        if (!domainOptional.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.Domain_IsNotExist);
        }
        String domainId=domainOptional.get().getRowId();
        Optional<Domain_DomainUser_Rel> rel=relRepository.findByMyQuery(rowId,domainId);
        if(!rel.isPresent()){
            throw new SufUserSystemException(SufUserSystemStateCode.User_IsNotExist);
        }
        Domain_DomainUser_Rel domain_domainUser_rel=rel.get();
        domain_domainUser_rel.setIsEnable(isAdmin);
        relRepository.save(domain_domainUser_rel);
    }
}
