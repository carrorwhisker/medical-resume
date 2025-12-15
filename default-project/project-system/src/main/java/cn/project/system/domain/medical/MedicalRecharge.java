package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 医简历充值订单实体类
 * 
 * @author medical
 */
public class MedicalRecharge extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 订单ID */
    private Long id;
    
    /** 用户UID */
    private String uid;
    
    /** 充值金额（元） */
    private Double money;
    
    /** 支付金额（分） */
    private Integer payMoney;
    
    /** 微信订单号 */
    private String outTradeNo;
    
    /** 订单号 */
    private String orderNum;
    
    /** 订单状态 1待支付 2已支付 3已取消 */
    private Integer status;
    
    /** 充值类型 1月会员 2季会员 3年会员 */
    private Integer type;
    
    /** 支付状态 1充值 2会议 */
    private Integer isPayStatus;
    
    /** 活动ID（会议支付时使用） */
    private Long activeId;
    
    /** 订单时间 */
    private String orderTime;
    
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

    public Double getMoney()
    {
        return money;
    }

    public void setMoney(Double money)
    {
        this.money = money;
    }

    public Integer getPayMoney()
    {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney)
    {
        this.payMoney = payMoney;
    }

    public String getOutTradeNo()
    {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo)
    {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getIsPayStatus()
    {
        return isPayStatus;
    }

    public void setIsPayStatus(Integer isPayStatus)
    {
        this.isPayStatus = isPayStatus;
    }

    public Long getActiveId()
    {
        return activeId;
    }

    public void setActiveId(Long activeId)
    {
        this.activeId = activeId;
    }

    public String getOrderTime()
    {
        return orderTime;
    }

    public void setOrderTime(String orderTime)
    {
        this.orderTime = orderTime;
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

