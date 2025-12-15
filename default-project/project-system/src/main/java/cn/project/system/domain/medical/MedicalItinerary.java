package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 行程管理实体类
 * 
 * @author medical
 */
public class MedicalItinerary extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 行程ID */
    private Long id;
    
    /** 报名ID */
    private Long signUpId;
    
    /** 会议ID */
    private Long meetingId;
    
    /** 用户UID */
    private String uid;
    
    /** 接待地点 */
    private String receptionLocation;
    
    /** 接待专家 */
    private String receptionExpert;
    
    /** 接待专家电话 */
    private String receptionExpertPhone;
    
    /** 行程备注 */
    private String notes;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;
    
    /** 更新时间（时间戳） */
    private Long updateTimeStamp;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getSignUpId()
    {
        return signUpId;
    }

    public void setSignUpId(Long signUpId)
    {
        this.signUpId = signUpId;
    }

    public Long getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(Long meetingId)
    {
        this.meetingId = meetingId;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getReceptionLocation()
    {
        return receptionLocation;
    }

    public void setReceptionLocation(String receptionLocation)
    {
        this.receptionLocation = receptionLocation;
    }

    public String getReceptionExpert()
    {
        return receptionExpert;
    }

    public void setReceptionExpert(String receptionExpert)
    {
        this.receptionExpert = receptionExpert;
    }

    public String getReceptionExpertPhone()
    {
        return receptionExpertPhone;
    }

    public void setReceptionExpertPhone(String receptionExpertPhone)
    {
        this.receptionExpertPhone = receptionExpertPhone;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public Long getCreateTimeStamp()
    {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Long createTimeStamp)
    {
        this.createTimeStamp = createTimeStamp;
    }

    public Long getUpdateTimeStamp()
    {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Long updateTimeStamp)
    {
        this.updateTimeStamp = updateTimeStamp;
    }
}

