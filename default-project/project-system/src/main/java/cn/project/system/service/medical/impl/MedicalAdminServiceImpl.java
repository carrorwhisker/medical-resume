package cn.project.system.service.medical.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalAdmin;
import cn.project.system.mapper.medical.MedicalAdminMapper;
import cn.project.system.service.medical.IMedicalAdminService;

/**
 * 医简历管理员Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalAdminServiceImpl implements IMedicalAdminService
{
    @Autowired
    private MedicalAdminMapper adminMapper;

    @Override
    public List<MedicalAdmin> selectAdminList(MedicalAdmin admin)
    {
        return adminMapper.selectAdminList(admin);
    }

    @Override
    public MedicalAdmin selectAdminById(Long id)
    {
        return adminMapper.selectAdminById(id);
    }

    @Override
    public MedicalAdmin selectAdminByName(String name)
    {
        return adminMapper.selectAdminByName(name);
    }

    @Override
    public int insertAdmin(MedicalAdmin admin)
    {
        return adminMapper.insertAdmin(admin);
    }

    @Override
    public int updateAdmin(MedicalAdmin admin)
    {
        return adminMapper.updateAdmin(admin);
    }

    @Override
    public int deleteAdminById(Long id)
    {
        return adminMapper.deleteAdminById(id);
    }
}

