package cn.project.system.service.medical.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.mapper.medical.MedicalTransportationMapper;
import cn.project.system.domain.medical.MedicalTransportation;
import cn.project.system.service.medical.IMedicalTransportationService;

/**
 * 交通信息Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalTransportationServiceImpl implements IMedicalTransportationService 
{
    @Autowired
    private MedicalTransportationMapper transportationMapper;

    /**
     * 查询交通信息列表
     * 
     * @param transportation 交通信息
     * @return 交通信息集合
     */
    @Override
    public List<MedicalTransportation> selectTransportationList(MedicalTransportation transportation)
    {
        return transportationMapper.selectTransportationList(transportation);
    }

    /**
     * 根据ID查询交通信息
     * 
     * @param id 交通信息ID
     * @return 交通信息
     */
    @Override
    public MedicalTransportation selectTransportationById(Long id)
    {
        return transportationMapper.selectTransportationById(id);
    }

    /**
     * 根据行程ID查询交通信息列表
     * 
     * @param itineraryId 行程ID
     * @return 交通信息集合
     */
    @Override
    public List<MedicalTransportation> selectTransportationByItineraryId(Long itineraryId)
    {
        return transportationMapper.selectTransportationByItineraryId(itineraryId);
    }

    /**
     * 新增交通信息
     * 
     * @param transportation 交通信息
     * @return 结果
     */
    @Override
    public int insertTransportation(MedicalTransportation transportation)
    {
        transportation.setCreateTimeStamp(System.currentTimeMillis() / 1000);
        transportation.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        return transportationMapper.insertTransportation(transportation);
    }

    /**
     * 修改交通信息
     * 
     * @param transportation 交通信息
     * @return 结果
     */
    @Override
    public int updateTransportation(MedicalTransportation transportation)
    {
        transportation.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        return transportationMapper.updateTransportation(transportation);
    }

    /**
     * 删除交通信息
     * 
     * @param id 交通信息ID
     * @return 结果
     */
    @Override
    public int deleteTransportationById(Long id)
    {
        return transportationMapper.deleteTransportationById(id);
    }

    /**
     * 根据行程ID删除交通信息
     * 
     * @param itineraryId 行程ID
     * @return 结果
     */
    @Override
    public int deleteTransportationByItineraryId(Long itineraryId)
    {
        return transportationMapper.deleteTransportationByItineraryId(itineraryId);
    }
}

