package cn.project.system.service.medical.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalExchange;
import cn.project.system.mapper.medical.MedicalExchangeMapper;
import cn.project.system.service.medical.IMedicalExchangeService;

/**
 * 医简历交换Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalExchangeServiceImpl implements IMedicalExchangeService
{
    @Autowired
    private MedicalExchangeMapper exchangeMapper;

    @Override
    public List<MedicalExchange> selectExchangeList(MedicalExchange exchange)
    {
        return exchangeMapper.selectExchangeList(exchange);
    }

    @Override
    public MedicalExchange selectExchangeById(Long id)
    {
        return exchangeMapper.selectExchangeById(id);
    }

    @Override
    public int insertExchange(MedicalExchange exchange)
    {
        return exchangeMapper.insertExchange(exchange);
    }

    @Override
    public int updateExchange(MedicalExchange exchange)
    {
        return exchangeMapper.updateExchange(exchange);
    }

    @Override
    public int deleteExchangeById(Long id)
    {
        return exchangeMapper.deleteExchangeById(id);
    }
    
    @Override
    public MedicalExchange selectExchangeByKeys(Long exchangeCardId, String exchangeCardUid, String uid, Long uidCardId)
    {
        return exchangeMapper.selectExchangeByKeys(exchangeCardId, exchangeCardUid, uid, uidCardId);
    }
}

