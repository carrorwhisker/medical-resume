package cn.project.system.service.medical.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalMeeting;
import cn.project.system.mapper.medical.MedicalMeetingMapper;
import cn.project.system.service.medical.IMedicalMeetingService;

/**
 * 医简历会议Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalMeetingServiceImpl implements IMedicalMeetingService
{
    @Autowired
    private MedicalMeetingMapper meetingMapper;

    /**
     * 查询医简历会议列表
     * 
     * @param meeting 医简历会议
     * @return 医简历会议
     */
    @Override
    public List<MedicalMeeting> selectMeetingList(MedicalMeeting meeting)
    {
        return meetingMapper.selectMeetingList(meeting);
    }

    /**
     * 根据ID查询会议
     * 
     * @param id 会议ID
     * @return 会议信息
     */
    @Override
    public MedicalMeeting selectMeetingById(Long id)
    {
        return meetingMapper.selectMeetingById(id);
    }

    /**
     * 新增医简历会议
     * 
     * @param meeting 医简历会议
     * @return 结果
     */
    @Override
    public int insertMeeting(MedicalMeeting meeting)
    {
        return meetingMapper.insertMeeting(meeting);
    }

    /**
     * 修改医简历会议
     * 
     * @param meeting 医简历会议
     * @return 结果
     */
    @Override
    public int updateMeeting(MedicalMeeting meeting)
    {
        return meetingMapper.updateMeeting(meeting);
    }

    /**
     * 删除医简历会议
     * 
     * @param id 会议ID
     * @return 结果
     */
    @Override
    public int deleteMeetingById(Long id)
    {
        return meetingMapper.deleteMeetingById(id);
    }
}

