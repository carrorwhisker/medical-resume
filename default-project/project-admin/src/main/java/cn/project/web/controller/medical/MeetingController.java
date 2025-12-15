package cn.project.web.controller.medical;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageHelper;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.common.utils.TokenUtils;
import cn.project.common.core.text.Convert;
import cn.project.common.annotation.Log;
import cn.project.common.enums.BusinessType;
import cn.project.system.domain.medical.MedicalMeeting;
import cn.project.system.service.medical.IMedicalMeetingService;
import cn.project.system.domain.medical.MedicalSignUp;
import cn.project.system.service.medical.IMedicalSignUpService;

/**
 * 医简历会议控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Meeting/Meeting")
public class MeetingController extends BaseController
{
    @Autowired
    private IMedicalMeetingService meetingService;
    @Autowired
    private IMedicalSignUpService signUpService;
    
    /**
     * 获取会议列表
     */
    @PostMapping("/getMeetingList")
    public TableDataInfo getMeetingList(@RequestBody Map<String, Object> params)
    {
        String city = Convert.toStr(params.get("city"));
        String title = Convert.toStr(params.get("title"));
        Integer size = (Integer) params.getOrDefault("size", 20);
        Integer page = (Integer) params.getOrDefault("page", 1);
        Integer orderBy = (Integer) params.getOrDefault("order", 1);
        
        startPage();
        if (orderBy != null && orderBy == 2)
        {
            PageHelper.orderBy("start_time desc");
        }
        else if (orderBy != null && orderBy == 3)
        {
            PageHelper.orderBy("create_time desc");
        }
        
        MedicalMeeting query = new MedicalMeeting();
        if (StringUtils.isNotEmpty(city))
        {
            query.setQyName(city);
        }
        if (StringUtils.isNotEmpty(title))
        {
            query.setMeetingName(title);
        }
        
        List<MedicalMeeting> list = meetingService.selectMeetingList(query);
        return getDataTable(list);
    }
    
    /**
     * 获取会议详情
     */
    @PostMapping("/getMeetingDetail")
    public AjaxResult getMeetingDetail(@RequestHeader(value = "Token", required = false) String token,
                                     @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录");
        }
        
        String id = (String) params.get("id");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("参数错误");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        Long meetingId = Convert.toLong(id, null);
        if (meetingId == null)
        {
            return AjaxResult.error("参数错误");
        }
        
        MedicalMeeting meeting = meetingService.selectMeetingById(meetingId);
        if (meeting == null)
        {
            return AjaxResult.error("暂无数据");
        }
        
        // 查询用户报名状态
        MedicalSignUp signUp = signUpService.selectSignUpByUidAndMeetingId(uid, meetingId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("id", meeting.getId());
        result.put("meeting_name", meeting.getMeetingName());
        result.put("meeting_img", meeting.getMeetingImg());
        result.put("gw_banner", meeting.getGwBanner());
        result.put("gw_url", meeting.getGwUrl());
        result.put("qy_id", meeting.getQyId());
        result.put("qy_name", meeting.getQyName());
        result.put("is_price_status", meeting.getIsPriceStatus());
        result.put("price", meeting.getPrice());
        result.put("ext_content", meeting.getExtContent());
        result.put("create_time", meeting.getCreateTimeStamp());
        
        // 判断是否已报名且审核通过
        boolean isApproved = signUp != null && signUp.getStatus() == 2;
        
        if (isApproved) {
            // 已报名且审核通过，显示完整信息
            result.put("meeting_content", meeting.getMeetingContent());
            result.put("meeting_address", meeting.getMeetingAddress());
            result.put("start_time", meeting.getStartTime());
            result.put("end_time", meeting.getEndTime());
            result.put("is_approved", true);
            result.put("sign_up_id", signUp.getId());
            result.put("sign_up_status", signUp.getStatus());
        } else {
            // 未报名或未通过审核，脱敏处理
            result.put("meeting_content", "报名审核通过后可查看详细内容");
            result.put("meeting_address", "报名审核通过后可查看");
            result.put("start_time", null);
            result.put("end_time", null);
            result.put("is_approved", false);
            if (signUp != null) {
                result.put("sign_up_id", signUp.getId());
                result.put("sign_up_status", signUp.getStatus());
            } else {
                result.put("sign_up_id", null);
                result.put("sign_up_status", 0); // 0表示未报名
            }
        }
        
        return AjaxResult.success("success", result);
    }
    
    /**
     * 会议报名
     */
    @PostMapping("/getMeetingSignUp")
    public AjaxResult getMeetingSignUp(@RequestHeader(value = "Token", required = false) String token,
                                     @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        String name = Convert.toStr(params.get("sign_up_name"));
        String phone = Convert.toStr(params.get("sign_up_phone"));
        String company = Convert.toStr(params.get("sign_up_company"));
        String position = Convert.toStr(params.get("sign_up_position"));
        String extContent = Convert.toStr(params.get("ext_content"));
        String meetingId = Convert.toStr(params.get("meeting_id"));
        
        if (StringUtils.isEmpty(meetingId) || StringUtils.isEmpty(name) || 
            StringUtils.isEmpty(phone) || StringUtils.isEmpty(company) || StringUtils.isEmpty(position))
        {
            return AjaxResult.error("参数错误");
        }
        
        Long mId = Convert.toLong(meetingId, null);
        if (mId == null)
        {
            return AjaxResult.error("参数错误");
        }
        
        // 检查是否已报名
        MedicalSignUp exists = signUpService.selectSignUpByUidAndMeetingId(uid, mId);
        if (exists != null)
        {
            return AjaxResult.error("已报名");
        }
        
        MedicalSignUp sign = new MedicalSignUp();
        sign.setUid(uid);
        sign.setMeetingId(mId);
        sign.setSignUpName(name);
        sign.setSignUpPhone(phone);
        sign.setSignUpCompany(company);
        sign.setSignUpPosition(position);
        sign.setExtContent(extContent);
        sign.setStatus(1); // 待审核
        sign.setCreateTimeStamp(System.currentTimeMillis() / 1000);
        
        int rows = signUpService.insertSignUp(sign);
        if (rows > 0)
        {
            return AjaxResult.success("报名成功");
        }
        return AjaxResult.error("报名失败");
    }
    
    /**
     * 获取报名信息
     */
    @PostMapping("/getMeetingSignUpInfo")
    public AjaxResult getMeetingSignUpInfo(@RequestHeader(value = "Token", required = false) String token,
                                         @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录");
        }
        
        Long activeId = Convert.toLong(params.get("active_id"), null);
        if (activeId == null)
        {
            return AjaxResult.error("参数错误");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        MedicalSignUp sign = signUpService.selectSignUpById(activeId);
        if (sign == null || !uid.equals(sign.getUid()))
        {
            return AjaxResult.error("暂无数据");
        }
        
        Map<String, Object> signInfo = new HashMap<>();
        signInfo.put("id", sign.getId());
        signInfo.put("meeting_id", sign.getMeetingId());
        signInfo.put("sign_up_name", sign.getSignUpName());
        signInfo.put("sign_up_phone", sign.getSignUpPhone());
        signInfo.put("sign_up_company", sign.getSignUpCompany());
        signInfo.put("sign_up_position", sign.getSignUpPosition());
        signInfo.put("ext_content", sign.getExtContent());
        signInfo.put("trip_content", sign.getTripContent());
        signInfo.put("remark", sign.getRemark());
        signInfo.put("status", sign.getStatus());
        signInfo.put("status_text", mapStatus(sign.getStatus()));
        signInfo.put("create_time", sign.getCreateTimeStamp());
        signInfo.put("update_time", sign.getUpdateTimeStamp());
        
        Map<String, Object> meetingInfo = new HashMap<>();
        if (sign.getMeetingId() != null)
        {
            MedicalMeeting meeting = meetingService.selectMeetingById(sign.getMeetingId());
            if (meeting != null)
            {
                meetingInfo.put("id", meeting.getId());
                meetingInfo.put("meeting_name", meeting.getMeetingName());
                meetingInfo.put("meeting_img", meeting.getMeetingImg());
                meetingInfo.put("start_time", meeting.getStartTime());
                meetingInfo.put("end_time", meeting.getEndTime());
                meetingInfo.put("qy_name", meeting.getQyName());
                meetingInfo.put("qy_id", meeting.getQyId());
                meetingInfo.put("meeting_address", meeting.getMeetingAddress());
                meetingInfo.put("meeting_content", meeting.getMeetingContent());
                meetingInfo.put("is_price_status", meeting.getIsPriceStatus());
                meetingInfo.put("price", meeting.getPrice());
                meetingInfo.put("gw_banner", meeting.getGwBanner());
                meetingInfo.put("gw_url", meeting.getGwUrl());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("sign_up_info", signInfo);
        result.put("meeting_info", meetingInfo);
        
        return AjaxResult.success("success", result);
    }
    
    /**
     * 提交行程信息
     */
    @PostMapping("/getMeetingSignUpTrip")
    public AjaxResult getMeetingSignUpTrip(@RequestHeader(value = "Token", required = false) String token,
                                         @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录");
        }
        
        String tripContent = (String) params.get("trip_content");
        String activeId = (String) params.get("active_id");
        
        if (StringUtils.isEmpty(tripContent))
        {
            return AjaxResult.error("参数错误");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        // TODO: 实现提交行程信息逻辑
        
        return AjaxResult.success("提交成功");
    }
    
    /**
     * 获取用户会议列表
     */
    @PostMapping("/getUserListMeeting")
    public TableDataInfo getUserListMeeting(@RequestHeader(value = "Token", required = false) String token,
                                          @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return getDataTable(new ArrayList<>());
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        Integer size = (Integer) params.getOrDefault("size", 20);
        Integer page = (Integer) params.getOrDefault("page", 1);
        // 前端传0表示全部，这里将0转为null表示不过滤
        Integer status = Convert.toInt(params.get("status"), null);
        if (status != null && status == 0)
        {
            status = null;
        }
        
        startPage();
        
        MedicalSignUp query = new MedicalSignUp();
        query.setUid(uid);
        if (status != null)
        {
            query.setStatus(status);
        }
        List<MedicalSignUp> signList = signUpService.selectSignUpList(query);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (MedicalSignUp s : signList)
        {
            Map<String, Object> item = new HashMap<>();
            item.put("id", s.getId());
            item.put("meeting_id", s.getMeetingId());
            item.put("status", s.getStatus());
            item.put("status_text", mapStatus(s.getStatus()));
            item.put("sign_up_name", s.getSignUpName());
            item.put("sign_up_phone", s.getSignUpPhone());
            item.put("sign_up_company", s.getSignUpCompany());
            item.put("sign_up_position", s.getSignUpPosition());
            item.put("create_time", s.getCreateTimeStamp());
            item.put("trip_content", s.getTripContent());
            item.put("remark", s.getRemark());
            
            if (s.getMeetingId() != null)
            {
                MedicalMeeting meeting = meetingService.selectMeetingById(s.getMeetingId());
                if (meeting != null)
                {
                    Map<String, Object> m = new HashMap<>();
                    m.put("meeting_name", meeting.getMeetingName());
                    m.put("meeting_img", meeting.getMeetingImg());
                    m.put("start_time", meeting.getStartTime());
                    m.put("end_time", meeting.getEndTime());
                    m.put("qy_name", meeting.getQyName());
                    m.put("meeting_address", meeting.getMeetingAddress());
                    m.put("is_price_status", meeting.getIsPriceStatus());
                    m.put("price", meeting.getPrice());
                    item.put("meeting", m);
                    // 兼容前端直接使用平铺字段
                    item.put("meeting_name", meeting.getMeetingName());
                    item.put("meeting_img", meeting.getMeetingImg());
                    item.put("start_time", meeting.getStartTime());
                    item.put("end_time", meeting.getEndTime());
                    item.put("qy_name", meeting.getQyName());
                    item.put("meeting_address", meeting.getMeetingAddress());
                    item.put("is_price_status", meeting.getIsPriceStatus());
                    item.put("price", meeting.getPrice());
                }
            }
            result.add(item);
        }
        
        return getDataTable(result);
    }
    
    /**
     * 取消会议报名
     */
    @PostMapping("/cancelMeetingData")
    public AjaxResult cancelMeetingData(@RequestHeader(value = "Token", required = false) String token,
                                      @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录");
        }
        
        String id = (String) params.get("active_id");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("参数错误");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        // TODO: 实现取消会议报名逻辑
        // 更新状态为3（已取消），备注为"用户自行取消"
        
        return AjaxResult.success("取消成功");
    }
    
    /**
     * 状态映射
     */
    private String mapStatus(Integer status)
    {
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(1, "待审核");
        statusMap.put(2, "待参加");
        statusMap.put(3, "已拒绝");
        
        return statusMap.getOrDefault(status, "");
    }
}
