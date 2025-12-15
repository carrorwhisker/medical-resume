package cn.project.system.service.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalSignUp;

/**
 * 医简历报名Service接口
 * 
 * @author medical
 */
public interface IMedicalSignUpService
{
    /**
     * 查询报名列表
     * 
     * @param signUp 报名信息
     * @return 报名集合
     */
    public List<MedicalSignUp> selectSignUpList(MedicalSignUp signUp);
    
    /**
     * 根据ID查询报名
     * 
     * @param id 报名ID
     * @return 报名信息
     */
    public MedicalSignUp selectSignUpById(Long id);
    
    /**
     * 根据UID和会议ID查询报名
     * 
     * @param uid 用户UID
     * @param meetingId 会议ID
     * @return 报名信息
     */
    public MedicalSignUp selectSignUpByUidAndMeetingId(String uid, Long meetingId);
    
    /**
     * 新增报名
     * 
     * @param signUp 报名信息
     * @return 结果
     */
    public int insertSignUp(MedicalSignUp signUp);
    
    /**
     * 修改报名
     * 
     * @param signUp 报名信息
     * @return 结果
     */
    public int updateSignUp(MedicalSignUp signUp);
    
    /**
     * 删除报名
     * 
     * @param id 报名ID
     * @return 结果
     */
    public int deleteSignUpById(Long id);
}

