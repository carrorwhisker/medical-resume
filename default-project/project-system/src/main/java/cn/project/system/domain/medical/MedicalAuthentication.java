package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历认证实体类
 * 
 * @author medical
 */
public class MedicalAuthentication extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 认证ID */
    private Long id;
    
    /** 姓名 */
    private String name;
    
    /** 证件号 */
    private String num;
    
    /** 认证正面照 */
    private String authenticationStraight;
    
    /** 认证侧面照 */
    private String authenticationSide;
    
    /** 创建时间（时间戳） */
    private Long createTimeStamp;
    
    /** 更新时间（时间戳） */
    private Long updateTimeStamp;
    
    /** 用户UID */
    private String uid;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNum()
    {
        return num;
    }

    public void setNum(String num)
    {
        this.num = num;
    }

    public String getAuthenticationStraight()
    {
        return authenticationStraight;
    }

    public void setAuthenticationStraight(String authenticationStraight)
    {
        this.authenticationStraight = authenticationStraight;
    }

    public String getAuthenticationSide()
    {
        return authenticationSide;
    }

    public void setAuthenticationSide(String authenticationSide)
    {
        this.authenticationSide = authenticationSide;
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

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }
}

