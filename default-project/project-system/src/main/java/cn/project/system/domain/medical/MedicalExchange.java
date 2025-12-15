package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历交换实体类
 * 
 * @author medical
 */
public class MedicalExchange extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 交换ID */
    private Long id;
    
    /** 交换简历ID */
    private Long exchangeCardId;
    
    /** 交换人UID */
    private String exchangeCardUid;
    
    /** 原始简历人UID */
    private String uid;
    
    /** 原始简历ID */
    private Long uidCardId;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;
    
    /** 状态 1待处理 2已同意 */
    private Integer status;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getExchangeCardId()
    {
        return exchangeCardId;
    }

    public void setExchangeCardId(Long exchangeCardId)
    {
        this.exchangeCardId = exchangeCardId;
    }

    public String getExchangeCardUid()
    {
        return exchangeCardUid;
    }

    public void setExchangeCardUid(String exchangeCardUid)
    {
        this.exchangeCardUid = exchangeCardUid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public Long getUidCardId()
    {
        return uidCardId;
    }

    public void setUidCardId(Long uidCardId)
    {
        this.uidCardId = uidCardId;
    }

    public Long getCreateTimeStamp()
    {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp)
    {
        this.createTimeStamp = createTimeStamp;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }
}

