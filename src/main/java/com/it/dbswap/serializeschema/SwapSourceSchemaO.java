package com.it.dbswap.serializeschema;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.kafka.shaded.org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class SwapSourceSchemaO implements KafkaDeserializationSchema<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(SwapSourceSchemaO.class);

    private long count;


    @Override
    public TypeInformation<byte[]> getProducedType() {
        return TypeInformation.of(new TypeHint<byte[]>() {
            @Override
            public TypeInformation<byte[]> getTypeInfo() {
                return super.getTypeInfo();
            }
        });
    }



    @Override
    public boolean isEndOfStream(byte[] bytes) {
        return false;
    }

    @Override
    public byte[] deserialize(ConsumerRecord<byte[], byte[]> record) throws IOException {
        try {
            return record.value();
        } catch (Exception ex) {
            logger.error("Cannot parse the provided message null", ex);
        }
        return null;
    }



}
