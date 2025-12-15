package cn.project.system.domain.medical;

import java.io.Serializable;
import cn.project.common.core.domain.BaseEntity;

/**
 * 交通信息实体类
 * 
 * @author medical
 */
public class MedicalTransportation extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    /** 交通信息ID */
    private Long id;
    
    /** 行程ID */
    private Long itineraryId;
    
    /** 交通类型 1飞机 2火车 3高铁 4汽车 5其他 */
    private Integer transportType;
    
    /** 出发地 */
    private String departureLocation;
    
    /** 到达地 */
    private String arrivalLocation;
    
    /** 出发时间（时间戳） */
    private Long departureTime;
    
    /** 到达时间（时间戳） */
    private Long arrivalTime;
    
    /** 车次/航班号 */
    private String transportNumber;
    
    /** 座位信息 */
    private String seatInfo;
    
    /** 票据图片 */
    private String ticketImage;
    
    /** 备注 */
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

    public Long getItineraryId()
    {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId)
    {
        this.itineraryId = itineraryId;
    }

    public Integer getTransportType()
    {
        return transportType;
    }

    public void setTransportType(Integer transportType)
    {
        this.transportType = transportType;
    }

    public String getDepartureLocation()
    {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation)
    {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation()
    {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation)
    {
        this.arrivalLocation = arrivalLocation;
    }

    public Long getDepartureTime()
    {
        return departureTime;
    }

    public void setDepartureTime(Long departureTime)
    {
        this.departureTime = departureTime;
    }

    public Long getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(Long arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public String getTransportNumber()
    {
        return transportNumber;
    }

    public void setTransportNumber(String transportNumber)
    {
        this.transportNumber = transportNumber;
    }

    public String getSeatInfo()
    {
        return seatInfo;
    }

    public void setSeatInfo(String seatInfo)
    {
        this.seatInfo = seatInfo;
    }

    public String getTicketImage()
    {
        return ticketImage;
    }

    public void setTicketImage(String ticketImage)
    {
        this.ticketImage = ticketImage;
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

