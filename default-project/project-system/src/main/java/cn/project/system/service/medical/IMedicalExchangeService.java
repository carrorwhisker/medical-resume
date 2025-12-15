package cn.project.system.service.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalExchange;

/**
 * 医简历交换Service接口
 * 
 * @author medical
 */
public interface IMedicalExchangeService
{
    /**
     * 查询交换列表
     * 
     * @param exchange 交换信息
     * @return 交换集合
     */
    public List<MedicalExchange> selectExchangeList(MedicalExchange exchange);
    
    /**
     * 根据ID查询交换
     * 
     * @param id 交换ID
     * @return 交换信息
     */
    public MedicalExchange selectExchangeById(Long id);
    
    /**
     * 新增交换
     * 
     * @param exchange 交换信息
     * @return 结果
     */
    public int insertExchange(MedicalExchange exchange);
    
    /**
     * 修改交换
     * 
     * @param exchange 交换信息
     * @return 结果
     */
    public int updateExchange(MedicalExchange exchange);
    
    /**
     * 删除交换
     * 
     * @param id 交换ID
     * @return 结果
     */
    public int deleteExchangeById(Long id);
    
    /**
     * 根据四元组查询是否存在交换记录
     */
    public MedicalExchange selectExchangeByKeys(Long exchangeCardId, String exchangeCardUid, String uid, Long uidCardId);
}

