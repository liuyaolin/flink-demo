package com.it.dbswap.creator;

import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class KafkaProducerKeyHashPartitioner<T> extends FlinkKafkaPartitioner<T> {

    public KafkaProducerKeyHashPartitioner() {

    }

    @Override
    public int partition(T record, byte[] key, byte[] value, String targetTopic, int[] partitions) {
        String keyStr="";
        if(null!= key){
            keyStr=new String(key);
        }

        int hashCode = keyStr.hashCode();
        if(hashCode < 0) {
            hashCode = 0 - hashCode;
        }
        return hashCode % partitions.length;
    }


    // @Override
    // public int partition(Object record, byte[] key, byte[] value, String targetTopic, int[] partitions) {
    //     return rd.nextInt(upRange) % partitions.length;
    // }


    public static void main(String[] args) {

        int[] partitions = {0,1,2,3,4,5,6,7,8,9,10};

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerKeyHashPartitioner().partition(null,null,null,null,partitions));

    }


}
