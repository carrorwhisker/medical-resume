package cn.project.common.utils;

import java.util.Map;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Token工具类
 * 用于解析和生成医简历系统的Token
 * 
 * @author medical
 */
public class TokenUtils
{
    private static final Logger log = LoggerFactory.getLogger(TokenUtils.class);
    
    /**
     * 从Token中解析UID
     * 
     * @param token AES加密的Token字符串
     * @return UID，如果解析失败返回null
     */
    public static String getUidFromToken(String token)
    {
        if (StringUtils.isEmpty(token))
        {
            return null;
        }
        
        try
        {
            // 解密Token
            String decryptedToken = AesUtils.decrypt(token);
            if (StringUtils.isEmpty(decryptedToken))
            {
                log.warn("Token解密失败");
                return null;
            }
            
            // 解析JSON
            JSONObject tokenObj = JSON.parseObject(decryptedToken);
            String uid = tokenObj.getString("uid");
            
            // 可选：验证time是否过期（这里暂时不验证，保持与PHP一致）
            // Long time = tokenObj.getLong("time");
            // if (time != null && (System.currentTimeMillis() / 1000 - time) > 7200) {
            //     return null; // Token过期（2小时）
            // }
            
            return uid;
        }
        catch (Exception e)
        {
            log.error("Token解析失败", e);
            return null;
        }
    }
    
    /**
     * 生成Token
     * 
     * @param uid 用户UID
     * @return AES加密的Token字符串
     */
    public static String generateToken(String uid)
    {
        try
        {
            JSONObject tokenObj = new JSONObject();
            tokenObj.put("uid", uid);
            tokenObj.put("time", System.currentTimeMillis() / 1000); // 秒级时间戳
            
            String tokenJson = tokenObj.toJSONString();
            return AesUtils.encrypt(tokenJson);
        }
        catch (Exception e)
        {
            log.error("Token生成失败", e);
            return null;
        }
    }
}
