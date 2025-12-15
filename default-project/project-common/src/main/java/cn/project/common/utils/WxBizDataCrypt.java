package cn.project.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信小程序数据解密工具类
 * 对应PHP的WXBizDataCrypt类
 * 
 * @author medical
 */
public class WxBizDataCrypt
{
    private static final Logger log = LoggerFactory.getLogger(WxBizDataCrypt.class);
    
    /**
     * 检验数据的真实性，并且获取解密后的明文
     * 
     * @param appId 小程序AppID
     * @param sessionKey 会话密钥
     * @param encryptedData 加密的用户数据
     * @param iv 与用户数据一同返回的初始向量
     * @return 解密后的JSON字符串，失败返回null
     */
    public static String decryptData(String appId, String sessionKey, String encryptedData, String iv)
    {
        try
        {
            // 验证sessionKey长度（Base64解码后应该是16字节）
            byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
            if (sessionKeyBytes.length != 16)
            {
                log.error("sessionKey长度不正确");
                return null;
            }
            
            // 验证iv长度
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            if (ivBytes.length != 16)
            {
                log.error("iv长度不正确");
                return null;
            }
            
            // Base64解码加密数据
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
            
            // AES-128-CBC解密
            SecretKeySpec secretKey = new SecretKeySpec(sessionKeyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            
            byte[] decrypted = cipher.doFinal(encryptedBytes);
            String result = new String(decrypted, "UTF-8");
            
            // 验证appid
            JSONObject dataObj = JSON.parseObject(result);
            JSONObject watermark = dataObj.getJSONObject("watermark");
            if (watermark == null || !appId.equals(watermark.getString("appid")))
            {
                log.error("appid验证失败");
                return null;
            }
            
            return result;
        }
        catch (Exception e)
        {
            log.error("微信数据解密失败", e);
            return null;
        }
    }
}
