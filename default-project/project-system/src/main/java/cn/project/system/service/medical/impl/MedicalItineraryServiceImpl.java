package cn.project.system.service.medical.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.mapper.medical.MedicalItineraryMapper;
import cn.project.system.domain.medical.MedicalItinerary;
import cn.project.system.service.medical.IMedicalItineraryService;

/**
 * 行程管理Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalItineraryServiceImpl implements IMedicalItineraryService 
{
    @Autowired
    private MedicalItineraryMapper itineraryMapper;

    /**
     * 查询行程列表
     * 
     * @param itinerary 行程信息
     * @return 行程集合
     */
    @Override
    public List<MedicalItinerary> selectItineraryList(MedicalItinerary itinerary)
    {
        return itineraryMapper.selectItineraryList(itinerary);
    }

    /**
     * 根据ID查询行程
     * 
     * @param id 行程ID
     * @return 行程信息
     */
    @Override
    public MedicalItinerary selectItineraryById(Long id)
    {
        return itineraryMapper.selectItineraryById(id);
    }

    /**
     * 根据报名ID查询行程
     * 
     * @param signUpId 报名ID
     * @return 行程信息
     */
    @Override
    public MedicalItinerary selectItineraryBySignUpId(Long signUpId)
    {
        return itineraryMapper.selectItineraryBySignUpId(signUpId);
    }

    /**
     * 根据UID和会议ID查询行程
     * 
     * @param uid 用户UID
     * @param meetingId 会议ID
     * @return 行程信息
     */
    @Override
    public MedicalItinerary selectItineraryByUidAndMeetingId(String uid, Long meetingId)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("uid", uid);
        params.put("meetingId", meetingId);
        return itineraryMapper.selectItineraryByUidAndMeetingId(params);
    }

    /**
     * 新增行程
     * 
     * @param itinerary 行程信息
     * @return 结果
     */
    @Override
    public int insertItinerary(MedicalItinerary itinerary)
    {
        itinerary.setCreateTimeStamp(System.currentTimeMillis() / 1000);
        itinerary.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        return itineraryMapper.insertItinerary(itinerary);
    }

    /**
     * 修改行程
     * 
     * @param itinerary 行程信息
     * @return 结果
     */
    @Override
    public int updateItinerary(MedicalItinerary itinerary)
    {
        itinerary.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        return itineraryMapper.updateItinerary(itinerary);
    }

    /**
     * 删除行程
     * 
     * @param id 行程ID
     * @return 结果
     */
    @Override
    public int deleteItineraryById(Long id)
    {
        return itineraryMapper.deleteItineraryById(id);
    }

    /**
     * 根据报名ID删除行程
     * 
     * @param signUpId 报名ID
     * @return 结果
     */
    @Override
    public int deleteItineraryBySignUpId(Long signUpId)
    {
        return itineraryMapper.deleteItineraryBySignUpId(signUpId);
    }
}

