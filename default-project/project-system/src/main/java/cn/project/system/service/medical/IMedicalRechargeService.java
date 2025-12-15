package cn.project.system.service.medical;

import cn.project.system.domain.medical.MedicalRecharge;

/**
 * 医简历充值订单Service接口
 * 
 * @author medical
 */
public interface IMedicalRechargeService
{
    /**
     * 新增充值订单
     * 
     * @param recharge 充值订单
     * @return 结果
     */
    public int insertRecharge(MedicalRecharge recharge);
    
    /**
     * 根据订单号查询充值订单
     * 
     * @param outTradeNo 微信订单号
     * @return 充值订单
     */
    public MedicalRecharge selectRechargeByOutTradeNo(String outTradeNo);
    
    /**
     * 更新充值订单状态
     * 
     * @param recharge 充值订单
     * @return 结果
     */
    public int updateRecharge(MedicalRecharge recharge);
}

