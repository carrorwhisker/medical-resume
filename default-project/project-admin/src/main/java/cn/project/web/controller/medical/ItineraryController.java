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
import cn.project.common.utils.StringUtils;
import cn.project.common.utils.TokenUtils;
import cn.project.system.domain.medical.MedicalItinerary;
import cn.project.system.domain.medical.MedicalTransportation;
import cn.project.system.domain.medical.MedicalMeeting;
import cn.project.system.domain.medical.MedicalSignUp;
import cn.project.system.service.medical.IMedicalItineraryService;
import cn.project.system.service.medical.IMedicalTransportationService;
import cn.project.system.service.medical.IMedicalMeetingService;
import cn.project.system.service.medical.IMedicalSignUpService;
import cn.project.common.utils.DateUtils;
import cn.project.common.core.text.Convert;

/**
 * 小程序端行程Controller
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Itinerary/Itinerary")
public class ItineraryController extends BaseController
{
    @Autowired
    private IMedicalItineraryService itineraryService;

    @Autowired
    private IMedicalTransportationService transportationService;

    @Autowired
    private IMedicalMeetingService meetingService;

    @Autowired
    private IMedicalSignUpService signUpService;

    /**
     * 获取我的行程列表
     */
    @PostMapping("/getMyItineraryList")
    public AjaxResult getMyItineraryList(@RequestHeader(value = "Token", required = false) String token)
    {
        try {
            if (StringUtils.isEmpty(token)) {
                return AjaxResult.error(201, "请重新登录");
            }
            
            // 解密token获取uid
            String uid = TokenUtils.getUidFromToken(token);
            if (StringUtils.isEmpty(uid)) {
                return AjaxResult.error(201, "请重新登录");
            }
            
            // 查询该用户的所有行程
            MedicalItinerary query = new MedicalItinerary();
            query.setUid(uid);
            List<MedicalItinerary> itineraryList = itineraryService.selectItineraryList(query);
            
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (MedicalItinerary itinerary : itineraryList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", itinerary.getId());
                map.put("meeting_id", itinerary.getMeetingId());
                map.put("reception_location", itinerary.getReceptionLocation());
                map.put("reception_expert", itinerary.getReceptionExpert());
                map.put("reception_expert_phone", itinerary.getReceptionExpertPhone());
                map.put("notes", itinerary.getNotes());
                
                // 获取会议信息
                MedicalMeeting meeting = meetingService.selectMeetingById(itinerary.getMeetingId());
                if (meeting != null) {
                    map.put("meeting_name", meeting.getMeetingName());
                    map.put("meeting_img", meeting.getMeetingImg());
                    map.put("meeting_address", meeting.getMeetingAddress());
                    map.put("start_time", meeting.getStartTime());
                    map.put("end_time", meeting.getEndTime());
                    map.put("start_time_str", formatTimestamp(meeting.getStartTime()));
                    map.put("end_time_str", formatTimestamp(meeting.getEndTime()));
                }
                
                // 获取报名信息
                MedicalSignUp signUp = signUpService.selectSignUpById(itinerary.getSignUpId());
                if (signUp != null) {
                    map.put("sign_up_id", signUp.getId());
                    map.put("status", signUp.getStatus());
                }
                
                // 获取交通信息
                List<MedicalTransportation> transportations = transportationService.selectTransportationByItineraryId(itinerary.getId());
                List<Map<String, Object>> transportList = new ArrayList<>();
                for (MedicalTransportation trans : transportations) {
                    Map<String, Object> transMap = new HashMap<>();
                    transMap.put("id", trans.getId());
                    transMap.put("transport_type", trans.getTransportType());
                    transMap.put("transport_type_name", getTransportTypeName(trans.getTransportType()));
                    transMap.put("departure_location", trans.getDepartureLocation());
                    transMap.put("arrival_location", trans.getArrivalLocation());
                    transMap.put("departure_time", trans.getDepartureTime());
                    transMap.put("arrival_time", trans.getArrivalTime());
                    transMap.put("departure_time_str", formatTimestamp(trans.getDepartureTime()));
                    transMap.put("arrival_time_str", formatTimestamp(trans.getArrivalTime()));
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
            result.put("list", resultList);
            result.put("count", resultList.size());
            
            return AjaxResult.success("查询成功", result);
        } catch (Exception e) {
            logger.error("查询行程列表失败", e);
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据会议ID获取行程详情
     */
    @PostMapping("/getItineraryByMeetingId")
    public AjaxResult getItineraryByMeetingId(@RequestHeader(value = "Token", required = false) String token,
                                              @RequestBody Map<String, Object> params)
    {
        try {
            if (StringUtils.isEmpty(token)) {
                return AjaxResult.error(201, "请重新登录");
            }
            
            // 解密token获取uid
            String uid = TokenUtils.getUidFromToken(token);
            if (StringUtils.isEmpty(uid)) {
                return AjaxResult.error(201, "请重新登录");
            }
            
            Long meetingId = Convert.toLong(params.get("meeting_id"));
            if (meetingId == null) {
                return AjaxResult.error("会议ID不能为空");
            }
            
            // 查询该用户在该会议的行程
            MedicalItinerary itinerary = itineraryService.selectItineraryByUidAndMeetingId(uid, meetingId);
            
            if (itinerary == null) {
                return AjaxResult.success("暂无行程信息", null);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("id", itinerary.getId());
            result.put("meeting_id", itinerary.getMeetingId());
            result.put("reception_location", itinerary.getReceptionLocation());
            result.put("reception_expert", itinerary.getReceptionExpert());
            result.put("reception_expert_phone", itinerary.getReceptionExpertPhone());
            result.put("notes", itinerary.getNotes());
            
            // 获取会议信息
            MedicalMeeting meeting = meetingService.selectMeetingById(itinerary.getMeetingId());
            if (meeting != null) {
                result.put("meeting_name", meeting.getMeetingName());
                result.put("meeting_img", meeting.getMeetingImg());
                result.put("meeting_address", meeting.getMeetingAddress());
                result.put("meeting_content", meeting.getMeetingContent());
                result.put("start_time", meeting.getStartTime());
                result.put("end_time", meeting.getEndTime());
                result.put("start_time_str", formatTimestamp(meeting.getStartTime()));
                result.put("end_time_str", formatTimestamp(meeting.getEndTime()));
            }
            
            // 获取交通信息
            List<MedicalTransportation> transportations = transportationService.selectTransportationByItineraryId(itinerary.getId());
            List<Map<String, Object>> transportList = new ArrayList<>();
            for (MedicalTransportation trans : transportations) {
                Map<String, Object> transMap = new HashMap<>();
                transMap.put("id", trans.getId());
                transMap.put("transport_type", trans.getTransportType());
                transMap.put("transport_type_name", getTransportTypeName(trans.getTransportType()));
                transMap.put("departure_location", trans.getDepartureLocation());
                transMap.put("arrival_location", trans.getArrivalLocation());
                transMap.put("departure_time", trans.getDepartureTime());
                transMap.put("arrival_time", trans.getArrivalTime());
                transMap.put("departure_time_str", formatTimestamp(trans.getDepartureTime()));
                transMap.put("arrival_time_str", formatTimestamp(trans.getArrivalTime()));
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
     * 获取交通类型名称
     */
    private String getTransportTypeName(Integer type) {
        if (type == null) {
            return "其他";
        }
        switch (type) {
            case 1:
                return "飞机";
            case 2:
                return "火车";
            case 3:
                return "高铁";
            case 4:
                return "汽车";
            default:
                return "其他";
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

