package com.it.dbswap.serializeschema;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.kafka.shaded.org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

//import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class SwapSourceSchema implements KafkaDeserializationSchema<Tuple2<String, byte[]>>, KeyedSerializationSchema<Tuple2<String, byte[]>> {

    private static final Logger logger = LoggerFactory.getLogger(SwapSourceSchema.class);

    private long count;

    @Override
    public boolean isEndOfStream(Tuple2<String, byte[]> nextElement) {
        return false;
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
    public Tuple2<String, byte[]> deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException {

        try {
            String key = null;
            if (null != record.key()) {
                key = new String(record.key(), StandardCharsets.UTF_8);
            }
            return new Tuple2(key, record.value());

        } catch (Exception ex) {
            logger.error("Cannot parse the provided message null", ex);
        }
        return null;
    }


    @Override
    public byte[] serializeKey(Tuple2<String, byte[]> element) {
        return new byte[0];
    }

    @Override
    public byte[] serializeValue(Tuple2<String, byte[]> element) {
        return new byte[0];
    }

    @Override
    public String getTargetTopic(Tuple2<String, byte[]> element) {
        return null;
    }
}
