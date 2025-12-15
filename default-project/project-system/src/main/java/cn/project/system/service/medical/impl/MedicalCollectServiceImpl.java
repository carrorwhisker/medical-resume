package cn.project.system.service.medical.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.project.system.domain.medical.MedicalCollect;
import cn.project.system.mapper.medical.MedicalCollectMapper;
import cn.project.system.service.medical.IMedicalCollectService;

/**
 * 医简历收藏Service业务层处理
 * 
 * @author medical
 */
@Service
public class MedicalCollectServiceImpl implements IMedicalCollectService
{
    @Autowired
    private MedicalCollectMapper collectMapper;

    @Override
    public List<MedicalCollect> selectCollectList(MedicalCollect collect)
    {
        return collectMapper.selectCollectList(collect);
    }

    @Override
    public MedicalCollect selectCollectByUidAndCardId(String uid, Long cardId)
    {
        return collectMapper.selectCollectByUidAndCardId(uid, cardId);
    }

    @Override
    public int insertCollect(MedicalCollect collect)
    {
        return collectMapper.insertCollect(collect);
    }

    @Override
    public int deleteCollectById(Long id)
    {
        return collectMapper.deleteCollectById(id);
    }

    @Override
    public int deleteCollectByUidAndCardId(String uid, Long cardId)
    {
        return collectMapper.deleteCollectByUidAndCardId(uid, cardId);
    }
    
    @Override
    public List<Map<String, Object>> selectCollectCardList(String uid, String position, String city, String title)
    {
        return collectMapper.selectCollectCardList(uid, position, city, title);
    }
}

