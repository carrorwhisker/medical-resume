package cn.project.web.controller.medical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.core.domain.entity.SysUser;
import cn.project.system.domain.medical.MedicalItinerary;
import cn.project.system.domain.medical.MedicalTransportation;
import cn.project.system.domain.medical.MedicalSignUp;
import cn.project.system.domain.medical.MedicalMeeting;
import cn.project.system.service.medical.IMedicalItineraryService;
import cn.project.system.service.medical.IMedicalTransportationService;
import cn.project.system.service.medical.IMedicalSignUpService;
import cn.project.system.service.medical.IMedicalMeetingService;
import cn.project.common.core.text.Convert;
import cn.project.common.utils.DateUtils;

/**
 * 管理端行程管理Controller
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Admin/AdminItinerary")
public class AdminItineraryController extends BaseController
{
    @Autowired
    private IMedicalItineraryService itineraryService;

    @Autowired
    private IMedicalTransportationService transportationService;

    @Autowired
    private IMedicalSignUpService signUpService;

    @Autowired
    private IMedicalMeetingService meetingService;

    /**
     * 查询行程列表
     */
    @PostMapping("/getItineraryList")
    public AjaxResult getItineraryList(@RequestBody Map<String, Object> params)
    {
        try {
            Integer page = Convert.toInt(params.get("page"), 1);
            Integer size = Convert.toInt(params.get("size"), 10);
            
            startPage();
            MedicalItinerary query = new MedicalItinerary();
            
            // 可以根据会议ID或报名ID查询
            if (params.get("meeting_id") != null) {
                query.setMeetingId(Convert.toLong(params.get("meeting_id")));
            }
            if (params.get("sign_up_id") != null) {
                query.setSignUpId(Convert.toLong(params.get("sign_up_id")));
            }
            
            List<MedicalItinerary> list = itineraryService.selectItineraryList(query);
            TableDataInfo dataTable = getDataTable(list);
            
            // 组装返回数据，包含报名人信息和会议信息
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (MedicalItinerary itinerary : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", itinerary.getId());
                map.put("sign_up_id", itinerary.getSignUpId());
                map.put("meeting_id", itinerary.getMeetingId());
                map.put("uid", itinerary.getUid());
                map.put("reception_location", itinerary.getReceptionLocation());
                map.put("reception_expert", itinerary.getReceptionExpert());
                map.put("reception_expert_phone", itinerary.getReceptionExpertPhone());
                map.put("notes", itinerary.getNotes());
                map.put("create_time", formatTimestamp(itinerary.getCreateTimeStamp()));
                map.put("update_time", formatTimestamp(itinerary.getUpdateTimeStamp()));
                
                // 获取报名信息
                MedicalSignUp signUp = signUpService.selectSignUpById(itinerary.getSignUpId());
                if (signUp != null) {
                    map.put("sign_up_name", signUp.getSignUpName());
                    map.put("sign_up_phone", signUp.getSignUpPhone());
                    map.put("sign_up_company", signUp.getSignUpCompany());
                }
                
                // 获取会议信息
                MedicalMeeting meeting = meetingService.selectMeetingById(itinerary.getMeetingId());
                if (meeting != null) {
                    map.put("meeting_name", meeting.getMeetingName());
                    map.put("meeting_start_time", formatTimestamp(meeting.getStartTime()));
                    map.put("meeting_end_time", formatTimestamp(meeting.getEndTime()));
                }
                
                // 获取交通信息列表
                List<MedicalTransportation> transportations = transportationService.selectTransportationByItineraryId(itinerary.getId());
                List<Map<String, Object>> transportList = new ArrayList<>();
                for (MedicalTransportation trans : transportations) {
                    Map<String, Object> transMap = new HashMap<>();
                    transMap.put("id", trans.getId());
                    transMap.put("transport_type", trans.getTransportType());
                    transMap.put("departure_location", trans.getDepartureLocation());
                    transMap.put("arrival_location", trans.getArrivalLocation());
                    transMap.put("departure_time", formatTimestamp(trans.getDepartureTime()));
                    transMap.put("arrival_time", formatTimestamp(trans.getArrivalTime()));
                    transMap.put("transport_number", trans.getTransportNumber());
                    transMap.put("seat_info", trans.getSeatInfo());
                    transMap.put("ticket_image", trans.getTicketImage());
                    transMap.put("notes", trans.getNotes());
                    transportList.add(transMap);
                }
                map.put("transportations", transportList);
                
                resultList.add(map);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("rows", resultList);
            result.put("total", dataTable.getTotal());
            
            return AjaxResult.success("查询成功", result);
        } catch (Exception e) {
            logger.error("查询行程列表失败", e);
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据报名ID获取行程详情
     */
    @GetMapping("/getItineraryBySignUpId")
    public AjaxResult getItineraryBySignUpId(@RequestParam Long signUpId)
    {
        try {
            MedicalItinerary itinerary = itineraryService.selectItineraryBySignUpId(signUpId);
            if (itinerary == null) {
                return AjaxResult.success("暂无行程信息", null);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", itinerary.getId());
            result.put("sign_up_id", itinerary.getSignUpId());
            result.put("meeting_id", itinerary.getMeetingId());
            result.put("uid", itinerary.getUid());
            result.put("reception_location", itinerary.getReceptionLocation());
            result.put("reception_expert", itinerary.getReceptionExpert());
            result.put("reception_expert_phone", itinerary.getReceptionExpertPhone());
            result.put("notes", itinerary.getNotes());
            
            // 获取交通信息列表
            List<MedicalTransportation> transportations = transportationService.selectTransportationByItineraryId(itinerary.getId());
            List<Map<String, Object>> transportList = new ArrayList<>();
            for (MedicalTransportation trans : transportations) {
                Map<String, Object> transMap = new HashMap<>();
                transMap.put("id", trans.getId());
                transMap.put("transport_type", trans.getTransportType());
                transMap.put("departure_location", trans.getDepartureLocation());
                transMap.put("arrival_location", trans.getArrivalLocation());
                transMap.put("departure_time", trans.getDepartureTime());
                transMap.put("arrival_time", trans.getArrivalTime());
                transMap.put("transport_number", trans.getTransportNumber());
                transMap.put("seat_info", trans.getSeatInfo());
                transMap.put("ticket_image", trans.getTicketImage());
                transMap.put("notes", trans.getNotes());
                transportList.add(transMap);
            }
            result.put("transportations", transportList);
            
            return AjaxResult.success("查询成功", result);
        } catch (Exception e) {
            logger.error("查询行程详情失败", e);
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增或更新行程
     */
    @PostMapping("/saveItinerary")
    public AjaxResult saveItinerary(@RequestBody Map<String, Object> params)
    {
        try {
            Long id = params.get("id") != null ? Convert.toLong(params.get("id")) : null;
            Long signUpId = Convert.toLong(params.get("sign_up_id"));
            
            if (signUpId == null) {
                return AjaxResult.error("报名ID不能为空");
            }
            
            // 获取报名信息
            MedicalSignUp signUp = signUpService.selectSignUpById(signUpId);
            if (signUp == null) {
                return AjaxResult.error("报名信息不存在");
            }
            
            MedicalItinerary itinerary;
            if (id != null) {
                // 更新
                itinerary = itineraryService.selectItineraryById(id);
                if (itinerary == null) {
                    return AjaxResult.error("行程不存在");
                }
            } else {
                // 新增
                itinerary = new MedicalItinerary();
                itinerary.setSignUpId(signUpId);
                itinerary.setMeetingId(signUp.getMeetingId());
                itinerary.setUid(signUp.getUid());
            }
            
            itinerary.setReceptionLocation(Convert.toStr(params.get("reception_location")));
            itinerary.setReceptionExpert(Convert.toStr(params.get("reception_expert")));
            itinerary.setReceptionExpertPhone(Convert.toStr(params.get("reception_expert_phone")));
            itinerary.setNotes(Convert.toStr(params.get("notes")));
            
            if (id != null) {
                itineraryService.updateItinerary(itinerary);
            } else {
                itineraryService.insertItinerary(itinerary);
            }
            
            return AjaxResult.success("保存成功");
        } catch (Exception e) {
            logger.error("保存行程失败", e);
            return AjaxResult.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除行程
     */
    @PostMapping("/deleteItinerary")
    public AjaxResult deleteItinerary(@RequestBody Map<String, Object> params)
    {
        try {
            Long id = Convert.toLong(params.get("id"));
            if (id == null) {
                return AjaxResult.error("行程ID不能为空");
            }
            
            // 先删除关联的交通信息
            transportationService.deleteTransportationByItineraryId(id);
            // 再删除行程
            itineraryService.deleteItineraryById(id);
            
            return AjaxResult.success("删除成功");
        } catch (Exception e) {
            logger.error("删除行程失败", e);
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 新增交通信息
     */
    @PostMapping("/addTransportation")
    public AjaxResult addTransportation(@RequestBody Map<String, Object> params)
    {
        try {
            Long itineraryId = Convert.toLong(params.get("itinerary_id"));
            if (itineraryId == null) {
                return AjaxResult.error("行程ID不能为空");
            }
            
            MedicalTransportation transportation = new MedicalTransportation();
            transportation.setItineraryId(itineraryId);
            transportation.setTransportType(Convert.toInt(params.get("transport_type")));
            transportation.setDepartureLocation(Convert.toStr(params.get("departure_location")));
            transportation.setArrivalLocation(Convert.toStr(params.get("arrival_location")));
            transportation.setDepartureTime(Convert.toLong(params.get("departure_time")));
            transportation.setArrivalTime(Convert.toLong(params.get("arrival_time")));
            transportation.setTransportNumber(Convert.toStr(params.get("transport_number")));
            transportation.setSeatInfo(Convert.toStr(params.get("seat_info")));
            transportation.setTicketImage(Convert.toStr(params.get("ticket_image")));
            transportation.setNotes(Convert.toStr(params.get("notes")));
            
            transportationService.insertTransportation(transportation);
            
            return AjaxResult.success("添加成功");
        } catch (Exception e) {
            logger.error("添加交通信息失败", e);
            return AjaxResult.error("添加失败: " + e.getMessage());
        }
    }

    /**
     * 更新交通信息
     */
    @PostMapping("/updateTransportation")
    public AjaxResult updateTransportation(@RequestBody Map<String, Object> params)
    {
        try {
            Long id = Convert.toLong(params.get("id"));
            if (id == null) {
                return AjaxResult.error("交通信息ID不能为空");
            }
            
            MedicalTransportation transportation = transportationService.selectTransportationById(id);
            if (transportation == null) {
                return AjaxResult.error("交通信息不存在");
            }
            
            transportation.setTransportType(Convert.toInt(params.get("transport_type")));
            transportation.setDepartureLocation(Convert.toStr(params.get("departure_location")));
            transportation.setArrivalLocation(Convert.toStr(params.get("arrival_location")));
            transportation.setDepartureTime(Convert.toLong(params.get("departure_time")));
            transportation.setArrivalTime(Convert.toLong(params.get("arrival_time")));
            transportation.setTransportNumber(Convert.toStr(params.get("transport_number")));
            transportation.setSeatInfo(Convert.toStr(params.get("seat_info")));
            transportation.setTicketImage(Convert.toStr(params.get("ticket_image")));
            transportation.setNotes(Convert.toStr(params.get("notes")));
            
            transportationService.updateTransportation(transportation);
            
            return AjaxResult.success("更新成功");
        } catch (Exception e) {
            logger.error("更新交通信息失败", e);
            return AjaxResult.error("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除交通信息
     */
    @PostMapping("/deleteTransportation")
    public AjaxResult deleteTransportation(@RequestBody Map<String, Object> params)
    {
        try {
            Long id = Convert.toLong(params.get("id"));
            if (id == null) {
                return AjaxResult.error("交通信息ID不能为空");
            }
            
            transportationService.deleteTransportationById(id);
            
            return AjaxResult.success("删除成功");
        } catch (Exception e) {
            logger.error("删除交通信息失败", e);
            return AjaxResult.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 时间戳(秒)转字符串
     */
    private String formatTimestamp(Long ts) {
        if (ts == null) {
            return null;
        }
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, new Date(ts * 1000));
    }
}

