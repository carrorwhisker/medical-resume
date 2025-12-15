package cn.project.web.controller.medical;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.core.domain.AjaxResult;
import cn.project.common.core.page.TableDataInfo;
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.common.utils.TokenUtils;
import cn.project.common.annotation.Log;
import cn.project.common.enums.BusinessType;
import cn.project.common.core.text.Convert;
import cn.project.system.domain.medical.MedicalCard;
import cn.project.system.service.medical.IMedicalCardService;
import cn.project.system.domain.medical.MedicalExchange;
import cn.project.system.service.medical.IMedicalExchangeService;
import cn.project.system.domain.medical.MedicalCollect;
import cn.project.system.service.medical.IMedicalCollectService;

/**
 * 医简历卡片列表控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/Card/CardList")
public class CardListController extends BaseController
{
    @Autowired
    private IMedicalCardService cardService;
    @Autowired
    private IMedicalExchangeService exchangeService;
    @Autowired
    private IMedicalCollectService collectService;
    
    
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
        
        String id = (String) params.get("id");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        // TODO: 实现删除附件逻辑
        
        return AjaxResult.success("删除附件成功！");
    }
    
    /**
     * 获取简历列表（首页）
     */
    @PostMapping("/indexList")
    public TableDataInfo indexList(@RequestHeader(value = "Token", required = false) String token,
                                 @RequestBody Map<String, Object> params)
    {
        String position = (String) params.get("position");
        String city = (String) params.get("city");
        String title = (String) params.get("title");
        
        Integer size = (Integer) params.getOrDefault("size", 20);
        Integer page = (Integer) params.getOrDefault("page", 1);
        
        startPage();
        
        MedicalCard query = new MedicalCard();
        query.setIsStatus(1); // 仅展示审核通过的
        if (StringUtils.isNotEmpty(position))
        {
            query.setPosition(position);
        }
        if (StringUtils.isNotEmpty(city))
        {
            Integer cityCode = Convert.toInt(city, null);
            if (cityCode != null)
            {
                query.setCity(cityCode);
            }
        }
        if (StringUtils.isNotEmpty(title))
        {
            query.setName(title);
        }
        
        List<MedicalCard> list = cardService.selectCardList(query);
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
        Integer type = Convert.toInt(params.get("type"));
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        Long cardId = Convert.toLong(id, null);
        if (cardId == null)
        {
            return AjaxResult.error("参数错误");
        }
        
        // 校验卡片存在且已审核通过
        MedicalCard card = cardService.selectCardById(cardId);
        if (card == null || card.getIsStatus() == null || card.getIsStatus() != 1)
        {
            return AjaxResult.error("简历不存在或未通过审核");
        }
        
        if (type != null && type == 1)
        {
            // 收藏
            MedicalCollect exists = collectService.selectCollectByUidAndCardId(uid, cardId);
            if (exists != null)
            {
                return AjaxResult.success("success"); // 已收藏视为成功
            }
            MedicalCollect collect = new MedicalCollect();
            collect.setUid(uid);
            collect.setCardId(cardId);
            collect.setCreateTimeStamp(System.currentTimeMillis() / 1000);
            int rows = collectService.insertCollect(collect);
            return rows > 0 ? AjaxResult.success("success") : AjaxResult.error("收藏失败");
        }
        else if (type != null && type == 2)
        {
            // 取消收藏
            collectService.deleteCollectByUidAndCardId(uid, cardId);
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
        
        String uid = TokenUtils.getUidFromToken(token);
        
        String id = (String) params.get("id");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        Long cardId = Convert.toLong(id, null);
        if (cardId == null)
        {
            return AjaxResult.error("参数错误");
        }
        
        MedicalCard card = cardService.selectCardById(cardId);
        if (card == null || card.getIsStatus() == null || card.getIsStatus() != 1)
        {
            return AjaxResult.error("暂无数据");
        }
        
        // 浏览次数 +1
        cardService.incrementViewNum(cardId);
        // 同步增加返回值中的 view_num（若为空则视为0）
        int currentView = (card.getViewNum() == null ? 0 : card.getViewNum()) + 1;
        
        // 是否已收藏
        MedicalCollect collect = collectService.selectCollectByUidAndCardId(uid, cardId);
        int isCollect = (collect != null) ? 1 : 0;
        
        Map<Integer, String> departmentMap = getDepartmentMap();
        
        // 返回Map，附加 is_collect
        Map<String, Object> result = new HashMap<>();
        result.put("id", card.getId());
        result.put("uid", card.getUid());
        result.put("img", card.getImg());
        result.put("img_yt", card.getImgYt());
        result.put("name", card.getName());
        result.put("company", card.getCompany());
        result.put("position", card.getPosition());
        result.put("department", card.getDepartment());
        result.put("department_name", departmentMap.get(card.getDepartment()));
        result.put("phone", card.getPhone());
        result.put("email", card.getEmail());
        result.put("address", card.getAddress());
        result.put("wechat", card.getWechat());
        result.put("city", card.getCity());
        result.put("content", card.getContent());
        result.put("attachment", card.getAttachment());
        result.put("attachment_name", card.getAttachmentName());
        result.put("create_time", card.getCreateTimeStamp());
        result.put("update_time", card.getUpdateTimeStamp());
        result.put("is_vip", card.getIsVip());
        result.put("is_status", card.getIsStatus());
        result.put("is_collect", isCollect);
        result.put("view_num", currentView);
        
        return AjaxResult.success("success", result);
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
        
        String uid = TokenUtils.getUidFromToken(token);
        
        String oldCardIdStr = Convert.toStr(params.get("old_card_id"));
        String exchangeCardIdStr = Convert.toStr(params.get("exchange_card_id"));
        
        if (StringUtils.isEmpty(oldCardIdStr))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        Long oldCardId = Convert.toLong(oldCardIdStr, null);
        Long exchangeCardId = Convert.toLong(exchangeCardIdStr, null);
        if (oldCardId == null)
        {
            return AjaxResult.error("简历ID无效");
        }
        
        // 校验对方简历
        MedicalCard oldCard = cardService.selectCardById(oldCardId);
        if (oldCard == null || oldCard.getIsStatus() == null || oldCard.getIsStatus() != 1)
        {
            return AjaxResult.error("对方简历在审核中");
        }
        String oldUid = oldCard.getUid();
        if (uid != null && uid.equals(oldUid))
        {
            return AjaxResult.error("不能与自己交换简历");
        }
        
        // 校验自己是否有审核通过的简历（优先使用传入的exchange_card_id）
        MedicalCard myCard = null;
        if (exchangeCardId != null)
        {
            MedicalCard tmp = cardService.selectCardById(exchangeCardId);
            if (tmp != null && uid.equals(tmp.getUid()) && tmp.getIsStatus() != null && tmp.getIsStatus() == 1)
            {
                myCard = tmp;
            }
        }
        if (myCard == null)
        {
            MedicalCard query = new MedicalCard();
            query.setUid(uid);
            query.setIsStatus(1);
            List<MedicalCard> myCards = cardService.selectCardList(query);
            if (myCards != null && !myCards.isEmpty())
            {
                myCard = myCards.get(0);
                if (exchangeCardId == null)
                {
                    exchangeCardId = myCard.getId();
                }
            }
        }
        if (myCard == null)
        {
            return AjaxResult.error("请先创建简历并通过审核后再交换");
        }
        
        // 防重复
        MedicalExchange exists = exchangeService.selectExchangeByKeys(exchangeCardId, uid, oldUid, oldCardId);
        if (exists != null)
        {
            return AjaxResult.error("您已交换过该简历，请勿重复操作");
        }
        
        Long now = System.currentTimeMillis() / 1000;
        // 记录1：我向对方发起（待处理）
        MedicalExchange first = new MedicalExchange();
        first.setExchangeCardId(exchangeCardId);
        first.setExchangeCardUid(uid);
        first.setUid(oldUid);
        first.setUidCardId(oldCardId);
        first.setCreateTimeStamp(now);
        first.setStatus(1);
        exchangeService.insertExchange(first);
        
        // 记录2：对方向我（已同意）
        MedicalExchange second = new MedicalExchange();
        second.setExchangeCardId(oldCardId);
        second.setExchangeCardUid(oldUid);
        second.setUid(uid);
        second.setUidCardId(exchangeCardId);
        second.setCreateTimeStamp(now);
        second.setStatus(2);
        exchangeService.insertExchange(second);
        
        return AjaxResult.success("success");
    }
    
    /**
     * 首页卡片列表
     */
    @PostMapping("/homeCardList")
    public AjaxResult homeCardList(@RequestHeader(value = "Token", required = false) String token)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        // 1) 我的审核通过简历（按更新时间/创建时间倒序）
        MedicalCard cardQuery = new MedicalCard();
        cardQuery.setUid(uid);
        cardQuery.setIsStatus(1); // 已审核通过
        startPage();
        List<MedicalCard> myCards = cardService.selectCardList(cardQuery);
        
        // 2) 我的交换请求（作为被交换方，状态=1待处理）
        MedicalExchange exchangeQuery = new MedicalExchange();
        exchangeQuery.setUid(uid);          // 对方是我
        exchangeQuery.setStatus(1);         // 待处理
        List<MedicalExchange> exchangeList = exchangeService.selectExchangeList(exchangeQuery);
        
        // 3) 收藏数
        MedicalCollect collectQuery = new MedicalCollect();
        collectQuery.setUid(uid);
        List<MedicalCollect> collects = collectService.selectCollectList(collectQuery);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("userViewNum", 0);                 // 暂无浏览数据来源，先置0
        stats.put("userExchange", exchangeList.size());
        stats.put("userCollect", collects.size());
        
        // 我的收藏列表（取收藏记录对应的已审核通过简历）
        List<Map<String, Object>> myCollectCards = new ArrayList<>();
        Map<Integer, String> departmentMap = getDepartmentMap();
        if (collects != null && !collects.isEmpty())
        {
            for (MedicalCollect c : collects)
            {
                if (c.getCardId() == null) continue;
                MedicalCard mc = cardService.selectCardById(c.getCardId());
                if (mc == null || mc.getIsStatus() == null || mc.getIsStatus() != 1) continue;
                Map<String, Object> m = new HashMap<>();
                m.put("id", mc.getId());
                m.put("uid", mc.getUid());
                m.put("img", mc.getImg());
                m.put("img_yt", mc.getImgYt());
                m.put("name", mc.getName());
                m.put("company", mc.getCompany());
                m.put("position", mc.getPosition());
                m.put("department", mc.getDepartment());
                m.put("department_name", departmentMap.get(mc.getDepartment()));
                m.put("phone", mc.getPhone());
                m.put("email", mc.getEmail());
                m.put("address", mc.getAddress());
                m.put("wechat", mc.getWechat());
                m.put("city", mc.getCity());
                m.put("content", mc.getContent());
                m.put("attachment", mc.getAttachment());
                m.put("attachment_name", mc.getAttachmentName());
                m.put("create_time", mc.getCreateTimeStamp());
                m.put("update_time", mc.getUpdateTimeStamp());
                m.put("is_vip", mc.getIsVip());
                m.put("is_status", mc.getIsStatus());
                m.put("is_collect", 1);
                myCollectCards.add(m);
            }
        }
        
        // 交换列表补充卡片展示信息（参考PHP：name/img/company/department/exchange_time）
        List<Map<String, Object>> exchangeViewList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (MedicalExchange ex : exchangeList)
        {
            if (ex.getExchangeCardId() == null) continue;
            MedicalCard reqCard = cardService.selectCardById(ex.getExchangeCardId());
            if (reqCard == null) continue;
            Map<String, Object> m = new HashMap<>();
            m.put("id", ex.getId());
            m.put("exchange_card_id", ex.getExchangeCardId());
            m.put("exchange_card_uid", ex.getExchangeCardUid());
            m.put("uid", ex.getUid());
            m.put("uid_card_id", ex.getUidCardId());
            m.put("status", ex.getStatus());
            m.put("create_time_stamp", ex.getCreateTimeStamp());
            if (ex.getCreateTimeStamp() != null)
            {
                m.put("exchange_time", sdf.format(new Date(ex.getCreateTimeStamp() * 1000)));
            }
            m.put("name", reqCard.getName());
            m.put("img", reqCard.getImg());
            m.put("company", reqCard.getCompany());
            m.put("position", reqCard.getPosition());
            m.put("department", reqCard.getDepartment());
            m.put("department_name", departmentMap.get(reqCard.getDepartment()));
            exchangeViewList.add(m);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("myselfCardNum", stats);
        result.put("myselfNewCard", myCollectCards);       // 我的收藏列表
        result.put("myselfExchangeList", exchangeViewList);    // 最新交换记录（附加名片信息）
        
        return AjaxResult.success("success", result);
    }
    
    /**
     * 保存交换状态
     */
    @PostMapping("/departmentCardSave")
    public AjaxResult departmentCardSave(@RequestHeader(value = "Token", required = false) String token,
                                       @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        
        String uid = TokenUtils.getUidFromToken(token);
        String idStr = Convert.toStr(params.get("id"));
        Integer type = Convert.toInt(params.get("type"));
        
        if (StringUtils.isEmpty(idStr) || type == null)
        {
            return AjaxResult.error("请重新登录&检查参数");
        }
        Long id = Convert.toLong(idStr, null);
        if (id == null)
        {
            return AjaxResult.error("参数错误");
        }
        
        MedicalExchange exchange = exchangeService.selectExchangeById(id);
        if (exchange == null)
        {
            return AjaxResult.error("记录不存在");
        }
        
        // 只有被交换方(uid)才可以处理，且状态=1待处理
        if (!uid.equals(exchange.getUid()) || exchange.getStatus() == null || exchange.getStatus() != 1)
        {
            return AjaxResult.error("无权限或状态已变更");
        }
        
        // type: 1忽略 -> 状态置3(拒绝)，2同意 -> 状态置2
        int newStatus = (type == 2) ? 2 : 3;
        exchange.setStatus(newStatus);
        exchangeService.updateExchange(exchange);
        
        // 同步更新另一条对应记录（对方视角：exchange_card_uid = 申请人，uid = 当前处理人，uid_card_id 与 exchange_card_id 对调）
        MedicalExchange other = exchangeService.selectExchangeByKeys(
            exchange.getUidCardId(),   // 对方卡id
            exchange.getUid(),         // 对方作为申请人
            exchange.getExchangeCardUid(), // 我
            exchange.getExchangeCardId()   // 我的卡
        );
        if (other != null)
        {
            other.setStatus(newStatus);
            exchangeService.updateExchange(other);
        }
        
        return AjaxResult.success("success");
    }
    
    /**
     * 获取用户卡片列表
     */
    @PostMapping("/getUserCardList")
    public TableDataInfo getUserCardList(@RequestHeader(value = "Token", required = false) String token,
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
        String status = (String) params.get("status");
        
        startPage();
        
        // TODO: 实现获取用户卡片列表逻辑
        
        List<Map<String, Object>> list = new ArrayList<>(); // 空列表示例
        
        return getDataTable(list);
    }
    
    /**
     * 科室映射
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
