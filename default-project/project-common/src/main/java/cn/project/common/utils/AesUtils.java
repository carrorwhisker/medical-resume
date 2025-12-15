package cn.project.common.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES加密解密工具类
 * 对应PHP的AES-128-CBC加密方式
 * 
 * @author medical
 */
public class AesUtils
{
    private static final Logger log = LoggerFactory.getLogger(AesUtils.class);
    
    // AES密钥和IV（与PHP配置保持一致）
    private static final String AES_KEY = "7ois37t5enevenoqpi1bvvokvgk2amef";
    private static final String AES_IV = "51n5er56a3hierj7";
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    
    /**
     * AES加密
     * 
     * @param plainText 明文
     * @return Base64编码的密文
     */
    public static String encrypt(String plainText)
    {
        try
        {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes("UTF-8"), ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes("UTF-8"));
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        }
        catch (Exception e)
        {
            log.error("AES加密失败", e);
            return null;
        }
    }
    
    /**
     * AES解密
     * 
     * @param encryptedText Base64编码的密文
     * @return 明文
     */
    public static String decrypt(String encryptedText)
    {
        try
        {
            SecretKeySpec secretKey = new SecretKeySpec(AES_KEY.getBytes("UTF-8"), ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(AES_IV.getBytes("UTF-8"));
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decoded);
            
            return new String(decrypted, "UTF-8");
        }
        catch (Exception e)
        {
            log.error("AES解密失败", e);
            return null;
        }
    }
}
