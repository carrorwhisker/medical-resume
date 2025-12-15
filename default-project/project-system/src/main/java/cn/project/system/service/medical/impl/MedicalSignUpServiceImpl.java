package cn.project.system.service.medical.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalSignUp;
import cn.project.system.mapper.medical.MedicalSignUpMapper;
import cn.project.system.service.medical.IMedicalSignUpService;

/**
 * 医简历报名Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalSignUpServiceImpl implements IMedicalSignUpService
{
    @Autowired
    private MedicalSignUpMapper signUpMapper;

    @Override
    public List<MedicalSignUp> selectSignUpList(MedicalSignUp signUp)
    {
        return signUpMapper.selectSignUpList(signUp);
    }

    @Override
    public MedicalSignUp selectSignUpById(Long id)
    {
        return signUpMapper.selectSignUpById(id);
    }

    @Override
    public MedicalSignUp selectSignUpByUidAndMeetingId(String uid, Long meetingId)
    {
        return signUpMapper.selectSignUpByUidAndMeetingId(uid, meetingId);
    }

    @Override
    public int insertSignUp(MedicalSignUp signUp)
    {
        return signUpMapper.insertSignUp(signUp);
    }

    @Override
    public int updateSignUp(MedicalSignUp signUp)
    {
        return signUpMapper.updateSignUp(signUp);
    }

    @Override
    public int deleteSignUpById(Long id)
    {
        return signUpMapper.deleteSignUpById(id);
    }
}

