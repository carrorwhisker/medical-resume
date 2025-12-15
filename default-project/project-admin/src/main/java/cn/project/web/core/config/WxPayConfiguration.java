package cn.project.web.core.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付配置类
 * 
 * @author medical
 */
@Configuration
public class WxPayConfiguration
{
    @Value("${wx.pay.appId}")
    private String appId;
    
    @Value("${wx.pay.mchId}")
    private String mchId;
    
    @Value("${wx.pay.mchKey}")
    private String mchKey;
    
    @Value("${wx.pay.notifyUrl}")
    private String notifyUrl;
    
    @Value("${wx.pay.useSandboxEnv:false}")
    private boolean useSandboxEnv;
    
    @Bean
    public WxPayService wxPayService()
    {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
        payConfig.setNotifyUrl(notifyUrl);
        payConfig.setUseSandboxEnv(useSandboxEnv);
        
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        
        return wxPayService;
    }
}

