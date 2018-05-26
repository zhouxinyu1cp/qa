package com.zhou.qa.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by zhouxinyu1cp on 2018/4/22.
 */
public class QaUtil
{
    private static final Logger logger = LoggerFactory.getLogger(QaUtil.class);

    public static int SYSTEM_USER_ID = 1; // 系统管理员id

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }

    // 获取应答返回的Json字符串
    public static String getResponseJsonString(int retCode)
    {
        JSONObject json = new JSONObject();
        json.put("code", retCode);
        return json.toJSONString();
    }

    public static String getResponseJsonString(int retCode, String msg)
    {
        JSONObject json = new JSONObject();
        json.put("code", retCode);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getResponseJsonString(int retCode, Map<String, Object> map)
    {
        JSONObject json = new JSONObject();
        json.put("code", retCode);
        for(Map.Entry<String, Object> entry : map.entrySet())
        {
            json.put(entry.getKey(), entry.getValue());
        }

        return json.toJSONString();
    }
}










