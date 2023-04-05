package com.it.dbswap.serializeschema;

import com.it.dbswap.util.proputil.KafkaPropertiesUtil;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.kafka.shaded.org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
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
public class SwapOutSchema implements KeyedSerializationSchema<Tuple2<String, byte[]>>, KafkaDeserializationSchema<Tuple2<String, byte[]>> {

    private static final Logger logger = LoggerFactory.getLogger(SwapOutSchema.class);

    @Override
    public boolean isEndOfStream(Tuple2<String, byte[]> nextElement) {
        return false;
    }

    @Override
    public Tuple2<String, byte[]> deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        try {
            String key = null;
            if (null != record.key()) {
                key = new String(record.key(), StandardCharsets.UTF_8);
            }
            return new Tuple2<>(key, record.value());
        } catch (Exception e) {
            logger.error("orderbasedata==== {}",e);
            return null;
        }
    }


    @Override
    public TypeInformation<Tuple2<String, byte[]>> getProducedType() {
        return TypeInformation.of(new TypeHint<Tuple2<String, byte[]>>() {
            @Override
            public TypeInformation<Tuple2<String, byte[]>> getTypeInfo() {
                return super.getTypeInfo();
            }
        });
    }


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
