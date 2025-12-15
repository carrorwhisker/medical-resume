package cn.project.system.mapper.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalExchange;
import org.apache.ibatis.annotations.Param;

/**
 * 医简历交换Mapper接口
 * 
 * @author medical
 */
public interface MedicalExchangeMapper
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
     * 根据四元组查询是否已存在交换记录
     */
    public MedicalExchange selectExchangeByKeys(@Param("exchangeCardId") Long exchangeCardId,
                                               @Param("exchangeCardUid") String exchangeCardUid,
                                               @Param("uid") String uid,
                                               @Param("uidCardId") Long uidCardId);
}

