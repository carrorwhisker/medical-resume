package cn.project.system.service.medical;

import java.util.List;
import java.util.Map;
import cn.project.system.domain.medical.MedicalCollect;

/**
 * 医简历收藏Service接口
 * 
 * @author medical
 */
public interface IMedicalCollectService
{
    /**
     * 查询收藏列表
     * 
     * @param collect 收藏信息
     * @return 收藏集合
     */
    public List<MedicalCollect> selectCollectList(MedicalCollect collect);
    
    /**
     * 根据UID和卡片ID查询收藏
     * 
     * @param uid 用户UID
     * @param cardId 卡片ID
     * @return 收藏信息
     */
    public MedicalCollect selectCollectByUidAndCardId(String uid, Long cardId);
    
    /**
     * 新增收藏
     * 
     * @param collect 收藏信息
     * @return 结果
     */
    public int insertCollect(MedicalCollect collect);
    
    /**
     * 删除收藏
     * 
     * @param id 收藏ID
     * @return 结果
     */
    public int deleteCollectById(Long id);
    
    /**
     * 根据UID和卡片ID删除收藏
     * 
     * @param uid 用户UID
     * @param cardId 卡片ID
     * @return 结果
     */
    public int deleteCollectByUidAndCardId(String uid, Long cardId);
    
    /**
     * 查询收藏列表（联表查询卡片信息）
     * 
     * @param uid 用户UID
     * @param position 科室代码
     * @param city 城市代码
     * @param title 名称（模糊查询）
     * @return 收藏卡片列表
     */
    public List<Map<String, Object>> selectCollectCardList(String uid, String position, String city, String title);
}

