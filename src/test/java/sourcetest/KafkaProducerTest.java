package sourcetest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.text.ParseException;
import java.util.Properties;

public class KafkaProducerTest {

    private final Producer<String, byte[]> producer;
    public final static String TOPIC = "IT_WHEEL_MONITOR_INPUT";
    private KafkaProducerTest(){
        Properties props = new Properties();
        // 此处配置的是kafka的broker地址:端口列表
        props.put("bootstrap.servers", KafkaUtil.brokers);
        props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
       // props.put("acks", "-1");
        this.producer = new KafkaProducer(props);
    }

    void produce() throws ParseException {
        int messageNo = 1;
        final int COUNT = 2;
        int messageCount = 0;
        while (messageNo < COUNT) {
            String key = String.valueOf(messageNo);

            KafkaUtil.inputSend("test");
            messageNo ++;
            messageCount++;
        }
        System.out.println("Producer端一共产生了" + messageCount + "条消息！");
    }


    public static void main( String[] args ) throws Exception {
        new KafkaProducerTest().produce();
        Thread.sleep(11000000);
    }

}
