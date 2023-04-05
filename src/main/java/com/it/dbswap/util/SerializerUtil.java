package com.it.dbswap.util;

import com.alibaba.fastjson.JSONObject;

import java.nio.charset.Charset;

public class SerializerUtil {
    public static <T> byte[] serialize(T obj) {

        return JSONObject.toJSONString(obj).getBytes(Charset.defaultCharset());
    }

    public static <T> T deserialize(byte[] paramArrayOfByte,Class<T> targetClass) {
        return JSONObject.parseObject(paramArrayOfByte,targetClass);
    }
}
