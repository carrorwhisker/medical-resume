package cn.project.system.domain.medical;

import java.io.Serializable;
import java.util.Date;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历卡片实体类
 * 
 * @author medical
 */
public class MedicalCard extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 卡片ID */
    private Long id;
    
    /** 用户UID */
    private String uid;
    
    /** 头像 */
    private String img;
    
    /** 头像原图 */
    private String imgYt;
    
    /** 姓名 */
    private String name;
    
    /** 公司 */
    private String company;
    
    /** 职位 */
    private String position;
    
    /** 科室 */
    private Integer department;
    
    /** 电话 */
    private String phone;
    
    /** 邮箱 */
    private String email;
    
    /** 地址 */
    private String address;
    
    /** 微信 */
    private String wechat;
    
    /** 城市 */
    private Integer city;
    
    /** 内容 */
    private String content;
    
    /** 附件 */
    private String attachment;
    
    /** 附件名称 */
    private String attachmentName;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;
    
    /** 更新时间（时间戳） */
    private Long updateTimeStamp;
    
    /** VIP状态 */
    private Integer isVip;
    
    /** 审核状态 */
    private Integer isStatus;
    
    /** 浏览次数 */
    private Integer viewNum;

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

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    public String getImgYt()
    {
        return imgYt;
    }

    public void setImgYt(String imgYt)
    {
        this.imgYt = imgYt;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public Integer getDepartment()
    {
        return department;
    }

    public void setDepartment(Integer department)
    {
        this.department = department;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getWechat()
    {
        return wechat;
    }

    public void setWechat(String wechat)
    {
        this.wechat = wechat;
    }

    public Integer getCity()
    {
        return city;
    }

    public void setCity(Integer city)
    {
        this.city = city;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getAttachment()
    {
        return attachment;
    }

    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }

    public String getAttachmentName()
    {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName)
    {
        this.attachmentName = attachmentName;
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

    public Integer getIsVip()
    {
        return isVip;
    }

    public void setIsVip(Integer isVip)
    {
        this.isVip = isVip;
    }

    public Integer getIsStatus()
    {
        return isStatus;
    }

    public void setIsStatus(Integer isStatus)
    {
        this.isStatus = isStatus;
    }
    
    public Integer getViewNum()
    {
        return viewNum;
    }
    
    public void setViewNum(Integer viewNum)
    {
        this.viewNum = viewNum;
    }
}
