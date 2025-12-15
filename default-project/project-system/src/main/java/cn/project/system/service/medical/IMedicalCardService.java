package cn.project.system.service.medical;

import java.util.List;
import cn.project.system.domain.medical.MedicalCard;

/**
 * 医简历卡片Service接口
 * 
 * @author medical
 */
public interface IMedicalCardService
{
    /**
     * 查询医简历卡片列表
     * 
     * @param card 医简历卡片
     * @return 医简历卡片集合
     */
    public List<MedicalCard> selectCardList(MedicalCard card);
    
    /**
     * 根据ID查询医简历卡片
     * 
     * @param id 卡片ID
     * @return 医简历卡片
     */
    public MedicalCard selectCardById(Long id);
    
    /**
     * 新增医简历卡片
     * 
     * @param card 医简历卡片
     * @return 结果
     */
    public int insertCard(MedicalCard card);
    
    /**
     * 修改医简历卡片
     * 
     * @param card 医简历卡片
     * @return 结果
     */
    public int updateCard(MedicalCard card);
    
    /**
     * 删除医简历卡片
     * 
     * @param id 卡片ID
     * @return 结果
     */
    public int deleteCardById(Long id);
    
    /**
     * 根据UID统计卡片数量
     * 
     * @param uid 用户UID
     * @return 数量
     */
    public int countByUid(String uid);
    
    /**
     * 浏览次数 +1
     * 
     * @param id 卡片ID
     * @return 结果
     */
    public int incrementViewNum(Long id);
}
