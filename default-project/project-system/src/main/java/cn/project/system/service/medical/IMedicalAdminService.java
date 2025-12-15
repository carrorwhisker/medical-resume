package cn.project.system.service.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalAdmin;

/**
 * 医简历管理员Service接口
 * 
 * @author medical
 */
public interface IMedicalAdminService
{
    /**
     * 查询管理员列表
     * 
     * @param admin 管理员信息
     * @return 管理员集合
     */
    public List<MedicalAdmin> selectAdminList(MedicalAdmin admin);
    
    /**
     * 根据ID查询管理员
     * 
     * @param id 管理员ID
     * @return 管理员信息
     */
    public MedicalAdmin selectAdminById(Long id);
    
    /**
     * 根据账户名查询管理员
     * 
     * @param name 账户名
     * @return 管理员信息
     */
    public MedicalAdmin selectAdminByName(String name);
    
    /**
     * 新增管理员
     * 
     * @param admin 管理员信息
     * @return 结果
     */
    public int insertAdmin(MedicalAdmin admin);
    
    /**
     * 修改管理员
     * 
     * @param admin 管理员信息
     * @return 结果
     */
    public int updateAdmin(MedicalAdmin admin);
    
    /**
     * 删除管理员
     * 
     * @param id 管理员ID
     * @return 结果
     */
    public int deleteAdminById(Long id);
}

