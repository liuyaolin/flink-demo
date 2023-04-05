package com.it.dbswap.creator;

import org.apache.flink.streaming.connectors.kafka.partitioner.FlinkKafkaPartitioner;

import java.util.Random;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class KafkaProducerRandomPartitioner<T> extends FlinkKafkaPartitioner<T> {

    private static final Integer upRange = 1000000;

    private Random rd;

    public KafkaProducerRandomPartitioner() {
        rd = new Random();
    }

    @Override
    public int partition(T record, byte[] key, byte[] value, String targetTopic, int[] partitions) {
        return rd.nextInt(upRange) % partitions.length;
    }


    // @Override
    // public int partition(Object record, byte[] key, byte[] value, String targetTopic, int[] partitions) {
    //     return rd.nextInt(upRange) % partitions.length;
    // }


    public static void main(String[] args) {

        int[] partitions = {0,1,2,3,4,5,6,7,8,9,10};

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));
        System.out.println(new KafkaProducerRandomPartitioner().partition(null,null,null,null,partitions));

    }


}
