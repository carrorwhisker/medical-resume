package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历管理员实体类
 * 
 * @author medical
 */
public class MedicalAdmin extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 管理员ID */
    private Long id;
    
    /** 账户 */
    private String name;
    
    /** 密码 */
    private String pwd;
    
    /** 标识 */
    private String uid;
    
    /** 是否超管 0超管 1普通 */
    private Integer isRoot;
    
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPwd()
    {
        return pwd;
    }

    public void setPwd(String pwd)
    {
        this.pwd = pwd;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public Integer getIsRoot()
    {
        return isRoot;
    }

    public void setIsRoot(Integer isRoot)
    {
        this.isRoot = isRoot;
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

