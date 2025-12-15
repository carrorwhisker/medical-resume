package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历收藏实体类
 * 
 * @author medical
 */
public class MedicalCollect extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 收藏ID */
    private Long id;
    
    /** 用户UID */
    private String uid;
    
    /** 卡片ID */
    private Long cardId;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public Long getCardId()
    {
        return cardId;
    }

    public void setCardId(Long cardId)
    {
        this.cardId = cardId;
    }

    public Long getCreateTimeStamp()
    {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp)
    {
        this.createTimeStamp = createTimeStamp;
    }
}

