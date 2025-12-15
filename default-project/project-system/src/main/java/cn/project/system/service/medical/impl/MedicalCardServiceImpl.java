package cn.project.system.service.medical.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalCard;
import cn.project.system.mapper.medical.MedicalCardMapper;
import cn.project.system.service.medical.IMedicalCardService;

/**
 * 医简历卡片Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalCardServiceImpl implements IMedicalCardService
{
    @Autowired
    private MedicalCardMapper cardMapper;

    /**
     * 查询医简历卡片列表
     * 
     * @param card 医简历卡片
     * @return 医简历卡片
     */
    @Override
    public List<MedicalCard> selectCardList(MedicalCard card)
    {
        return cardMapper.selectCardList(card);
    }

    /**
     * 根据ID查询医简历卡片
     * 
     * @param id 卡片ID
     * @return 医简历卡片
     */
    @Override
    public MedicalCard selectCardById(Long id)
    {
        return cardMapper.selectCardById(id);
    }

    /**
     * 新增医简历卡片
     * 
     * @param card 医简历卡片
     * @return 结果
     */
    @Override
    public int insertCard(MedicalCard card)
    {
        return cardMapper.insertCard(card);
    }

    /**
     * 修改医简历卡片
     * 
     * @param card 医简历卡片
     * @return 结果
     */
    @Override
    public int updateCard(MedicalCard card)
    {
        return cardMapper.updateCard(card);
    }

    /**
     * 删除医简历卡片信息
     * 
     * @param id 卡片ID
     * @return 结果
     */
    @Override
    public int deleteCardById(Long id)
    {
        return cardMapper.deleteCardById(id);
    }
    
    /**
     * 根据UID统计卡片数量
     * 
     * @param uid 用户UID
     * @return 数量
     */
    @Override
    public int countByUid(String uid)
    {
        return cardMapper.countByUid(uid);
    }
    
    /**
     * 浏览次数 +1
     * 
     * @param id 卡片ID
     * @return 结果
     */
    @Override
    public int incrementViewNum(Long id)
    {
        return cardMapper.incrementViewNum(id);
    }
}
