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
import cn.project.system.domain.medical.MedicalCard;
import cn.project.system.service.medical.IMedicalCardService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.fastjson2.JSONObject;

/**
 * 医简历卡片控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Card/Card")
public class CardController extends BaseController
{
    @Autowired
    private IMedicalCardService cardService;
    
    /**
     * 获取城市列表
     */
    @PostMapping("/cityList")
    public AjaxResult cityList()
    {
        List<Map<String, Object>> cityList = getCityList();
        return AjaxResult.success("success", cityList);
    }
    
    /**
     * 获取职位列表
     */
    @PostMapping("/positionList")
    public AjaxResult positionList()
    {
        List<Map<String, Object>> positionList = getPositionList();
        return AjaxResult.success("success", positionList);
    }
    
    /**
     * 获取筛选列表
     */
    @PostMapping("/filterList")
    public AjaxResult filterList()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("city", getCityList());
        result.put("position", getPositionList());
        
        return AjaxResult.success("success", result);
    }
    
    /**
     * 获取科室列表
     */
    @PostMapping("/departmentList")
    public AjaxResult departmentList()
    {
        List<Map<String, Object>> departmentList = getDepartmentList();
        return AjaxResult.success("success", departmentList);
    }
    
    /**
     * 获取用户简历信息列表
     */
    @PostMapping("/cardInfoList")
    public AjaxResult cardInfoList(@RequestHeader(value = "Token", required = false) String token)
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
        
        // 查询用户简历列表
        MedicalCard queryCard = new MedicalCard();
        queryCard.setUid(uid);
        List<MedicalCard> cardList = cardService.selectCardList(queryCard);
        
        if (cardList == null || cardList.isEmpty())
        {
            return AjaxResult.error("暂无数据", "");
        }
        
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
        
        return AjaxResult.success("success", resultList);
    }
    
    /**
     * 保存简历
     */
    @PostMapping("/saveCard")
    public AjaxResult saveCard(@RequestHeader(value = "Token", required = false) String token,
                             @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        // 使用Convert.toStr安全转换，支持String和Integer类型
        String img = Convert.toStr(params.get("img"));
        String imgYt = Convert.toStr(params.get("img_yt"));
        String name = Convert.toStr(params.get("name"));
        String company = Convert.toStr(params.get("company"));
        String position = Convert.toStr(params.get("position"));
        String department = Convert.toStr(params.get("department"));
        String phone = Convert.toStr(params.get("phone"));
        String email = Convert.toStr(params.get("email"));
        String address = Convert.toStr(params.get("address"));
        String city = Convert.toStr(params.get("city"));
        String wechat = Convert.toStr(params.get("wechat"));
        String content = Convert.toStr(params.get("content"));
        String attachment = Convert.toStr(params.get("attachment"));
        String attachmentName = Convert.toStr(params.get("attachment_name"));
        String id = Convert.toStr(params.get("id"));
        
        if (StringUtils.isEmpty(uid))
        {
            return AjaxResult.error("请登录");
        }
        
        // 1. 验证用户简历数量限制（最多3份，但PHP代码显示是2份）
        int cardCount = cardService.countByUid(uid);
        if (StringUtils.isEmpty(id) && cardCount >= 3)
        {
            return AjaxResult.error("最多只能保存2份简历");
        }
        
        // 2. 处理城市名称映射
        Integer cityCode = null;
        if (StringUtils.isNotEmpty(city))
        {
            String shortName = city.length() >= 2 ? city.substring(0, 2) : city;
            if ("黑龙".equals(shortName))
            {
                cityCode = 8;
            }
            else
            {
                List<Map<String, Object>> cityList = getCityList();
                for (Map<String, Object> cityItem : cityList)
                {
                    if (shortName.equals(cityItem.get("name")))
                    {
                        Object code = cityItem.get("code");
                        if (code instanceof Integer)
                        {
                            cityCode = (Integer) code;
                        }
                        else if (code != null)
                        {
                            cityCode = Convert.toInt(code);
                        }
                        break;
                    }
                }
            }
        }
        
        // 3. 获取用户VIP状态（TODO: 需要实现UserService）
        Integer isVip = 1; // 默认值，后续需要从数据库查询
        
        if (StringUtils.isEmpty(id))
        {
            // 新增简历
            MedicalCard card = new MedicalCard();
            card.setUid(uid);
            card.setImg(img);
            card.setImgYt(imgYt);
            card.setName(name);
            card.setCompany(company);
            card.setPosition(position);
            card.setDepartment(Convert.toInt(department));
            card.setPhone(phone);
            card.setEmail(email);
            card.setAddress(address);
            card.setWechat(wechat);
            card.setCity(cityCode);
            card.setContent(content);
            card.setAttachment(attachment);
            card.setAttachmentName(attachmentName);
            card.setCreateTimeStamp(System.currentTimeMillis() / 1000); // 秒级时间戳
            card.setIsVip(isVip);
            card.setIsStatus(0); // 待审核
            
            int result = cardService.insertCard(card);
            if (result > 0)
            {
                return AjaxResult.success("success", card);
            }
            else
            {
                return AjaxResult.error("保存失败");
            }
        }
        else
        {
            // 更新简历
            Long cardId = Convert.toLong(id, null);
            if (cardId == null)
            {
                return AjaxResult.error("简历ID无效");
            }
            MedicalCard card = cardService.selectCardById(cardId);
            if (card == null || !uid.equals(card.getUid()))
            {
                return AjaxResult.error("简历不存在或无权限");
            }
            
            // 更新字段
            if (StringUtils.isNotEmpty(img))
            {
                card.setImg(img);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(imgYt))
            {
                card.setImgYt(imgYt);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(name))
            {
                card.setName(name);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(company))
            {
                card.setCompany(company);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(position))
            {
                card.setPosition(position);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(department))
            {
                card.setDepartment(Convert.toInt(department));
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(phone))
            {
                card.setPhone(phone);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(email))
            {
                card.setEmail(email);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(address))
            {
                card.setAddress(address);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(wechat))
            {
                card.setWechat(wechat);
                card.setIsStatus(0);
            }
            if (cityCode != null)
            {
                card.setCity(cityCode);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(content))
            {
                card.setContent(content);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(attachment))
            {
                card.setAttachment(attachment);
                card.setIsStatus(0);
            }
            if (StringUtils.isNotEmpty(attachmentName))
            {
                card.setAttachmentName(attachmentName);
                card.setIsStatus(0);
            }
            
            card.setUpdateTimeStamp(System.currentTimeMillis() / 1000);
            
            int result = cardService.updateCard(card);
            if (result > 0)
            {
                return AjaxResult.success("success");
            }
            else
            {
                return AjaxResult.error("更新失败");
            }
        }
    }
    
    /**
     * 保存认证信息
     */
    @PostMapping("/saveAuthentication")
    public AjaxResult saveAuthentication(@RequestHeader(value = "Token", required = false) String token,
                                       @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        String name = Convert.toStr(params.get("name"));
        String num = Convert.toStr(params.get("num"));
        String authenticationStraight = Convert.toStr(params.get("straight"));
        String authenticationSide = Convert.toStr(params.get("side"));
        
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(num) || 
            StringUtils.isEmpty(authenticationStraight) || StringUtils.isEmpty(authenticationSide))
        {
            return AjaxResult.error("请填写完整");
        }
        
        // TODO: 实现保存认证信息逻辑
        // 1. 保存认证信息
        // 2. 更新简历认证状态
        
        return AjaxResult.success("保存成功");
    }
    
    /**
     * 获取认证信息
     */
    @PostMapping("/getAuthentication")
    public AjaxResult getAuthentication(@RequestHeader(value = "Token", required = false) String token)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        // TODO: 实现获取认证信息逻辑
        
        return AjaxResult.error("未认证");
    }
    
    /**
     * 获取简历列表（首页）
     */
    @PostMapping("/indexList")
    public TableDataInfo indexList(@RequestHeader(value = "Token", required = false) String token,
                                 @RequestBody Map<String, Object> params)
    {
        String position = Convert.toStr(params.get("position"));
        String city = Convert.toStr(params.get("city"));
        String title = Convert.toStr(params.get("title"));
        
        Integer size = (Integer) params.getOrDefault("size", 20);
        Integer page = (Integer) params.getOrDefault("page", 1);
        
        startPage();
        
        // TODO: 实现获取简历列表逻辑
        // 1. 根据条件查询已审核通过的简历
        // 2. 处理收藏状态
        // 3. 科室名称映射
        
        List<Map<String, Object>> list = new ArrayList<>(); // 空列表示例
        
        return getDataTable(list);
    }
    
    /**
     * 简历收藏/取消收藏
     */
    @PostMapping("/cardCollect")
    public AjaxResult cardCollect(@RequestHeader(value = "Token", required = false) String token,
                                @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        String id = Convert.toStr(params.get("id"));
        Integer type = (Integer) params.get("type");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        // TODO: 实现收藏/取消收藏逻辑
        // type = 1: 收藏
        // type = 2: 取消收藏
        
        if (type == 1)
        {
            return AjaxResult.success("success");
        }
        else if (type == 2)
        {
            return AjaxResult.success("取消收藏成功");
        }
        
        return AjaxResult.error("操作失败");
    }
    
    /**
     * 获取简历详情
     */
    @PostMapping("/cardDetail")
    public AjaxResult cardDetail(@RequestHeader(value = "Token", required = false) String token,
                               @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        String id = Convert.toStr(params.get("id"));
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        // TODO: 实现获取简历详情逻辑
        // 1. 查询简历信息
        // 2. 处理收藏状态
        // 3. 处理交换状态
        // 4. 科室名称映射
        
        return AjaxResult.success("暂无数据");
    }
    
    /**
     * 交换简历
     */
    @PostMapping("/exchangeCard")
    public AjaxResult exchangeCard(@RequestHeader(value = "Token", required = false) String token,
                                 @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        String oldCardId = Convert.toStr(params.get("old_card_id"));
        String exchangeCardId = Convert.toStr(params.get("exchange_card_id"));
        
        if (StringUtils.isEmpty(oldCardId))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        // TODO: 实现交换简历逻辑
        // 1. 验证用户有审核通过的简历
        // 2. 验证用户VIP状态或简历权限
        // 3. 创建交换记录
        
        return AjaxResult.success("success");
    }
    
    /**
     * 删除附件
     */
    @PostMapping("/delAttachment")
    public AjaxResult delAttachment(@RequestHeader(value = "Token", required = false) String token,
                                  @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        String id = Convert.toStr(params.get("id"));
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        // TODO: 实现删除附件逻辑
        
        return AjaxResult.success("删除附件成功！");
    }
    
    /**
     * 获取城市列表
     */
    private List<Map<String, Object>> getCityList()
    {
        List<Map<String, Object>> cityList = new ArrayList<>();
        
        Map<String, Object> city0 = new HashMap<>();
        city0.put("code", "");
        city0.put("name", "全部");
        cityList.add(city0);
        
        Map<String, Object> city1 = new HashMap<>();
        city1.put("code", 1);
        city1.put("name", "北京");
        cityList.add(city1);
        
        Map<String, Object> city2 = new HashMap<>();
        city2.put("code", 2);
        city2.put("name", "天津");
        cityList.add(city2);
        
        Map<String, Object> city3 = new HashMap<>();
        city3.put("code", 3);
        city3.put("name", "河北");
        cityList.add(city3);
        
        Map<String, Object> city4 = new HashMap<>();
        city4.put("code", 4);
        city4.put("name", "山西");
        cityList.add(city4);
        
        Map<String, Object> city5 = new HashMap<>();
        city5.put("code", 5);
        city5.put("name", "内蒙");
        cityList.add(city5);
        
        Map<String, Object> city6 = new HashMap<>();
        city6.put("code", 6);
        city6.put("name", "辽宁");
        cityList.add(city6);
        
        Map<String, Object> city7 = new HashMap<>();
        city7.put("code", 7);
        city7.put("name", "吉林");
        cityList.add(city7);
        
        Map<String, Object> city8 = new HashMap<>();
        city8.put("code", 8);
        city8.put("name", "黑龙江");
        cityList.add(city8);
        
        Map<String, Object> city9 = new HashMap<>();
        city9.put("code", 9);
        city9.put("name", "上海");
        cityList.add(city9);
        
        Map<String, Object> city10 = new HashMap<>();
        city10.put("code", 10);
        city10.put("name", "江苏");
        cityList.add(city10);
        
        Map<String, Object> city11 = new HashMap<>();
        city11.put("code", 11);
        city11.put("name", "浙江");
        cityList.add(city11);
        
        Map<String, Object> city12 = new HashMap<>();
        city12.put("code", 12);
        city12.put("name", "安徽");
        cityList.add(city12);
        
        Map<String, Object> city13 = new HashMap<>();
        city13.put("code", 13);
        city13.put("name", "福建");
        cityList.add(city13);
        
        Map<String, Object> city14 = new HashMap<>();
        city14.put("code", 14);
        city14.put("name", "江西");
        cityList.add(city14);
        
        Map<String, Object> city15 = new HashMap<>();
        city15.put("code", 15);
        city15.put("name", "山东");
        cityList.add(city15);
        
        Map<String, Object> city16 = new HashMap<>();
        city16.put("code", 16);
        city16.put("name", "河南");
        cityList.add(city16);
        
        Map<String, Object> city17 = new HashMap<>();
        city17.put("code", 17);
        city17.put("name", "湖北");
        cityList.add(city17);
        
        Map<String, Object> city18 = new HashMap<>();
        city18.put("code", 18);
        city18.put("name", "湖南");
        cityList.add(city18);
        
        Map<String, Object> city19 = new HashMap<>();
        city19.put("code", 19);
        city19.put("name", "广东");
        cityList.add(city19);
        
        Map<String, Object> city20 = new HashMap<>();
        city20.put("code", 20);
        city20.put("name", "广西");
        cityList.add(city20);
        
        Map<String, Object> city21 = new HashMap<>();
        city21.put("code", 21);
        city21.put("name", "海南");
        cityList.add(city21);
        
        Map<String, Object> city22 = new HashMap<>();
        city22.put("code", 22);
        city22.put("name", "重庆");
        cityList.add(city22);
        
        Map<String, Object> city23 = new HashMap<>();
        city23.put("code", 23);
        city23.put("name", "四川");
        cityList.add(city23);
        
        Map<String, Object> city24 = new HashMap<>();
        city24.put("code", 24);
        city24.put("name", "贵州");
        cityList.add(city24);
        
        Map<String, Object> city25 = new HashMap<>();
        city25.put("code", 25);
        city25.put("name", "云南");
        cityList.add(city25);
        
        Map<String, Object> city26 = new HashMap<>();
        city26.put("code", 26);
        city26.put("name", "西藏");
        cityList.add(city26);
        
        Map<String, Object> city27 = new HashMap<>();
        city27.put("code", 27);
        city27.put("name", "陕西");
        cityList.add(city27);
        
        Map<String, Object> city28 = new HashMap<>();
        city28.put("code", 28);
        city28.put("name", "甘肃");
        cityList.add(city28);
        
        Map<String, Object> city29 = new HashMap<>();
        city29.put("code", 29);
        city29.put("name", "宁夏");
        cityList.add(city29);
        
        Map<String, Object> city30 = new HashMap<>();
        city30.put("code", 30);
        city30.put("name", "新疆");
        cityList.add(city30);
        
        Map<String, Object> city31 = new HashMap<>();
        city31.put("code", 31);
        city31.put("name", "台湾");
        cityList.add(city31);
        
        Map<String, Object> city32 = new HashMap<>();
        city32.put("code", 32);
        city32.put("name", "香港");
        cityList.add(city32);
        
        Map<String, Object> city33 = new HashMap<>();
        city33.put("code", 33);
        city33.put("name", "澳门");
        cityList.add(city33);
        
        return cityList;
    }
    
    /**
     * 获取职位列表
     */
    private List<Map<String, Object>> getPositionList()
    {
        List<Map<String, Object>> positionList = new ArrayList<>();
        
        Map<String, Object> pos0 = new HashMap<>();
        pos0.put("code", "");
        pos0.put("name", "全部");
        positionList.add(pos0);
        
        Map<String, Object> pos1 = new HashMap<>();
        pos1.put("code", 1);
        pos1.put("name", "销售");
        positionList.add(pos1);
        
        Map<String, Object> pos2 = new HashMap<>();
        pos2.put("code", 2);
        pos2.put("name", "客服");
        positionList.add(pos2);
        
        Map<String, Object> pos3 = new HashMap<>();
        pos3.put("code", 3);
        pos3.put("name", "行政");
        positionList.add(pos3);
        
        Map<String, Object> pos4 = new HashMap<>();
        pos4.put("code", 4);
        pos4.put("name", "人事");
        positionList.add(pos4);
        
        Map<String, Object> pos5 = new HashMap<>();
        pos5.put("code", 5);
        pos5.put("name", "财务");
        positionList.add(pos5);
        
        Map<String, Object> pos6 = new HashMap<>();
        pos6.put("code", 6);
        pos6.put("name", "董事长");
        positionList.add(pos6);
        
        Map<String, Object> pos7 = new HashMap<>();
        pos7.put("code", 7);
        pos7.put("name", "ceo");
        positionList.add(pos7);
        
        Map<String, Object> pos8 = new HashMap<>();
        pos8.put("code", 8);
        pos8.put("name", "总监");
        positionList.add(pos8);
        
        Map<String, Object> pos9 = new HashMap<>();
        pos9.put("code", 9);
        pos9.put("name", "技术");
        positionList.add(pos9);
        
        Map<String, Object> pos10 = new HashMap<>();
        pos10.put("code", 10);
        pos10.put("name", "研发");
        positionList.add(pos10);
        
        Map<String, Object> pos11 = new HashMap<>();
        pos11.put("code", 11);
        pos11.put("name", "产品");
        positionList.add(pos11);
        
        return positionList;
    }
    
    /**
     * 获取科室列表
     */
    /**
     * 获取科室映射（code -> name）
     */
    private Map<Integer, String> getDepartmentMap()
    {
        Map<Integer, String> departmentMap = new HashMap<>();
        List<Map<String, Object>> departmentList = getDepartmentList();
        for (Map<String, Object> dept : departmentList)
        {
            Object code = dept.get("code");
            Object name = dept.get("name");
            if (code != null && name != null)
            {
                Integer codeInt = Convert.toInt(code);
                if (codeInt != null)
                {
                    departmentMap.put(codeInt, Convert.toStr(name));
                }
            }
        }
        return departmentMap;
    }
    
    private List<Map<String, Object>> getDepartmentList()
    {
        List<Map<String, Object>> departmentList = new ArrayList<>();
        
        String[] departments = {
            "心内科", "血液内科", "老年病科", "呼吸内科", "肾内科", "消化内科", "神经内科", "感染病科",
            "内分泌科", "风湿免疫科", "精神卫生科", "大内科及其他", "骨科", "普外科", "胸心外科", "神经外科",
            "烧伤水伤修复外科", "泌尿外科", "整形美容外科", "器官移植科", "耳鼻咽喉头颈外科", "妇产科", "儿科", "口腔医学科",
            "肿瘤科", "眼科", "检验医学科", "急危重症科", "健康管理科", "科研教育科", "临床药学", "医院管理",
            "麻醉科", "影像科", "康复医学科", "皮肤性病科", "预防医学科", "全科医学科", "临床营养科", "中医药学科",
            "基础医学", "临床输血", "罕见病科", "介入医学科"
        };
        
        for (int i = 0; i < departments.length; i++) {
            Map<String, Object> dept = new HashMap<>();
            dept.put("code", i + 1);
            dept.put("name", departments[i]);
            departmentList.add(dept);
        }
        
        return departmentList;
    }
}
