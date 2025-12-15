package cn.project.system.service.medical;

import cn.project.system.domain.medical.MedicalAuthentication;

/**
 * 医简历认证Service接口
 * 
 * @author medical
 */
public interface IMedicalAuthenticationService
{
    /**
     * 根据UID查询认证信息
     * 
     * @param uid 用户UID
     * @return 认证信息
     */
    public MedicalAuthentication selectAuthenticationByUid(String uid);
    
    /**
     * 新增认证信息
     * 
     * @param authentication 认证信息
     * @return 结果
     */
    public int insertAuthentication(MedicalAuthentication authentication);
    
    /**
     * 修改认证信息
     * 
     * @param authentication 认证信息
     * @return 结果
     */
    public int updateAuthentication(MedicalAuthentication authentication);
}

