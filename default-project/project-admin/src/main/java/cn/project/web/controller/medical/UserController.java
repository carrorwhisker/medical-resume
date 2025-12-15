package cn.project.web.controller.medical;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.common.utils.AesUtils;
import cn.project.common.utils.TokenUtils;
import cn.project.common.utils.WxBizDataCrypt;
import cn.project.common.utils.http.HttpUtils;
import cn.project.common.core.text.Convert;
import cn.project.common.annotation.Log;
import cn.project.common.enums.BusinessType;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import cn.project.system.service.medical.IMedicalUserService;
import cn.project.system.domain.medical.MedicalUser;
import cn.project.system.service.medical.IMedicalCollectService;

/**
 * 医简历用户控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/User/User")
public class UserController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private IMedicalCollectService collectService;
    
    @Autowired
    private IMedicalUserService userService;
    
    // 微信小程序配置
    private static final String APP_ID = "wx0afd3279d39d709f";
    private static final String APP_SECRET = "c13d9943f0b781f76b00601a6c725052";
    
    /**
     * 获取用户信息
     */
    @PostMapping("/userInfo")
    public AjaxResult userInfo(@RequestHeader(value = "Token", required = false) String token)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        if (StringUtils.isEmpty(uid))
        {
            return AjaxResult.error("请登录");
        }
        
        // 查询用户信息
        MedicalUser user = userService.selectUserByUid(uid);
        if (user == null)
        {
            return AjaxResult.error("用户不存在");
        }
        
        // 计算VIP到期时间（格式：yyyy-MM-dd），如果为空返回 "-"
        String vipTimeStr = "-";
        if (user.getVipTime() != null && user.getVipTime() > 0)
        {
            try
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                vipTimeStr = sdf.format(new Date(user.getVipTime() * 1000));
            }
            catch (Exception e)
            {
                log.warn("format vip time error", e);
            }
        }
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("img", StringUtils.defaultString(user.getImg()));
        userData.put("username", StringUtils.defaultString(user.getUsername()));
        userData.put("phone", StringUtils.defaultString(user.getPhone()));
        userData.put("content", StringUtils.defaultString(user.getContent()));
        userData.put("is_vip", user.getIsVip() == null ? 1 : user.getIsVip());
        userData.put("vip_time", vipTimeStr);
        
        List<Map<String, Object>> userDataList = new ArrayList<>();
        userDataList.add(userData);
        return AjaxResult.success("success", userDataList);
    }
    
    /**
     * 更新用户信息
     */
    @PostMapping("/updateUserInfo")
    public AjaxResult updateUserInfo(@RequestHeader(value = "Token", required = false) String token,
                                   @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        String img = Convert.toStr(params.get("img"));
        String name = Convert.toStr(params.get("name"));
        String phone = Convert.toStr(params.get("phone"));
        String content = Convert.toStr(params.get("content"));
        
        // 查询用户
        MedicalUser user = userService.selectUserByUid(uid);
        if (user == null)
        {
            return AjaxResult.error("用户不存在");
        }
        
        // 更新字段（仅当传入时更新）
        if (StringUtils.isNotEmpty(img))
        {
            user.setImg(img);
        }
        if (StringUtils.isNotEmpty(name))
        {
            user.setUsername(name);
        }
        if (StringUtils.isNotEmpty(phone))
        {
            user.setPhone(phone);
        }
        if (content != null)
        {
            user.setContent(content);
        }
        user.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        
        int rows = userService.updateUser(user);
        return rows > 0 ? AjaxResult.success("success") : AjaxResult.error("更新失败");
    }
    
    /**
     * 获取收藏列表
     */
    @PostMapping("/getCollectList")
    public TableDataInfo getCollectList(@RequestHeader(value = "Token", required = false) String token,
                                      @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return getDataTable(new ArrayList<>());
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        if (StringUtils.isEmpty(uid))
        {
            return getDataTable(new ArrayList<>());
        }
        
        Integer size = (Integer) params.getOrDefault("size", 20);
        Integer page = (Integer) params.getOrDefault("page", 1);
        String position = Convert.toStr(params.get("position"));
        String city = Convert.toStr(params.get("city"));
        String title = Convert.toStr(params.get("title"));
        
        // 如果 position 为空字符串，设置为 null
        if (StringUtils.isEmpty(position))
        {
            position = null;
        }
        if (StringUtils.isEmpty(city))
        {
            city = null;
        }
        if (StringUtils.isEmpty(title))
        {
            title = null;
        }
        
        startPage();
        
        // 查询收藏列表（联表查询）
        List<Map<String, Object>> collectList = collectService.selectCollectCardList(uid, position, city, title);
        
        // 获取科室映射
        Map<Integer, String> departmentMap = getDepartmentMap();
        
        // 处理返回数据格式
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map<String, Object> item : collectList)
        {
            Map<String, Object> resultItem = new HashMap<>();
            resultItem.put("id", item.get("id"));
            resultItem.put("name", item.get("name"));
            resultItem.put("img", item.get("img"));
            resultItem.put("company", item.get("company"));
            resultItem.put("position", item.get("position"));
            
            // 处理科室：转换为名称
            Object deptObj = item.get("department");
            if (deptObj != null)
            {
                Integer deptCode = Convert.toInt(deptObj, null);
                if (deptCode != null)
                {
                    resultItem.put("department", departmentMap.get(deptCode));
                }
                else
                {
                    resultItem.put("department", "");
                }
            }
            else
            {
                resultItem.put("department", "");
            }
            
            // 处理附件：如果有附件则设置 is_attachment = 1，否则为 0，并删除 attachment 字段
            Object attachment = item.get("attachment");
            if (attachment != null && StringUtils.isNotEmpty(Convert.toStr(attachment)))
            {
                resultItem.put("is_attachment", 1);
            }
            else
            {
                resultItem.put("is_attachment", 0);
            }
            
            resultItem.put("attachment_name", item.get("attachment_name"));
            
            // 处理认证状态
            Object authObj = item.get("is_authentication");
            if (authObj != null)
            {
                resultItem.put("is_authentication", Convert.toInt(authObj, 0));
            }
            else
            {
                resultItem.put("is_authentication", 0);
            }
            
            resultList.add(resultItem);
        }
        
        return getDataTable(resultList);
    }
    
    /**
     * 用户登录（微信小程序）
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, Object> params)
    {
        String jsCode = Convert.toStr(params.get("jscode"));
        String encryptedData = Convert.toStr(params.get("encryptedData"));
        String iv = Convert.toStr(params.get("iv"));
        
        if (StringUtils.isEmpty(jsCode))
        {
            return AjaxResult.error("jscode不可为空");
        }
        
        if (StringUtils.isEmpty(encryptedData) || StringUtils.isEmpty(iv))
        {
            return AjaxResult.error("encryptedData or iv 参数有误");
        }
        
        try
        {
            // 1. 调用微信API获取session_key和openid
            String wxUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APP_ID 
                    + "&secret=" + APP_SECRET + "&js_code=" + jsCode + "&grant_type=authorization_code";
            
            String wxResponse = HttpUtils.sendGet(wxUrl);
            JSONObject wxData = JSON.parseObject(wxResponse);
            
            Integer errcode = wxData.getInteger("errcode");
            if (errcode != null && errcode == 40163)
            {
                return AjaxResult.error("code已使用");
            }
            
            String sessionKey = wxData.getString("session_key");
            String openid = wxData.getString("openid");
            
            if (StringUtils.isEmpty(sessionKey) || StringUtils.isEmpty(openid))
            {
                return AjaxResult.error("获取微信信息失败");
            }
            
            // TODO: 保存session_key和openid到数据库（User_wechat表）
            
            // 2. 解密手机号
            String decryptedData = WxBizDataCrypt.decryptData(APP_ID, sessionKey, encryptedData, iv);
            if (StringUtils.isEmpty(decryptedData))
            {
                return AjaxResult.error("网络不稳定，请重新授权登录");
            }
            
            JSONObject phoneData = JSON.parseObject(decryptedData);
            String phoneNumber = phoneData.getString("phoneNumber");
            
            if (StringUtils.isEmpty(phoneNumber))
            {
                return AjaxResult.error("获取手机号失败");
            }
            
            // 3. 生成UID（根据手机号生成12位MD5）
            String uid = getUniqueUid(phoneNumber, 12);
            
            // TODO: 查询用户是否存在
            // User user = userService.getUserByWechatPhone(phoneNumber);
            
            // TODO: 如果用户不存在，创建新用户
            // if (user == null) {
            //     Random random = new Random();
            //     int randomNum = random.nextInt(999999 - 111111 + 1) + 111111;
            //     user = new User();
            //     user.setUsername("用户" + randomNum);
            //     user.setContent("这个人很懒，什么也没有留下！" + phoneNumber);
            //     user.setUid(uid);
            //     user.setPhone(phoneNumber);
            //     user.setWechatPhone(phoneNumber);
            //     user.setImg("https://qy20210816.oss-cn-beijing.aliyuncs.com/12241634711940_.pic_hd.jpg");
            //     user.setOpenid(openid);
            //     user.setIsVip(2);
            //     userService.insertUser(user);
            // }
            
            // 4. 生成token
            String token = TokenUtils.generateToken(uid);
            
            if (StringUtils.isEmpty(token))
            {
                return AjaxResult.error("生成token失败");
            }
            
            // 返回token字符串（不是对象）
            return AjaxResult.success("success", token);
        }
        catch (Exception e)
        {
            log.error("微信登录失败", e);
            return AjaxResult.error("登录失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据手机号生成唯一UID
     * 
     * @param phoneNumber 手机号
     * @param length UID长度
     * @return UID
     */
    private String getUniqueUid(String phoneNumber, int length)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(phoneNumber.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash)
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            String md5Hash = hexString.toString();
            return md5Hash.substring(0, Math.min(length, md5Hash.length()));
        }
        catch (Exception e)
        {
            log.error("生成UID失败", e);
            return phoneNumber.substring(0, Math.min(length, phoneNumber.length()));
        }
    }
    
    /**
     * 获取科室列表映射
     */
    private Map<Integer, String> getDepartmentMap()
    {
        Map<Integer, String> departmentMap = new HashMap<>();
        departmentMap.put(1, "心内科");
        departmentMap.put(2, "血液内科");
        departmentMap.put(3, "老年病科");
        departmentMap.put(4, "呼吸内科");
        departmentMap.put(5, "肾内科");
        departmentMap.put(6, "消化内科");
        departmentMap.put(7, "神经内科");
        departmentMap.put(8, "感染病科");
        departmentMap.put(9, "内分泌科");
        departmentMap.put(10, "风湿免疫科");
        departmentMap.put(11, "精神卫生科");
        departmentMap.put(12, "大内科及其他");
        departmentMap.put(13, "骨科");
        departmentMap.put(14, "普外科");
        departmentMap.put(15, "胸心外科");
        departmentMap.put(16, "神经外科");
        departmentMap.put(17, "烧伤水伤修复外科");
        departmentMap.put(18, "泌尿外科");
        departmentMap.put(19, "整形美容外科");
        departmentMap.put(20, "器官移植科");
        departmentMap.put(21, "耳鼻咽喉头颈外科");
        departmentMap.put(22, "妇产科");
        departmentMap.put(23, "儿科");
        departmentMap.put(24, "口腔医学科");
        departmentMap.put(25, "肿瘤科");
        departmentMap.put(26, "眼科");
        departmentMap.put(27, "检验医学科");
        departmentMap.put(28, "急危重症科");
        departmentMap.put(29, "健康管理科");
        departmentMap.put(30, "科研教育科");
        departmentMap.put(31, "临床药学");
        departmentMap.put(32, "医院管理");
        departmentMap.put(33, "麻醉科");
        departmentMap.put(34, "影像科");
        departmentMap.put(35, "康复医学科");
        departmentMap.put(36, "皮肤性病科");
        departmentMap.put(37, "预防医学科");
        departmentMap.put(38, "全科医学科");
        departmentMap.put(39, "临床营养科");
        departmentMap.put(40, "中医药学科");
        departmentMap.put(41, "基础医学");
        departmentMap.put(42, "临床输血");
        departmentMap.put(43, "罕见病科");
        departmentMap.put(44, "介入医学科");
        
        return departmentMap;
    }
}
