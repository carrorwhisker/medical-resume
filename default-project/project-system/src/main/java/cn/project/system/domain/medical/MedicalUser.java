package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历用户实体类
 * 
 * @author medical
 */
public class MedicalUser extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 用户ID */
    private Long id;
    
    /** 微信openid */
    private String openid;
    
    /** 用户标识 */
    private String uid;
    
    /** 名称 */
    private String username;
    
    /** 手机号 */
    private String phone;
    
    /** 头像 */
    private String img;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;
    
    /** 更新时间（时间戳） */
    private Long updateTimeStamp;
    
    /** 内容 */
    private String content;
    
    /** VIP状态 1否 2是 */
    private Integer isVip;
    
    /** 微信手机号 */
    private String wechatPhone;
    
    /** VIP到期时间（时间戳） */
    private Long vipTime;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getOpenid()
    {
        return openid;
    }

    public void setOpenid(String openid)
    {
        this.openid = openid;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
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

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Integer getIsVip()
    {
        return isVip;
    }

    public void setIsVip(Integer isVip)
    {
        this.isVip = isVip;
    }

    public String getWechatPhone()
    {
        return wechatPhone;
    }

    public void setWechatPhone(String wechatPhone)
    {
        this.wechatPhone = wechatPhone;
    }

    public Long getVipTime()
    {
        return vipTime;
    }

    public void setVipTime(Long vipTime)
    {
        this.vipTime = vipTime;
    }
}

