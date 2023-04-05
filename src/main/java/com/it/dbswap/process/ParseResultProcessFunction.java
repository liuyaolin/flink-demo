package com.it.dbswap.process;

import com.alibaba.fastjson.JSONObject;
import com.it.dbswap.constant.CommonConstant;
import com.it.dbswap.domain.VinParseResult;
import com.it.dbswap.hbase.HBaseProcessor;
import com.it.dbswap.util.ProtostuffSerializer;
import com.it.dbswap.util.SaltUtil;
import com.it.dbswap.util.TableCountHashUtil;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ParseResultProcessFunction extends ProcessFunction<Tuple2<String, byte[]>, Tuple2<String, byte[]>> implements CheckpointedFunction{
    private static final Logger logger = LoggerFactory.getLogger(ParseResultProcessFunction.class);
    private  int flushSize = 500;
    private  int needPrint=0;
    private  int tableCount =1;
    private  ConcurrentHashMap<String,List<Put>> putsMap;
    public ParseResultProcessFunction(){

    }
    public ParseResultProcessFunction(int flushSize, int needPrint, int tableCount){
        this.needPrint = needPrint;
        this.flushSize=flushSize;
        this.tableCount = tableCount;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        putsMap = new ConcurrentHashMap<>();
    }

    @Override
    public void processElement(Tuple2<String, byte[]> input, Context context, Collector<Tuple2<String, byte[]>> out) throws Exception {
        try{
           byte[] bytes = input.f1;
           String key = input.f0;
            //把车辆+日期作为rowkey，保存起来
            String[] fullKey = key.split(CommonConstant.FULL_SPLIT_KEY);
            //先解析日期,如：2021-10-28
            String day = fullKey[1];
            //获取时分秒
            String[] time = fullKey[2].split(":");
            //获取车辆表示vin码
            String vin = fullKey[0]+CommonConstant.FULL_SPLIT_KEY;
            /**
             * 此处是压测造数据时，对vin码做了额外的后缀，方便区分，
             * 生产环境此处if内代码不会运行
             */
            if(fullKey.length==4){
                vin +=fullKey[3];
            }

        /**
         * 假如需要分表的话，根据vin的hashCode进行分，
         * vin的hashcode根据表数量取模，假如是10张表，则
         * vin(hash)%/10
         */
            String tablePostfix ="0";
            if(tableCount>1){
                //当存到hbase中的表的数量大于1时，此处表后缀需要根据vin的hash值对表数量取模
                tablePostfix = TableCountHashUtil.getKeyHash(vin,tableCount);
            }
            //日期-去掉，方便hbase表名创建
            day = day.replace("-","");
            /**
             * 根据vin码，日期，时间的时分生成rowkey
             * 此处rowkey生成做了预分区打散，同时方便通过日期进行后缀匹配
             * 加盐处理，把数据打散
             */
            String rowKey = SaltUtil.generateSalt(vin,
                CommonConstant.VIN_HBASE_PARTITION,CommonConstant.VIN_HBASE_SALT_SEED)+vin+day+time[0]+time[1];
            if(needPrint==1){
                logger.warn("====gg==== {} ==== {} ==== {} === {}",key,rowKey,tablePostfix,vin);
            }
            //生成rowkey
            byte[] row = Bytes.toBytes(rowKey);
            Put put = new Put(row);
            byte[] family = Bytes.toBytes(CommonConstant.VIN_HBAE_FAMILY_NAME);
            //秒作为列，rowkey是分钟，一分钟内的数据都会保存到同一个rowkey中
            byte[] valQualifier = Bytes.toBytes(time[2]);
            //解析结果数据进行pb压缩
            VinParseResult a = JSONObject.parseObject(new String(bytes,"utf-8"),VinParseResult.class);
            put.addColumn(family, valQualifier,ProtostuffSerializer.serialize(a));

            //获取对应的hbase表的 put缓存
            List<Put> puts = putsMap.get(tablePostfix);
            if(puts==null){
                puts= new ArrayList<>();
            }
            puts.add(put);
            //缓存数据量大于默认500条时，写入hbase,
            //写入完成后，会清空处理
            if(puts.size()>flushSize){
                saveToHBase(puts,tablePostfix);
            }
            //未写入hbase的数据会写入缓存中
            putsMap.put(tablePostfix,puts);
        }catch (Exception e){
            logger.error("=== {}",e);
        }
    }

    private void saveToHBase(List<Put> puts,String tablePostfix) throws Exception {
        try {
            //批量写入hbase，同时清空缓存
            HBaseProcessor.saveToHbase(puts, CommonConstant.getVinParseHbaseTable()+tablePostfix, CommonConstant.VIN_HBAE_FAMILY_NAME);
            puts.clear();
        } catch (Exception e) {
            logger.error("saveToHbase {}", e);
            throw e;
        }
    }

    private void saveToHBase(String tablePostfix) throws Exception {
        if(tablePostfix!=null){
            List<Put> puts = putsMap.get(tablePostfix);
            if(puts.size()>flushSize){
                // 往hbase追加数据
                try {
                    HBaseProcessor.saveToHbase(puts, CommonConstant.getVinParseHbaseTable()+tablePostfix, CommonConstant.VIN_HBAE_FAMILY_NAME);
                } catch (Exception e) {
                    logger.error("saveToHbase {}", e);
                    throw e;
                }
                puts.clear();
            }
        }else{
            for(String key : putsMap.keySet()){
                List<Put> puts = putsMap.get(key);
                if(puts!=null && puts.size()>flushSize){
                    // 往hbase追加数据
                    try {
                        HBaseProcessor.saveToHbase(puts, CommonConstant.getVinParseHbaseTable()+key, CommonConstant.VIN_HBAE_FAMILY_NAME);
                    } catch (Exception e) {
                        logger.error("saveToHbase {}", e);
                        throw e;
                    }
                    puts.clear();
                }
            }
        }

    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
        //每次快照时，需要把缓存中的数据写入hbase防止数据丢失
        saveToHBase(null);
    }

    @Override
    public void initializeState(FunctionInitializationContext functionInitializationContext) throws Exception {

    }

}
