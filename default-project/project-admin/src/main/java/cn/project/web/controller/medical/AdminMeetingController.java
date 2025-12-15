package cn.project.web.controller.medical;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.common.core.text.Convert;
import cn.project.common.annotation.Log;
import cn.project.common.enums.BusinessType;
import org.springframework.security.access.prepost.PreAuthorize;
import cn.project.system.domain.medical.MedicalMeeting;
import cn.project.system.service.medical.IMedicalMeetingService;
import cn.project.system.service.medical.IMedicalSignUpService;
import cn.project.system.domain.medical.MedicalSignUp;
import java.util.stream.Collectors;

/**
 * 医简历管理员会议管理控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Admin/AdminMeeting")
public class AdminMeetingController extends BaseController
{
    @Autowired
    private IMedicalMeetingService meetingService;
    
    @Autowired
    private IMedicalSignUpService signUpService;
    
    /**
     * 获取管理员会议列表
     */
    @PostMapping("/getAdminMeetingList")
    @PreAuthorize("@ss.hasPermi('medical:meeting:list')")
    public TableDataInfo getAdminMeetingList(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        String meetingName = Convert.toStr(params.get("meeting_name"));
        String city = Convert.toStr(params.get("city"));
        // 修复类型转换问题：前端传的是字符串，需要转换为Integer
        Integer isPriceStatus = Convert.toInt(params.get("is_price_status"), null);
        
        startPage();
        
        // 查询所有会议（管理员可以看到所有数据）
        MedicalMeeting query = new MedicalMeeting();
        if (StringUtils.isNotEmpty(meetingName))
        {
            query.setMeetingName(meetingName);
        }
        if (StringUtils.isNotEmpty(city))
        {
            query.setQyName(city);
        }
        if (isPriceStatus != null)
        {
            query.setIsPriceStatus(isPriceStatus);
        }
        
        List<MedicalMeeting> list = meetingService.selectMeetingList(query);
        
        return getDataTable(list);
    }
    
    /**
     * 删除会议
     */
    @PostMapping("/delMeetingAdmin")
    @PreAuthorize("@ss.hasPermi('medical:meeting:remove')")
    @Log(title = "会议管理", businessType = BusinessType.DELETE)
    public AjaxResult delMeetingAdmin(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Long id = Convert.toLong(params.get("id"));
        
        if (id == null)
        {
            return AjaxResult.error("请输入id");
        }
        
        // 删除会议
        int result = meetingService.deleteMeetingById(id);
        
        // TODO: 删除相关报名记录（需要在Service层实现级联删除）
        
        return result > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }
    
    /**
     * 添加会议
     */
    @PostMapping("/addAdminMeeting")
    @PreAuthorize("@ss.hasPermi('medical:meeting:add')")
    @Log(title = "会议管理", businessType = BusinessType.INSERT)
    public AjaxResult addAdminMeeting(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        String meetingName = Convert.toStr(params.get("meeting_name"));
        String meetingImg = Convert.toStr(params.get("meeting_img"));
        String startTime = Convert.toStr(params.get("start_time"));
        String endTime = Convert.toStr(params.get("end_time"));
        Integer qyId = Convert.toInt(params.get("qy_id"));
        String meetingContent = Convert.toStr(params.get("meeting_content"));
        Integer isPriceStatus = Convert.toInt(params.get("is_price_status"));
        String priceStr = Convert.toStr(params.get("price"));
        String gwBanner = Convert.toStr(params.get("gw_banner"));
        String gwUrl = Convert.toStr(params.get("gw_url"));
        String meetingAddress = Convert.toStr(params.get("meeting_address"));
        String extContent = Convert.toStr(params.get("ext_content"));
        
        if (StringUtils.isEmpty(meetingName) || StringUtils.isEmpty(meetingImg) || 
            StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) || 
            qyId == null || StringUtils.isEmpty(meetingContent) || 
            isPriceStatus == null || StringUtils.isEmpty(gwBanner) || 
            StringUtils.isEmpty(gwUrl) || StringUtils.isEmpty(meetingAddress) || 
            StringUtils.isEmpty(extContent))
        {
            return AjaxResult.error("请输入完整信息");
        }
        
        // 构建实体
        MedicalMeeting meeting = new MedicalMeeting();
        meeting.setMeetingName(meetingName);
        meeting.setMeetingImg(meetingImg);
        meeting.setMeetingAddress(meetingAddress);
        meeting.setMeetingContent(meetingContent);
        meeting.setQyId(qyId);
        meeting.setQyName(Convert.toStr(params.get("qy_name")));
        meeting.setIsPriceStatus(isPriceStatus);
        meeting.setPrice(parseBigDecimal(priceStr));
        meeting.setGwBanner(gwBanner);
        meeting.setGwUrl(gwUrl);
        meeting.setExtContent(extContent);
        meeting.setAuthorAdminId(userId != null ? String.valueOf(userId) : null);
        meeting.setAuthor(SecurityUtils.getUsername());
        meeting.setCreateTimeStamp(System.currentTimeMillis() / 1000);
        meeting.setUpdateTimeStamp(meeting.getCreateTimeStamp());
        // 时间处理：如果是时间戳（秒/毫秒）或yyyy-MM-dd HH:mm:ss字符串
        meeting.setStartTime(parseToSeconds(startTime));
        meeting.setEndTime(parseToSeconds(endTime));
        
        int rows = meetingService.insertMeeting(meeting);
        return rows > 0 ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
    }
    
    /**
     * 编辑会议
     */
    @PostMapping("/editAdminMeeting")
    @PreAuthorize("@ss.hasPermi('medical:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    public AjaxResult editAdminMeeting(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Long id = Convert.toLong(params.get("id"));
        String meetingName = Convert.toStr(params.get("meeting_name"));
        String meetingImg = Convert.toStr(params.get("meeting_img"));
        String startTime = Convert.toStr(params.get("start_time"));
        String endTime = Convert.toStr(params.get("end_time"));
        Integer qyId = Convert.toInt(params.get("qy_id"));
        String meetingContent = Convert.toStr(params.get("meeting_content"));
        Integer isPriceStatus = Convert.toInt(params.get("is_price_status"));
        String priceStr = Convert.toStr(params.get("price"));
        String gwBanner = Convert.toStr(params.get("gw_banner"));
        String gwUrl = Convert.toStr(params.get("gw_url"));
        String meetingAddress = Convert.toStr(params.get("meeting_address"));
        String extContent = Convert.toStr(params.get("ext_content"));
        
        if (id == null)
        {
            return AjaxResult.error("请输入id");
        }
        
        MedicalMeeting meeting = meetingService.selectMeetingById(id);
        if (meeting == null)
        {
            return AjaxResult.error("会议不存在");
        }
        
        if (StringUtils.isNotEmpty(meetingName)) meeting.setMeetingName(meetingName);
        if (StringUtils.isNotEmpty(meetingImg)) meeting.setMeetingImg(meetingImg);
        if (StringUtils.isNotEmpty(meetingAddress)) meeting.setMeetingAddress(meetingAddress);
        if (StringUtils.isNotEmpty(meetingContent)) meeting.setMeetingContent(meetingContent);
        if (qyId != null) meeting.setQyId(qyId);
        String qyName = Convert.toStr(params.get("qy_name"));
        if (StringUtils.isNotEmpty(qyName)) meeting.setQyName(qyName);
        if (isPriceStatus != null) meeting.setIsPriceStatus(isPriceStatus);
        BigDecimal price = parseBigDecimal(priceStr);
        if (price != null) meeting.setPrice(price);
        if (StringUtils.isNotEmpty(gwBanner)) meeting.setGwBanner(gwBanner);
        if (StringUtils.isNotEmpty(gwUrl)) meeting.setGwUrl(gwUrl);
        if (StringUtils.isNotEmpty(extContent)) meeting.setExtContent(extContent);
        Long sTime = parseToSeconds(startTime);
        if (sTime != null) meeting.setStartTime(sTime);
        Long eTime = parseToSeconds(endTime);
        if (eTime != null) meeting.setEndTime(eTime);
        meeting.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        
        int rows = meetingService.updateMeeting(meeting);
        return rows > 0 ? AjaxResult.success("修改成功") : AjaxResult.error("修改失败");
    }
    
    /**
     * 获取报名列表
     */
    @PostMapping("/getSignUpList")
    @PreAuthorize("@ss.hasPermi('medical:meeting:signupList')")
    public TableDataInfo getSignUpList(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Integer size = Convert.toInt(params.getOrDefault("size", 20));
        Integer page = Convert.toInt(params.getOrDefault("page", 1));
        Long meetingId = Convert.toLong(params.get("meeting_id"));
        Integer status = Convert.toInt(params.get("status"));
        
        startPage();
        
        MedicalSignUp query = new MedicalSignUp();
        if (meetingId != null) query.setMeetingId(meetingId);
        if (status != null) query.setStatus(status);
        
        List<MedicalSignUp> list = signUpService.selectSignUpList(query);
        
        // 缓存会议名称，避免重复查询
        Map<Long, String> meetingNameCache = new HashMap<>();
        List<Long> meetingIds = list.stream()
                .map(MedicalSignUp::getMeetingId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (!meetingIds.isEmpty())
        {
            for (Long mid : meetingIds)
            {
                MedicalMeeting m = meetingService.selectMeetingById(mid);
                if (m != null)
                {
                    meetingNameCache.put(mid, m.getMeetingName());
                }
            }
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (MedicalSignUp s : list)
        {
            Map<String, Object> item = new HashMap<>();
            item.put("id", s.getId());
            item.put("meeting_id", s.getMeetingId());
            item.put("uid", s.getUid());
            item.put("sign_up_name", s.getSignUpName());
            item.put("sign_up_phone", s.getSignUpPhone());
            item.put("sign_up_company", s.getSignUpCompany());
            item.put("sign_up_position", s.getSignUpPosition());
            item.put("status", s.getStatus());
            item.put("ext_content", s.getExtContent());
            item.put("trip_content", s.getTripContent());
            item.put("remark", s.getRemark());
            item.put("create_time", s.getCreateTimeStamp());
            if (s.getMeetingId() != null)
            {
                item.put("meeting_name", meetingNameCache.get(s.getMeetingId()));
            }
            result.add(item);
        }
        
        return getDataTable(result);
    }
    
    /**
     * 审核报名
     */
    @PostMapping("/checkSignUp")
    @PreAuthorize("@ss.hasPermi('medical:meeting:audit')")
    @Log(title = "报名审核", businessType = BusinessType.UPDATE)
    public AjaxResult checkSignUp(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Integer status = Convert.toInt(params.get("status"));
        String remark = Convert.toStr(params.get("remark"));
        Long id = Convert.toLong(params.get("id"));
        
        if (id == null)
        {
            return AjaxResult.error("请选择ID");
        }
        
        if (status == null)
        {
            return AjaxResult.error("请选择审核状态");
        }
        
        MedicalSignUp sign = signUpService.selectSignUpById(id);
        if (sign == null)
        {
            return AjaxResult.error("记录不存在");
        }
        sign.setStatus(status);
        sign.setRemark(remark);
        sign.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        
        int rows = signUpService.updateSignUp(sign);
        return rows > 0 ? AjaxResult.success("审核成功") : AjaxResult.error("审核失败");
    }
    
    /**
     * 导出会议Excel
     */
    @GetMapping("/meetingExcel")
    @PreAuthorize("@ss.hasPermi('medical:meeting:export')")
    public void meetingExcel()
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        // TODO: 实现导出会议Excel逻辑
        // 1. 根据管理员权限查询会议数据
        // 2. 生成CSV文件
        // 3. 设置响应头
        // 4. 输出文件流
    }
    
    /**
     * 导出报名Excel
     */
    @GetMapping("/signUpExcel")
    @PreAuthorize("@ss.hasPermi('medical:meeting:exportSignup')")
    public void signUpExcel(@RequestParam(value = "id", required = false) String id)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        // TODO: 实现导出报名Excel逻辑
        // 1. 根据会议ID和管理员权限查询报名数据
        // 2. 生成CSV文件
        // 3. 设置响应头
        // 4. 输出文件流
    }
    
    /**
     * 获取可导出报名的会议列表
     */
    @PostMapping("/getSignUpExcelMeeting")
    @PreAuthorize("@ss.hasPermi('medical:meeting:list')")
    public AjaxResult getSignUpExcelMeeting()
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        List<MedicalMeeting> list = meetingService.selectMeetingList(new MedicalMeeting());
        List<Map<String, Object>> result = new ArrayList<>();
        for (MedicalMeeting m : list)
        {
            Map<String, Object> item = new HashMap<>();
            item.put("id", m.getId());
            item.put("meeting_name", m.getMeetingName());
            result.add(item);
        }
        
        return AjaxResult.success("", result);
    }
    
    /**
     * 城市映射
     */
    private String cityMapping(Integer cityId)
    {
        Map<Integer, String> cityMap = new HashMap<>();
        cityMap.put(1, "北京");
        cityMap.put(2, "天津");
        cityMap.put(3, "河北");
        cityMap.put(4, "山西");
        cityMap.put(5, "内蒙古自治区");
        cityMap.put(6, "辽宁");
        cityMap.put(7, "吉林");
        cityMap.put(8, "黑龙江");
        cityMap.put(9, "上海");
        cityMap.put(10, "江苏");
        cityMap.put(11, "浙江");
        cityMap.put(12, "安徽");
        cityMap.put(13, "福建");
        cityMap.put(14, "江西");
        cityMap.put(15, "山东");
        cityMap.put(16, "河南");
        cityMap.put(17, "湖北");
        cityMap.put(18, "湖南");
        cityMap.put(19, "广东");
        cityMap.put(20, "广西壮族自治区");
        cityMap.put(21, "海南");
        cityMap.put(22, "重庆");
        cityMap.put(23, "四川");
        cityMap.put(24, "贵州");
        cityMap.put(25, "云南");
        cityMap.put(26, "西藏自治区");
        cityMap.put(27, "陕西");
        cityMap.put(28, "甘肃");
        cityMap.put(29, "宁夏回族自治区");
        cityMap.put(30, "新疆维吾尔族自治区");
        cityMap.put(31, "台湾");
        cityMap.put(32, "香港特别行政区");
        cityMap.put(33, "澳门特别行政区");
        
        return cityMap.getOrDefault(cityId, "");
    }
    
    /**
     * 报名状态映射
     */
    private String signUpMapping(Integer status)
    {
        Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(1, "待审核");
        statusMap.put(2, "待参加");
        statusMap.put(3, "审核未通过");
        
        return statusMap.getOrDefault(status, "");
    }
    
    /**
     * 将多种时间格式转换为秒级时间戳
     */
    private Long parseToSeconds(String val)
    {
        if (StringUtils.isEmpty(val)) return null;
        try
        {
            // 如果是纯数字
            if (val.matches("^\\d+$"))
            {
                long num = Long.parseLong(val);
                // 如果是13位视为毫秒
                return (val.length() == 13) ? num / 1000 : num;
            }
            // 尝试解析 yyyy-MM-dd HH:mm:ss
            java.time.LocalDateTime dt = java.time.LocalDateTime.parse(val, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return dt.atZone(java.time.ZoneId.systemDefault()).toEpochSecond();
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * 将字符串安全转换为BigDecimal
     */
    private BigDecimal parseBigDecimal(String val)
    {
        if (StringUtils.isEmpty(val)) return null;
        try
        {
            return new BigDecimal(val);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
