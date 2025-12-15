package cn.project.system.service.medical.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalRecharge;
import cn.project.system.mapper.medical.MedicalRechargeMapper;
import cn.project.system.service.medical.IMedicalRechargeService;

/**
 * 医简历充值订单Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalRechargeServiceImpl implements IMedicalRechargeService
{
    @Autowired
    private MedicalRechargeMapper rechargeMapper;

    @Override
    public int insertRecharge(MedicalRecharge recharge)
    {
        return rechargeMapper.insertRecharge(recharge);
    }

    @Override
    public MedicalRecharge selectRechargeByOutTradeNo(String outTradeNo)
    {
        return rechargeMapper.selectRechargeByOutTradeNo(outTradeNo);
    }

    @Override
    public int updateRecharge(MedicalRecharge recharge)
    {
        return rechargeMapper.updateRecharge(recharge);
    }
}

