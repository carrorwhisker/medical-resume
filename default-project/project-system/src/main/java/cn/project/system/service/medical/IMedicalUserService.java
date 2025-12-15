package cn.project.system.service.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalUser;

/**
 * 医简历用户Service接口
 * 
 * @author medical
 */
public interface IMedicalUserService
{
    /**
     * 查询医简历用户列表
     * 
     * @param user 医简历用户
     * @return 医简历用户集合
     */
    public List<MedicalUser> selectUserList(MedicalUser user);
    
    /**
     * 根据UID查询用户
     * 
     * @param uid 用户UID
     * @return 用户信息
     */
    public MedicalUser selectUserByUid(String uid);
    
    /**
     * 根据openid查询用户
     * 
     * @param openid 微信openid
     * @return 用户信息
     */
    public MedicalUser selectUserByOpenid(String openid);
    
    /**
     * 新增医简历用户
     * 
     * @param user 医简历用户
     * @return 结果
     */
    public int insertUser(MedicalUser user);
    
    /**
     * 修改医简历用户
     * 
     * @param user 医简历用户
     * @return 结果
     */
    public int updateUser(MedicalUser user);

    /**
     * 根据ID删除用户
     * 
     * @param id 用户ID
     * @return 结果
     */
    public int deleteUserById(Long id);
    
    /**
     * 根据UID删除用户
     * 
     * @param uid 用户UID
     * @return 结果
     */
    public int deleteUserByUid(String uid);
}

