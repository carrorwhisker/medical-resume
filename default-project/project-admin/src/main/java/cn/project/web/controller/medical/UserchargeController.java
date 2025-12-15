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
import cn.project.common.utils.SecurityUtils;
import cn.project.common.utils.StringUtils;
import cn.project.common.utils.TokenUtils;
import cn.project.common.core.text.Convert;
import cn.project.common.annotation.Log;
import cn.project.common.enums.BusinessType;
import cn.project.system.service.medical.IMedicalRechargeService;
import cn.project.system.service.medical.IMedicalUserService;
import cn.project.system.domain.medical.MedicalRecharge;
import cn.project.system.domain.medical.MedicalUser;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 医简历用户充值控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/User/Usercharge")
public class UserchargeController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(UserchargeController.class);
    
    @Autowired
    private WxPayService wxPayService;
    
    @Autowired
    private IMedicalRechargeService rechargeService;
    
    @Autowired
    private IMedicalUserService userService;
    
    /**
     * 会议支付
     */
    @PostMapping("/meetingPay")
    public AjaxResult meetingPay(@RequestHeader(value = "Token", required = false) String token,
                               @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        String activeId = (String) params.get("active_id");
        String id = (String) params.get("id");
        
        if (StringUtils.isEmpty(id))
        {
            return AjaxResult.error("param is falid");
        }
        
        // TODO: 实现会议支付逻辑
        // 1. 查询会议价格信息
        // 2. 如果是免费会议，直接返回成功
        // 3. 生成订单号
        // 4. 调用微信支付
        // 5. 返回支付信息
        
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("notify_url", "/Main/action/User/Notify/notify_return");
        responseData.put("app_response", new HashMap<>());
        
        return AjaxResult.success("success", responseData);
    }
    
    /**
     * VIP充值支付
     */
    @PostMapping("/rechargePay")
    public AjaxResult rechargePay(@RequestHeader(value = "Token", required = false) String token,
                                @RequestBody Map<String, Object> params)
    {
        if (StringUtils.isEmpty(token))
        {
            return AjaxResult.error("请登录");
        }
        
        // 解密token获取uid
        String uid = TokenUtils.getUidFromToken(token);
        
        Integer type = Convert.toInt(params.get("type"), 1);
        
        if (type == null)
        {
            return AjaxResult.error("param is falid");
        }
        
        // 根据类型确定金额
        double typeMoney = 0;
        if (type == 1)
        {
            typeMoney = 19;
        }
        else if (type == 2)
        {
            typeMoney = 49;
        }
        else if (type == 3)
        {
            typeMoney = 188;
        }
        else
        {
            return AjaxResult.error("交易失败");
        }
        
        try
        {
            // 生成订单号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateStr = sdf.format(new Date());
            long timestamp = System.currentTimeMillis();
            String orderNum = "TXZ" + dateStr.substring(0, 8) + String.valueOf(timestamp).substring(8);
            String outTradeNo = dateStr + String.valueOf(timestamp % 100000) + String.valueOf((int)(Math.random() * 100));
            
            // 查询用户openid
            MedicalUser user = userService.selectUserByUid(uid);
            if (user == null || StringUtils.isEmpty(user.getOpenid()))
            {
                return AjaxResult.error("用户信息错误");
            }
            
            // 创建充值订单
            MedicalRecharge recharge = new MedicalRecharge();
            recharge.setUid(uid);
            recharge.setMoney(typeMoney);
            recharge.setPayMoney((int)(typeMoney * 100)); // 转换为分
            recharge.setOutTradeNo(outTradeNo);
            recharge.setOrderNum(orderNum);
            recharge.setStatus(1); // 待支付
            recharge.setType(type);
            recharge.setIsPayStatus(1); // 充值
            recharge.setOrderTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            rechargeService.insertRecharge(recharge);
            
            // 构建微信统一下单请求
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setBody("会员充值");
            orderRequest.setOutTradeNo(outTradeNo);
            orderRequest.setTotalFee((int)(typeMoney * 100)); // 金额单位：分
            orderRequest.setSpbillCreateIp("127.0.0.1");
            orderRequest.setNotifyUrl(wxPayService.getConfig().getNotifyUrl());
            orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
            orderRequest.setOpenid(user.getOpenid());
            
            // 设置订单过期时间（1天后）
            SimpleDateFormat expireSdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Date expireDate = new Date(System.currentTimeMillis() + 86400000);
            orderRequest.setTimeExpire(expireSdf.format(expireDate));
            
            // 调用统一下单接口
            WxPayUnifiedOrderResult unifiedOrderResult = wxPayService.unifiedOrder(orderRequest);
            
            // 生成小程序支付参数
            WxPayMpOrderResult mpOrderResult = wxPayService.createOrder(orderRequest);
            
            // 构造返回数据
            Map<String, Object> appResponse = new HashMap<>();
            appResponse.put("timeStamp", mpOrderResult.getTimeStamp());
            appResponse.put("nonceStr", mpOrderResult.getNonceStr());
            appResponse.put("package", mpOrderResult.getPackageValue());
            appResponse.put("signType", mpOrderResult.getSignType());
            appResponse.put("paySign", mpOrderResult.getPaySign());
            appResponse.put("appId", mpOrderResult.getAppId());
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("notify_url", wxPayService.getConfig().getNotifyUrl());
            responseData.put("app_response", appResponse);
            
            return AjaxResult.success("success", responseData);
        }
        catch (WxPayException e)
        {
            log.error("微信支付统一下单失败", e);
            return AjaxResult.error("支付失败：" + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("充值支付异常", e);
            return AjaxResult.error("系统异常");
        }
    }
}
