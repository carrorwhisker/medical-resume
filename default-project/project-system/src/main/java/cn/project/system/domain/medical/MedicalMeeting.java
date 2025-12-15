package cn.project.system.domain.medical;

import java.io.Serializable;
import java.math.BigDecimal;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历会议实体类
 * 
 * @author medical
 */
public class MedicalMeeting extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 会议ID */
    private Long id;
    
    /** 会议名称 */
    private String meetingName;
    
    /** 封面图 */
    private String meetingImg;
    
    /** 会议开始时间（时间戳） */
    private Long startTime;
    
    /** 会议结束时间（时间戳） */
    private Long endTime;
    
    /** 区域名称 */
    private String qyName;
    
    /** 区域ID */
    private Integer qyId;
    
    /** 内容 */
    private String meetingContent;
    
    /** 是否付费 0免费 1付费 */
    private Integer isPriceStatus;
    
    /** 价格 */
    private BigDecimal price;
    
    /** 创建者ID */
    private String authorAdminId;
    
    /** 创建者名称 */
    private String author;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;
    
    /** 更新时间（时间戳） */
    private Long updateTimeStamp;
    
    /** 官网封面图 */
    private String gwBanner;
    
    /** 官网跳转地址 */
    private String gwUrl;
    
    /** 会议地址 */
    private String meetingAddress;
    
    /** 报名扩展字段 */
    private String extContent;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getMeetingName()
    {
        return meetingName;
    }

    public void setMeetingName(String meetingName)
    {
        this.meetingName = meetingName;
    }

    public String getMeetingImg()
    {
        return meetingImg;
    }

    public void setMeetingImg(String meetingImg)
    {
        this.meetingImg = meetingImg;
    }

    public Long getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Long startTime)
    {
        this.startTime = startTime;
    }

    public Long getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Long endTime)
    {
        this.endTime = endTime;
    }

    public String getQyName()
    {
        return qyName;
    }

    public void setQyName(String qyName)
    {
        this.qyName = qyName;
    }

    public Integer getQyId()
    {
        return qyId;
    }

    public void setQyId(Integer qyId)
    {
        this.qyId = qyId;
    }

    public String getMeetingContent()
    {
        return meetingContent;
    }

    public void setMeetingContent(String meetingContent)
    {
        this.meetingContent = meetingContent;
    }

    public Integer getIsPriceStatus()
    {
        return isPriceStatus;
    }

    public void setIsPriceStatus(Integer isPriceStatus)
    {
        this.isPriceStatus = isPriceStatus;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public String getAuthorAdminId()
    {
        return authorAdminId;
    }

    public void setAuthorAdminId(String authorAdminId)
    {
        this.authorAdminId = authorAdminId;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
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

    public String getGwBanner()
    {
        return gwBanner;
    }

    public void setGwBanner(String gwBanner)
    {
        this.gwBanner = gwBanner;
    }

    public String getGwUrl()
    {
        return gwUrl;
    }

    public void setGwUrl(String gwUrl)
    {
        this.gwUrl = gwUrl;
    }

    public String getMeetingAddress()
    {
        return meetingAddress;
    }

    public void setMeetingAddress(String meetingAddress)
    {
        this.meetingAddress = meetingAddress;
    }

    public String getExtContent()
    {
        return extContent;
    }

    public void setExtContent(String extContent)
    {
        this.extContent = extContent;
    }
}

