package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历报名实体类
 * 
 * @author medical
 */
public class MedicalSignUp extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 报名ID */
    private Long id;
    
    /** 报名者名称 */
    private String signUpName;
    
    /** 报名者手机号 */
    private String signUpPhone;
    
    /** 报名者公司 */
    private String signUpCompany;
    
    /** 报名者职位 */
    private String signUpPosition;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;
    
    /** 更新时间（时间戳） */
    private Long updateTimeStamp;
    
    /** 其他信息 */
    private String extContent;
    
    /** 报名者UID */
    private String uid;
    
    /** 会议ID */
    private Long meetingId;
    
    /** 状态 1待审核 2审核通过 3已拒绝 */
    private Integer status;
    
    /** 行程信息 */
    private String tripContent;
    
    /** 管理员反馈 */
    private String remark;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getSignUpName()
    {
        return signUpName;
    }

    public void setSignUpName(String signUpName)
    {
        this.signUpName = signUpName;
    }

    public String getSignUpPhone()
    {
        return signUpPhone;
    }

    public void setSignUpPhone(String signUpPhone)
    {
        this.signUpPhone = signUpPhone;
    }

    public String getSignUpCompany()
    {
        return signUpCompany;
    }

    public void setSignUpCompany(String signUpCompany)
    {
        this.signUpCompany = signUpCompany;
    }

    public String getSignUpPosition()
    {
        return signUpPosition;
    }

    public void setSignUpPosition(String signUpPosition)
    {
        this.signUpPosition = signUpPosition;
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

    public String getExtContent()
    {
        return extContent;
    }

    public void setExtContent(String extContent)
    {
        this.extContent = extContent;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public Long getMeetingId()
    {
        return meetingId;
    }

    public void setMeetingId(Long meetingId)
    {
        this.meetingId = meetingId;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getTripContent()
    {
        return tripContent;
    }

    public void setTripContent(String tripContent)
    {
        this.tripContent = tripContent;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
}

