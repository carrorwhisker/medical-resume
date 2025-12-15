package cn.project.system.service.medical.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalUser;
import cn.project.system.mapper.medical.MedicalUserMapper;
import cn.project.system.service.medical.IMedicalUserService;

/**
 * 医简历用户Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalUserServiceImpl implements IMedicalUserService
{
    @Autowired
    private MedicalUserMapper userMapper;

    /**
     * 查询医简历用户列表
     * 
     * @param user 医简历用户
     * @return 医简历用户
     */
    @Override
    public List<MedicalUser> selectUserList(MedicalUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 根据UID查询用户
     * 
     * @param uid 用户UID
     * @return 用户信息
     */
    @Override
    public MedicalUser selectUserByUid(String uid)
    {
        return userMapper.selectUserByUid(uid);
    }

    /**
     * 根据openid查询用户
     * 
     * @param openid 微信openid
     * @return 用户信息
     */
    @Override
    public MedicalUser selectUserByOpenid(String openid)
    {
        return userMapper.selectUserByOpenid(openid);
    }

    /**
     * 新增医简历用户
     * 
     * @param user 医简历用户
     * @return 结果
     */
    @Override
    public int insertUser(MedicalUser user)
    {
        return userMapper.insertUser(user);
    }

    /**
     * 修改医简历用户
     * 
     * @param user 医简历用户
     * @return 结果
     */
    @Override
    public int updateUser(MedicalUser user)
    {
        return userMapper.updateUser(user);
    }

    @Override
    public int deleteUserById(Long id)
    {
        return userMapper.deleteUserById(id);
    }
    
    @Override
    public int deleteUserByUid(String uid)
    {
        return userMapper.deleteUserByUid(uid);
    }
}

