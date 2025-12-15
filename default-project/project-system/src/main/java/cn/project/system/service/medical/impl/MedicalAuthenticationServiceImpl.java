package cn.project.system.service.medical.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalAuthentication;
import cn.project.system.mapper.medical.MedicalAuthenticationMapper;
import cn.project.system.service.medical.IMedicalAuthenticationService;

/**
 * 医简历认证Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalAuthenticationServiceImpl implements IMedicalAuthenticationService
{
    @Autowired
    private MedicalAuthenticationMapper authenticationMapper;

    @Override
    public MedicalAuthentication selectAuthenticationByUid(String uid)
    {
        return authenticationMapper.selectAuthenticationByUid(uid);
    }

    @Override
    public int insertAuthentication(MedicalAuthentication authentication)
    {
        return authenticationMapper.insertAuthentication(authentication);
    }

    @Override
    public int updateAuthentication(MedicalAuthentication authentication)
    {
        return authenticationMapper.updateAuthentication(authentication);
    }
}

