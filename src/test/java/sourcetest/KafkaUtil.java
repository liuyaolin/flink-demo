package sourcetest;

import com.it.dbswap.util.proputil.KafkaPropertiesUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;


public class KafkaUtil implements Serializable {

   public static  String brokers="";
    public static  String inputTopic="";
    public static  String resultTopic="";
    static {
        brokers = KafkaPropertiesUtil.getSwapSourceBroker();
        inputTopic = KafkaPropertiesUtil.getSwapSourceTopic();
        resultTopic = KafkaPropertiesUtil.getProperty("swap_out_kafka_topic");
    }


    private static final Map<String, KafkaProducer<String, String>> producerCache = new ConcurrentHashMap<>();


    /**
     * create producer
     *
     * @param brokers
     * @return
     */
    private static KafkaProducer<String, String> createProducer(String brokers) {
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        prop.put(ProducerConfig.ACKS_CONFIG, "-1");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        prop.put("request.required.acks","0");
        prop.put("batch.size",500);
        prop.put("linger.ms",500);
        prop.put("buffer.memory",33554432);
        return new KafkaProducer<>(prop);
    }


    /**
     * close producer
     */
    static {
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> producerCache.forEach(KafkaUtil::accept)
                )
        );
    }


    public static void inputSend(String msg){
        KafkaProducer<String, String> producer = getProducer(brokers);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(inputTopic, msg);
        producer.send(producerRecord);

    }

    /**
     * 批量发送
     * @param msgs
     */
    public static void timerResultBatchSend(List<String> msgs) {
        KafkaProducer<String, String> producer = getProducer(brokers);
        for(String msg : msgs){
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(resultTopic, msg);
            producer.send(producerRecord);
        }
    }

    private static KafkaProducer<String, String> getProducer(String brokers) {
        return producerCache.compute(brokers, (k, oldProducer) -> {
            if (oldProducer == null) {
                oldProducer = createProducer(brokers);
            }
            return oldProducer;
        });


    }


    /**
     * send message,need key
     *
     * @param topic
     * @param key
     * @param message
     * @return
     */
    public static Future<RecordMetadata> send(String brokers, String topic, String key, String message) {
        KafkaProducer<String, String> producer = getProducer(brokers);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, key, message);
        return producer.send(producerRecord);
    }


    /**
     * send message,do not need key
     *
     * @param topic
     * @param message
     * @return
     */
    public static Future<RecordMetadata> send(String brokers, String topic, String message) {
        KafkaProducer<String, String> producer = getProducer(brokers);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, message);
        return producer.send(producerRecord);
    }


    /**
     * close method
     *
     * @param key
     * @param v
     */
    private static void accept(String key, KafkaProducer<String, String> v) {
        try {
            v.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
