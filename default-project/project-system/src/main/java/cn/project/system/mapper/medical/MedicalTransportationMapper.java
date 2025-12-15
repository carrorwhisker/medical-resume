package cn.project.system.mapper.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalTransportation;

/**
 * 交通信息Mapper接口
 * 
 * @author medical
 */
public interface MedicalTransportationMapper 
{
    /**
     * 查询交通信息列表
     * 
     * @param transportation 交通信息
     * @return 交通信息集合
     */
    public List<MedicalTransportation> selectTransportationList(MedicalTransportation transportation);

    /**
     * 根据ID查询交通信息
     * 
     * @param id 交通信息ID
     * @return 交通信息
     */
    public MedicalTransportation selectTransportationById(Long id);

    /**
     * 根据行程ID查询交通信息列表
     * 
     * @param itineraryId 行程ID
     * @return 交通信息集合
     */
    public List<MedicalTransportation> selectTransportationByItineraryId(Long itineraryId);

    /**
     * 新增交通信息
     * 
     * @param transportation 交通信息
     * @return 结果
     */
    public int insertTransportation(MedicalTransportation transportation);

    /**
     * 修改交通信息
     * 
     * @param transportation 交通信息
     * @return 结果
     */
    public int updateTransportation(MedicalTransportation transportation);

    /**
     * 删除交通信息
     * 
     * @param id 交通信息ID
     * @return 结果
     */
    public int deleteTransportationById(Long id);

    /**
     * 根据行程ID删除交通信息
     * 
     * @param itineraryId 行程ID
     * @return 结果
     */
    public int deleteTransportationByItineraryId(Long itineraryId);
}

