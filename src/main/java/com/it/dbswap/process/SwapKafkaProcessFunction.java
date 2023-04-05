package com.it.dbswap.process;

import com.it.dbswap.constant.CommonConstant;
import com.it.dbswap.hbase.HBaseProcessor;
import com.it.dbswap.message.Message;
import com.it.dbswap.message.MessageParse;
import com.it.dbswap.util.RowKeyHashUtil;
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

public class SwapKafkaProcessFunction extends ProcessFunction<Tuple2<String, byte[]>, Tuple2<String, byte[]>> implements CheckpointedFunction{
    private static final Logger logger = LoggerFactory.getLogger(SwapKafkaProcessFunction.class);
    private static final int FLUSH_SIZE = 500;
    private List<Put> puts;
    private int circles;
   public SwapKafkaProcessFunction(int circles){
        this.circles = circles;
    }
    public SwapKafkaProcessFunction(){

    }

    @Override
    public void open(Configuration parameters) throws Exception {
        puts = new ArrayList<>();
    }

    @Override
    public void processElement(Tuple2<String, byte[]> input, Context context, Collector<Tuple2<String, byte[]>> out) throws Exception {
       try {
           byte[] bytes = input.f1;
           //解析车辆数据
           Message ms = MessageParse.parsing(bytes);
           if (ms != null && ms.getSendingTime() != null) {
               //把车辆+日期作为rowkey，保存起来
               String[] fullTime = ms.getSendingTime().split(" ");
               String day = fullTime[0];
               String time = fullTime[1];
               //压测时circles可以放到N倍，默认1是不放大,直接输出到kafka
               if (circles == 1) {
                   out.collect(new Tuple2<>(ms.getVin() + CommonConstant.FULL_SPLIT_KEY + day
                           + CommonConstant.FULL_SPLIT_KEY + time, bytes));
               } else {
                   /**
                    * 放大后输出到kafka，压测时使用
                    */
                   for (int i = 0; i < circles; i++) {
                       out.collect(new Tuple2<>(ms.getVin() + CommonConstant.FULL_SPLIT_KEY + day
                               + CommonConstant.FULL_SPLIT_KEY + time + CommonConstant.FULL_SPLIT_KEY + i, bytes));
                   }
               }


           }
       }catch (Exception e){
           logger.warn("{}",e);
       }

    }

//    private void saveToHBase() throws Exception {
//        if(puts.size()>FLUSH_SIZE){
//            // 往hbase追加数据
//            try {
//                HBaseProcessor.saveToHbase(puts, CommonConstant.VIN_BASE_HBASE_TABLE, CommonConstant.VIN_HBAE_FAMILY_NAME);
//            } catch (Exception e) {
//                logger.error("saveToHbase {}", e);
//                throw e;
//            }
//            puts.clear();
//        }
//    }

    @Override
    public void snapshotState(FunctionSnapshotContext functionSnapshotContext) throws Exception {
//        saveToHBase();
    }

    @Override
    public void initializeState(FunctionInitializationContext functionInitializationContext) throws Exception {

    }

}
