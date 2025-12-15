package cn.project.web.controller.medical;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.common.utils.TokenUtils;
import cn.project.common.core.text.Convert;
import cn.project.common.annotation.Log;
import cn.project.common.enums.BusinessType;
import org.springframework.security.access.prepost.PreAuthorize;
import cn.project.system.domain.medical.MedicalCard;
import cn.project.system.service.medical.IMedicalCardService;
import cn.project.system.service.medical.IMedicalCollectService;

/**
 * 医简历管理员卡片管理控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Admin/AdminCard")
public class AdminCardController extends BaseController
{
    @Autowired
    private IMedicalCardService cardService;
    
    @Autowired
    private IMedicalCollectService collectService;
    
    /**
     * 获取管理员简历列表
     */
    @PostMapping("/getAdminCardList")
    @PreAuthorize("@ss.hasPermi('medical:card:list')")
    public TableDataInfo getAdminCardList(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        String title = Convert.toStr(params.get("title"));
        
        startPage();
        
        // 查询所有简历（管理员可以看到所有数据）
        MedicalCard query = new MedicalCard();
        if (StringUtils.isNotEmpty(title))
        {
            query.setName(title);
        }
        
        List<MedicalCard> cardList = cardService.selectCardList(query);
        
        // 获取科室映射
        Map<Integer, String> departmentMap = getDepartmentMap();
        
        // 转换为Map并添加科室名称
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (MedicalCard card : cardList)
        {
            Map<String, Object> cardMap = new HashMap<>();
            cardMap.put("id", card.getId());
            cardMap.put("uid", card.getUid());
            cardMap.put("img", card.getImg());
            cardMap.put("img_yt", card.getImgYt());
            cardMap.put("name", card.getName());
            cardMap.put("company", card.getCompany());
            cardMap.put("position", card.getPosition());
            cardMap.put("department", card.getDepartment());
            cardMap.put("department_name", departmentMap.get(card.getDepartment()));
            cardMap.put("phone", card.getPhone());
            cardMap.put("email", card.getEmail());
            cardMap.put("address", card.getAddress());
            cardMap.put("wechat", card.getWechat());
            cardMap.put("city", card.getCity());
            cardMap.put("content", card.getContent());
            cardMap.put("attachment", card.getAttachment());
            cardMap.put("attachment_name", card.getAttachmentName());
            cardMap.put("create_time", card.getCreateTimeStamp());
            cardMap.put("update_time", card.getUpdateTimeStamp());
            cardMap.put("is_vip", card.getIsVip());
            cardMap.put("is_status", card.getIsStatus());
            resultList.add(cardMap);
        }
        
        return getDataTable(resultList);
    }
    
    /**
     * 更新简历信息
     */
    @PostMapping("/updateAdminCardList")
    @PreAuthorize("@ss.hasPermi('medical:card:edit')")
    @Log(title = "简历管理", businessType = BusinessType.UPDATE)
    public AjaxResult updateAdminCardList(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Long id = Convert.toLong(params.get("id"));
        if (id == null)
        {
            return AjaxResult.error("请输入id");
        }
        
        MedicalCard card = cardService.selectCardById(id);
        if (card == null)
        {
            return AjaxResult.error("简历不存在");
        }
        
        // 更新字段
        String img = Convert.toStr(params.get("img"));
        String imgYt = Convert.toStr(params.get("img_yt"));
        String name = Convert.toStr(params.get("name"));
        String company = Convert.toStr(params.get("company"));
        String position = Convert.toStr(params.get("position"));
        Integer department = Convert.toInt(params.get("department"));
        String phone = Convert.toStr(params.get("phone"));
        String email = Convert.toStr(params.get("email"));
        String address = Convert.toStr(params.get("address"));
        String wechat = Convert.toStr(params.get("wechat"));
        Integer city = Convert.toInt(params.get("city"));
        String content = Convert.toStr(params.get("content"));
        String attachment = Convert.toStr(params.get("attachment"));
        String attachmentName = Convert.toStr(params.get("attachment_name"));
        
        if (StringUtils.isNotEmpty(img)) card.setImg(img);
        if (StringUtils.isNotEmpty(imgYt)) card.setImgYt(imgYt);
        if (StringUtils.isNotEmpty(name)) card.setName(name);
        if (StringUtils.isNotEmpty(company)) card.setCompany(company);
        if (StringUtils.isNotEmpty(position)) card.setPosition(position);
        if (department != null) card.setDepartment(department);
        if (StringUtils.isNotEmpty(phone)) card.setPhone(phone);
        if (StringUtils.isNotEmpty(email)) card.setEmail(email);
        if (StringUtils.isNotEmpty(address)) card.setAddress(address);
        if (StringUtils.isNotEmpty(wechat)) card.setWechat(wechat);
        if (city != null) card.setCity(city);
        if (StringUtils.isNotEmpty(content)) card.setContent(content);
        if (StringUtils.isNotEmpty(attachment)) card.setAttachment(attachment);
        if (StringUtils.isNotEmpty(attachmentName)) card.setAttachmentName(attachmentName);
        
        card.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        
        int result = cardService.updateCard(card);
        return result > 0 ? AjaxResult.success("success") : AjaxResult.error("更新失败");
    }
    
    /**
     * 删除简历
     */
    @PostMapping("/delCardAdmin")
    @PreAuthorize("@ss.hasPermi('medical:card:remove')")
    @Log(title = "简历管理", businessType = BusinessType.DELETE)
    public AjaxResult delCardAdmin(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Long id = Convert.toLong(params.get("id"));
        
        if (id == null)
        {
            return AjaxResult.error("请输入id");
        }
        
        // 删除简历
        int result = cardService.deleteCardById(id);
        
        // TODO: 删除相关收藏记录（需要在Service层实现级联删除）
        
        return result > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }
    
    /**
     * 审核简历
     */
    @PostMapping("/toExamineCard")
    @PreAuthorize("@ss.hasPermi('medical:card:audit')")
    @Log(title = "简历审核", businessType = BusinessType.UPDATE)
    public AjaxResult toExamineCard(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Long id = Convert.toLong(params.get("id"));
        Integer isStatus = Convert.toInt(params.get("is_status"));
        
        if (id == null)
        {
            return AjaxResult.error("请输入id");
        }
        
        if (isStatus == null)
        {
            return AjaxResult.error("请输入状态");
        }
        
        MedicalCard card = cardService.selectCardById(id);
        if (card == null)
        {
            return AjaxResult.error("简历不存在");
        }
        
        card.setIsStatus(isStatus);
        card.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        
        int result = cardService.updateCard(card);
        return result > 0 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }
    
    /**
     * 更新附件名称
     */
    @PostMapping("/updateAttachment")
    @PreAuthorize("@ss.hasPermi('medical:card:edit')")
    @Log(title = "简历管理", businessType = BusinessType.UPDATE)
    public AjaxResult updateAttachment(@RequestBody Map<String, Object> params)
    {
        // 使用Spring Security获取当前登录用户
        Long userId = SecurityUtils.getUserId();
        
        Long id = Convert.toLong(params.get("id"));
        String attachmentName = Convert.toStr(params.get("attachment_name"));
        
        if (StringUtils.isEmpty(attachmentName) || id == null)
        {
            return AjaxResult.error("请输入附件名称");
        }
        
        MedicalCard card = cardService.selectCardById(id);
        if (card == null)
        {
            return AjaxResult.error("简历不存在");
        }
        
        card.setAttachmentName(attachmentName);
        card.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
        
        int result = cardService.updateCard(card);
        return result > 0 ? AjaxResult.success("操作成功") : AjaxResult.error("操作失败");
    }
    
    /**
     * 创建简历数据
     */
    @PostMapping("/createCardData")
    @PreAuthorize("@ss.hasPermi('medical:card:add')")
    @Log(title = "简历管理", businessType = BusinessType.INSERT)
    public AjaxResult createCardData(@RequestBody Map<String, Object> params)
    {
        String phone = (String) params.get("phone");
        
        if (StringUtils.isEmpty(phone))
        {
            return AjaxResult.error("手机号必传，需要数据打通");
        }
        
        String img = (String) params.get("img");
        String imgYt = (String) params.get("img_yt");
        String name = (String) params.get("name");
        String company = (String) params.get("company");
        String position = (String) params.get("position");
        String department = (String) params.get("department");
        String city = (String) params.get("city");
        String email = (String) params.get("email");
        String address = (String) params.get("address");
        String wechat = (String) params.get("wechat");
        String content = (String) params.get("content");
        String attachment = (String) params.get("attachment");
        String attachmentName = (String) params.get("attachment_name");
        
        // TODO: 实现创建简历数据逻辑
        // 1. 检查手机号是否已存在
        // 2. 生成uid
        // 3. 处理城市名称映射
        // 4. 创建简历记录
        
        return AjaxResult.success("success");
    }
    
    /**
     * 导出简历Excel
     */
    @GetMapping("/cardExcel")
    @PreAuthorize("@ss.hasPermi('medical:card:export')")
    public void cardExcel(@RequestParam(value = "id", required = false) String id)
    {
        // TODO: 实现导出简历Excel逻辑
        // 1. 根据ID查询简历数据
        // 2. 生成CSV文件
        // 3. 设置响应头
        // 4. 输出文件流
    }
    
    /**
     * 获取科室映射
     */
    private Map<Integer, String> getDepartmentMap()
    {
        Map<Integer, String> departmentMap = new HashMap<>();
        String[] departments = {
            "心内科", "血液内科", "老年病科", "呼吸内科", "肾内科", "消化内科", "神经内科", "感染病科",
            "内分泌科", "风湿免疫科", "精神卫生科", "大内科及其他", "骨科", "普外科", "胸心外科", "神经外科",
            "烧伤水伤修复外科", "泌尿外科", "整形美容外科", "器官移植科", "耳鼻咽喉头颈外科", "妇产科", "儿科", "口腔医学科",
            "肿瘤科", "眼科", "检验医学科", "急危重症科", "健康管理科", "科研教育科", "临床药学", "医院管理",
            "麻醉科", "影像科", "康复医学科", "皮肤性病科", "预防医学科", "全科医学科", "临床营养科", "中医药学科",
            "基础医学", "临床输血", "罕见病科", "介入医学科"
        };
        
        for (int i = 0; i < departments.length; i++)
        {
            departmentMap.put(i + 1, departments[i]);
        }
        return departmentMap;
    }
}
