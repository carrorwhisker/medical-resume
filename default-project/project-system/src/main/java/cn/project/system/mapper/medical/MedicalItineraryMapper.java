package cn.project.system.mapper.medical;

import java.util.List;
import java.util.Map;
import cn.project.system.domain.medical.MedicalItinerary;

/**
 * 行程管理Mapper接口
 * 
 * @author medical
 */
public interface MedicalItineraryMapper 
{
    /**
     * 查询行程列表
     * 
     * @param itinerary 行程信息
     * @return 行程集合
     */
    public List<MedicalItinerary> selectItineraryList(MedicalItinerary itinerary);

    /**
     * 根据ID查询行程
     * 
     * @param id 行程ID
     * @return 行程信息
     */
    public MedicalItinerary selectItineraryById(Long id);

    /**
     * 根据报名ID查询行程
     * 
     * @param signUpId 报名ID
     * @return 行程信息
     */
    public MedicalItinerary selectItineraryBySignUpId(Long signUpId);

    /**
     * 根据UID和会议ID查询行程
     * 
     * @param params 参数Map
     * @return 行程信息
     */
    public MedicalItinerary selectItineraryByUidAndMeetingId(Map<String, Object> params);

    /**
     * 新增行程
     * 
     * @param itinerary 行程信息
     * @return 结果
     */
    public int insertItinerary(MedicalItinerary itinerary);

    /**
     * 修改行程
     * 
     * @param itinerary 行程信息
     * @return 结果
     */
    public int updateItinerary(MedicalItinerary itinerary);

    /**
     * 删除行程
     * 
     * @param id 行程ID
     * @return 结果
     */
    public int deleteItineraryById(Long id);

    /**
     * 根据报名ID删除行程
     * 
     * @param signUpId 报名ID
     * @return 结果
     */
    public int deleteItineraryBySignUpId(Long signUpId);
}

