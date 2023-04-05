package com.it.dbswap.creator;


import com.it.dbswap.serializeschema.SwapOutSchema;
import com.it.dbswap.serializeschema.SwapOutSchemaO;
import com.it.dbswap.serializeschema.SwapSourceSchema;
import com.it.dbswap.serializeschema.SwapSourceSchemaO;
import com.it.dbswap.util.proputil.KafkaPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Properties;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class KafkaCreator {

    private static final Logger logger = LoggerFactory.getLogger(KafkaCreator.class);

    public static FlinkKafkaConsumer<Tuple2<String, byte[]>> getSwapSourceKafkaConsumer(String kafkaStartType,
                                                                                              long timestamp) {
        return new KafkaBuilder<Tuple2<String, byte[]>>()
                .bootstrapServers(KafkaPropertiesUtil.getSwapSourceBroker())
                .zookeeperConnect(KafkaPropertiesUtil.getSwapSourceZookeeper())
                .groupId(KafkaPropertiesUtil.getSwapSourceGroupId())
                .topic(KafkaPropertiesUtil.getSwapSourceTopic())
                .startType(kafkaStartType)
                .timestamp(timestamp)
                .consumeSchema(new SwapSourceSchema())
                .buildConsumer();
    }


    public static FlinkKafkaProducer<Tuple2<String, byte[]>> getSwapOutProducer() {
        return new KafkaBuilder<Tuple2<String, byte[]>>()
                .bootstrapServers(KafkaPropertiesUtil.getProperty("swap_out_kafka_broker"))
                .zookeeperConnect(KafkaPropertiesUtil.getProperty("swap_out_kafka_zookeeper"))
                .topic(KafkaPropertiesUtil.getProperty("swap_out_kafka_topic"))
//                .partitionner(Optional.of(new KafkaProducerKeyHashPartitioner<>()))
                .produceSchema(new SwapOutSchema())
                .buildProducer();
    }


    public static FlinkKafkaConsumer<Tuple2<String, byte[]>> getHBaseSwapSourceKafkaConsumer(String kafkaStartType,
                                                                                        long timestamp) {
        return new KafkaBuilder<Tuple2<String, byte[]>>()
                .bootstrapServers(KafkaPropertiesUtil.getHbaseSwapSourceBroker())
                .zookeeperConnect(KafkaPropertiesUtil.getHbaseSwapSourceZookeeper())
                .groupId(KafkaPropertiesUtil.getHbaseSwapSourceGroupId())
                .topic(KafkaPropertiesUtil.getHbaseSwapSourceTopic())
                .startType(kafkaStartType)
                .timestamp(timestamp)
                .consumeSchema(new SwapSourceSchema())
                .buildConsumer();
    }


    public static FlinkKafkaConsumer<Tuple2<String, byte[]>> getParseResultSourceKafkaConsumer(String kafkaStartType,
                                                                                             long timestamp) {
        return new KafkaBuilder<Tuple2<String, byte[]>>()
                .bootstrapServers(KafkaPropertiesUtil.getParseResultSourceBroker())
                .zookeeperConnect(KafkaPropertiesUtil.getParseResultSourceZookeeper())
                .groupId(KafkaPropertiesUtil.getParseResultSourceGroupId())
                .topic(KafkaPropertiesUtil.getParseResultSourceTopic())
                .startType(kafkaStartType)
                .timestamp(timestamp)
                .consumeSchema(new SwapSourceSchema())
                .buildConsumer();
    }

    public static class KafkaBuilder<T> {
        private Properties properties = new Properties();
        private String topic;
        private KeyedSerializationSchema<T> serializationSchema;
        private Optional<FlinkKafkaPartitioner<T>> flinkKafkaPartitioner;
        public String startType;

        private String producerBatchSize;
        private int producerBufferMemoryKB;
        private long timestamp;
        private KafkaDeserializationSchema<T> deserializationSchema;

        public KafkaBuilder<T> bootstrapServers(String bootstrapServers) {
            properties.setProperty("bootstrap.servers", bootstrapServers);
            return this;
        }


        public KafkaBuilder<T> zookeeperConnect(String zookeeperConnect) {
            properties.setProperty("zookeeper.connect", zookeeperConnect);
            return this;
        }

        public KafkaBuilder<T> fetchMiniBytes(String fetchMiniByte) {
            setPropertiesIfNotBlank("fetch.min.bytes",fetchMiniByte);
            return this;
        }

        public KafkaBuilder<T> fetchWaitMaxMs(String fetchWaitMaxMs) {
            setPropertiesIfNotBlank("fetch.wait.max.ms", fetchWaitMaxMs);
            return this;
        }


        public KafkaBuilder<T> fetchMessageMaxBytes(String fetchMessageMaxBytes) {
            setPropertiesIfNotBlank("fetch.message.max.bytes", fetchMessageMaxBytes);
            return this;
        }

        public KafkaBuilder<T> socketRecieveMessageMaxBytes(String socketRecieveMessageMaxBytes) {
            setPropertiesIfNotBlank("socket.receive.buffer.bytes", socketRecieveMessageMaxBytes);
            return this;
        }

        public KafkaBuilder<T> groupId(String groupid) {
            properties.setProperty("group.id", groupid);
            return this;
        }

        public KafkaBuilder<T> topic(String topic) {
            this.topic = topic;
            return this;
        }
        public KafkaBuilder<T> timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        public KafkaBuilder<T> startType(String startType) {
            this.startType = startType;
            return this;
        }

        public KafkaBuilder<T> consumeSchema(KafkaDeserializationSchema<T> deserializationSchema) {
            this.deserializationSchema = deserializationSchema;
            return this;
        }

        public KafkaBuilder<T> produceSchema(KeyedSerializationSchema<T> serializationSchema) {
            this.serializationSchema = serializationSchema;
            return this;
        }


        public KafkaBuilder<T> partitionner(Optional<FlinkKafkaPartitioner<T>> flinkKafkaPartitioner) {
            this.flinkKafkaPartitioner = flinkKafkaPartitioner;
            return this;
        }

        public KafkaBuilder<T> producerBatchSize(String producerBatchSize) {
            this.producerBatchSize = producerBatchSize;
            return this;
        }

        public KafkaBuilder<T> producerBufferMemoryKB(int producerBufferMemoryKB) {
            this.producerBufferMemoryKB = producerBufferMemoryKB;
            return this;
        }

        public FlinkKafkaConsumer<T> buildConsumer() {
            valid();
            if (deserializationSchema == null) {
                throw new IllegalArgumentException("serializationSchema参数不能为空");
            }
            FlinkKafkaConsumer<T> consumer = new FlinkKafkaConsumer<T>(topic, deserializationSchema, properties);

            String startOffset = null;
            if (StringUtils.isNotBlank(startType)) {
                startOffset = startType.trim();
                logger.info("使用传入startType参数：{}", startType);
            } else {
                startOffset = "earliest";
                logger.info("使用配置文件的参数");
            }
            if(timestamp>0){
                consumer.setStartFromTimestamp(timestamp);
            }else{
                if ("earliest".equalsIgnoreCase(startOffset)) {
                    logger.info("consumer start from earliest");
                    consumer.setStartFromEarliest();
                } else if ("latest".equalsIgnoreCase(startOffset)) {
                    logger.info("consumer start from latest");
                    consumer.setStartFromLatest();
                } else {
                    logger.info("consumer start from group offset");
                    // 默认 使用groupoffset
                    consumer.setStartFromGroupOffsets();
                }
            }
            return consumer;
        }


        public void setPropertiesIfNotBlank(String key,String value){
            if(StringUtils.isNotBlank(value)){
                properties.setProperty(key, value);
            }
        }


        private FlinkKafkaProducer<T> buildProducer() {
            valid();
            if (serializationSchema == null) {
                throw new IllegalArgumentException("参数deserializationSchema不能为空");
            }

            if(StringUtils.isNotBlank(producerBatchSize)) {
                properties.setProperty("batch.size", producerBatchSize);
            } else {
                // 默认值
                properties.setProperty("batch.size", "1000");
            }

            if(producerBufferMemoryKB > 0) {
                properties.setProperty("buffer.memory", String.valueOf(producerBufferMemoryKB * 1024));
            } else {
                // 默认值
                properties.setProperty("buffer.memory", String.valueOf(1024 * 1024));
            }

            if (flinkKafkaPartitioner == null) {
                return new FlinkKafkaProducer<T>(topic, serializationSchema, properties);
            } else {
                return new FlinkKafkaProducer<T>(topic, serializationSchema, properties, flinkKafkaPartitioner);
            }
        }


        private void valid() {
            if (StringUtils.isBlank(properties.getProperty("zookeeper.connect"))) {
                throw new IllegalArgumentException("zookeeperConnect参数不能为空");
            }

            if (StringUtils.isBlank(properties.getProperty("bootstrap.servers"))) {
                throw new IllegalArgumentException("bootstrapServers参数不能为空");
            }

            if (StringUtils.isBlank(topic)) {
                throw new IllegalArgumentException("topic参数不能为空");
            }
        }
    }


}
