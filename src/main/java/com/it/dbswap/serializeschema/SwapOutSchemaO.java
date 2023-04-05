package com.it.dbswap.serializeschema;

import com.it.dbswap.util.proputil.KafkaPropertiesUtil;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * Created by
 *
 * @author liangzhibiao
 * @date 2019/10/31 18:29
 */
public class SwapOutSchemaO implements KeyedSerializationSchema<Tuple2<String, byte[]>> {

    private static final Logger logger = LoggerFactory.getLogger(SwapOutSchemaO.class);



    @Override
    public byte[] serializeKey(Tuple2<String, byte[]> element) {
        if (null != element && null != element.f0) {
            return element.f0.getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public byte[] serializeValue(Tuple2<String, byte[]> element) {
        if (null != element && null != element.f1) {
            return element.f1;
        }
        return null;
    }

    @Override
    public String getTargetTopic(Tuple2<String, byte[]> element) {
        return KafkaPropertiesUtil.getProperty("swap_out_kafka_topic");
    }
}
