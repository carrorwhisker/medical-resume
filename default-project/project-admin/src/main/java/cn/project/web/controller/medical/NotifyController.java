package cn.project.web.controller.medical;

import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.project.common.core.controller.BaseController;
import cn.project.common.utils.StringUtils;
import cn.project.system.service.medical.IMedicalRechargeService;
import cn.project.system.service.medical.IMedicalUserService;
import cn.project.system.domain.medical.MedicalRecharge;
import cn.project.system.domain.medical.MedicalUser;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信支付回调控制器
 * 
 * @author medical
 */
@RestController
@RequestMapping("/Main/action/User/Notify")
public class NotifyController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(NotifyController.class);
    
    @Autowired
    private WxPayService wxPayService;
    
    @Autowired
    private IMedicalRechargeService rechargeService;
    
    @Autowired
    private IMedicalUserService userService;
    
    /**
     * 微信支付回调通知
     */
    @PostMapping("/notify_return")
    public String notifyReturn(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            // 获取微信回调数据
            String xmlData = getRequestBody(request);
            
            if (StringUtils.isEmpty(xmlData))
            {
                log.error("微信支付回调数据为空");
                return WxPayNotifyResponse.fail("回调数据为空");
            }
            
            log.info("收到微信支付回调：{}", xmlData);
            
            // 解析并验证签名
            WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            
            // 验证通知结果
            if (!"SUCCESS".equals(notifyResult.getReturnCode()) || 
                !"SUCCESS".equals(notifyResult.getResultCode()))
            {
                log.error("微信支付回调失败：returnCode={}, resultCode={}", 
                    notifyResult.getReturnCode(), notifyResult.getResultCode());
                return WxPayNotifyResponse.fail("支付失败");
            }
            
            // 获取订单号
            String outTradeNo = notifyResult.getOutTradeNo();
            
            if (StringUtils.isEmpty(outTradeNo))
            {
                log.error("订单号为空");
                return WxPayNotifyResponse.fail("订单号为空");
            }
            
            // 查询订单
            MedicalRecharge recharge = rechargeService.selectRechargeByOutTradeNo(outTradeNo);
            
            if (recharge == null)
            {
                log.error("订单不存在：{}", outTradeNo);
                return WxPayNotifyResponse.fail("订单不存在");
            }
            
            // 检查订单状态，避免重复处理
            if (recharge.getStatus() != null && recharge.getStatus() == 2)
            {
                log.info("订单已处理：{}", outTradeNo);
                return WxPayNotifyResponse.success("SUCCESS");
            }
            
            // 更新订单状态
            recharge.setStatus(2); // 已支付
            rechargeService.updateRecharge(recharge);
            
            // 处理业务逻辑
            if (recharge.getIsPayStatus() != null && recharge.getIsPayStatus() == 1)
            {
                // 充值VIP
                handleVipRecharge(recharge);
            }
            else if (recharge.getIsPayStatus() != null && recharge.getIsPayStatus() == 2)
            {
                // 会议支付
                handleMeetingPay(recharge);
            }
            
            log.info("微信支付回调处理成功：{}", outTradeNo);
            
            // 返回成功响应
            return WxPayNotifyResponse.success("SUCCESS");
        }
        catch (WxPayException e)
        {
            log.error("微信支付回调异常", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
        catch (Exception e)
        {
            log.error("处理微信支付回调异常", e);
            return WxPayNotifyResponse.fail("系统异常");
        }
    }
    
    /**
     * 处理VIP充值
     */
    private void handleVipRecharge(MedicalRecharge recharge)
    {
        try
        {
            String uid = recharge.getUid();
            Integer type = recharge.getType();
            
            if (StringUtils.isEmpty(uid) || type == null)
            {
                log.error("充值参数错误：uid={}, type={}", uid, type);
                return;
            }
            
            // 查询用户
            MedicalUser user = userService.selectUserByUid(uid);
            
            if (user == null)
            {
                log.error("用户不存在：{}", uid);
                return;
            }
            
            // 计算VIP到期时间
            long currentTime = System.currentTimeMillis() / 1000;
            long vipTime = user.getVipTime() != null ? user.getVipTime() : currentTime;
            
            // 如果VIP已过期，从当前时间开始计算
            if (vipTime < currentTime)
            {
                vipTime = currentTime;
            }
            
            // 根据充值类型增加VIP时长
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(vipTime * 1000);
            
            if (type == 1)
            {
                // 月会员：增加1个月
                calendar.add(Calendar.MONTH, 1);
            }
            else if (type == 2)
            {
                // 季会员：增加3个月
                calendar.add(Calendar.MONTH, 3);
            }
            else if (type == 3)
            {
                // 年会员：增加12个月
                calendar.add(Calendar.YEAR, 1);
            }
            
            long newVipTime = calendar.getTimeInMillis() / 1000;
            
            // 更新用户VIP状态
            user.setIsVip(2); // 设置为VIP
            user.setVipTime(newVipTime);
            userService.updateUser(user);
            
            log.info("VIP充值成功：uid={}, type={}, vipTime={}", uid, type, newVipTime);
        }
        catch (Exception e)
        {
            log.error("处理VIP充值异常", e);
        }
    }
    
    /**
     * 处理会议支付
     */
    private void handleMeetingPay(MedicalRecharge recharge)
    {
        try
        {
            String uid = recharge.getUid();
            Long activeId = recharge.getActiveId();
            
            if (StringUtils.isEmpty(uid) || activeId == null)
            {
                log.error("会议支付参数错误：uid={}, activeId={}", uid, activeId);
                return;
            }
            
            // TODO: 实现会议支付逻辑
            // 1. 更新会议报名状态
            // 2. 发送通知等
            
            log.info("会议支付成功：uid={}, activeId={}", uid, activeId);
        }
        catch (Exception e)
        {
            log.error("处理会议支付异常", e);
        }
    }
    
    /**
     * 获取请求体内容
     */
    private String getRequestBody(HttpServletRequest request)
    {
        try
        {
            StringBuilder sb = new StringBuilder();
            java.io.BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            log.error("读取请求体异常", e);
            return null;
        }
    }
}

