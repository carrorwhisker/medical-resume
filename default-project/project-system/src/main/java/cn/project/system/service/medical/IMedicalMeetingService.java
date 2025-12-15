package cn.project.system.service.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalMeeting;

/**
 * 医简历会议Service接口
 * 
 * @author medical
 */
public interface IMedicalMeetingService
{
    /**
     * 查询医简历会议列表
     * 
     * @param meeting 医简历会议
     * @return 医简历会议集合
     */
    public List<MedicalMeeting> selectMeetingList(MedicalMeeting meeting);
    
    /**
     * 根据ID查询会议
     * 
     * @param id 会议ID
     * @return 会议信息
     */
    public MedicalMeeting selectMeetingById(Long id);
    
    /**
     * 新增医简历会议
     * 
     * @param meeting 医简历会议
     * @return 结果
     */
    public int insertMeeting(MedicalMeeting meeting);
    
    /**
     * 修改医简历会议
     * 
     * @param meeting 医简历会议
     * @return 结果
     */
    public int updateMeeting(MedicalMeeting meeting);
    
    /**
     * 删除医简历会议
     * 
     * @param id 会议ID
     * @return 结果
     */
    public int deleteMeetingById(Long id);
}

