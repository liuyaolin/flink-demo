package com.it.dbswap.main;

import com.alibaba.fastjson.JSON;
import com.it.dbswap.creator.FlinkEnvBuilder;
import com.it.dbswap.creator.KafkaCreator;
import com.it.dbswap.process.SwapKafkaProcessFunction;
import com.it.dbswap.util.TimeTool;
import com.it.dbswap.util.proputil.FlinkTaskPropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class SwapKafkaTestJob {
    //numRecordsInpersecond
    //--processParrel 4 --sourceParrel 2 --sinkParrel 4 --kafkaStartType current
    //-yD state.checkpoints.num-retained=20 -yjm 4096m -ytm 4096m -ys 2  -yqu root.default   -c com.it.dbswap.main.SwapKafkaJob  dbswap-1.0-SNAPSHOT.jar --kafkaStartType current
    private static final Logger logger = LoggerFactory.getLogger(SwapKafkaTestJob.class);
    private static final String SWAP_SOURCE_SRC = "swap_source_src";
    private static final String SWAP_PROCESS = "swap_process";
    private static final String SWAP_OUT_KAFKA_SINK = "swap_out_kafka_sink";

    //--kafkaStartType latest --processParrel 96 --sinkParrel 96 --circles 500
    public static void main(String[] args) throws IOException {

        // 本地测试用
        if ("dev".equalsIgnoreCase(FlinkTaskPropertiesUtil.getEnv())) {
            args = new String[]{
                    "--kafkaStartType", "latest",
                    "--fvpKafkaStartType", "latest",
                    "--taskId", "999888",
                    "--useMapState", "false",
                    "--cachePrintRecordSize", "1",
                    "--useProcessVersionTmCache", "false"
            };
        }
        //获取获取参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        long timestamp =  parameterTool.getLong("timestamp",0);
        //以参数中的 kafkaStartType 为第一优先级
        String kafkaStartType = parameterTool.get("kafkaStartType");
        if(StringUtils.isBlank(kafkaStartType)){
            kafkaStartType = "current";
        }
        String taskId =  parameterTool.get("taskId");
        //kafka处理并行度，主要简单解析数据,获取车辆vin码
        int processParrel = parameterTool.getInt("processParrel", FlinkTaskPropertiesUtil.getProcessParrel());
        //kafka数据源接入并行度
        int sourceParrel = parameterTool.getInt("sourceParrel", FlinkTaskPropertiesUtil.getSourceParrel());
        //kafka数据输出并行度
        int sinkParrel = parameterTool.getInt("sinkParrel", FlinkTaskPropertiesUtil.getSinkParrel());
        //最大并行度，最好跟并行度是倍数关系
        int maxParallelism = parameterTool.getInt("maxParallelism",512);
        //数据放大倍数，用于压测，压测时可以放大到500倍
        int circles= parameterTool.getInt("circles",1);
        logger.info("参数 parameterTool:{}", JSON.toJSONString(parameterTool));


        long checkpointInterval = parameterTool.getInt("ckInterval", 10)* TimeTool.MILLISECOND_OF_MINUTE;
        long checkpointTimeout = parameterTool.getInt("ckTimeout", 20)*TimeTool.MILLISECOND_OF_MINUTE;

        //配置Flink运行环境
        FlinkEnvBuilder builder = new FlinkEnvBuilder()
                .env(FlinkTaskPropertiesUtil.getEnv())
                .localPort(8082)
                // checkpont 间隔时间
                .checkpointInterval(checkpointInterval)
                .maxConcurrentCheckpoints(1)
                // checkpoint 最小间隔时间
                .minPauseBetweenCheckpoints(5 * TimeTool.MILLISECOND_OF_MINUTE)
                // checkpoint 超时时间
                .checkpointTimeoutMilliseconds(checkpointTimeout);

        //配置checkpoint目录
//        String checkpointDir = FlinkTaskPropertiesUtil.getCheckpointDir() + taskId ;
//        if ("dev".equalsIgnoreCase(FlinkTaskPropertiesUtil.getEnv())) {
//            builder.stateBackend(new FsStateBackend(checkpointDir));
//        } else {
//            builder.stateBackend(StateBackendCreator.getRocksDBStateBackend(checkpointDir));
//        }
        //创建Flink运行环境
        StreamExecutionEnvironment env = builder.build();
        //-yD state.checkpoints.num-retained=20
        env.getCheckpointConfig().
                enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

        if(maxParallelism > 0) {
            env.setMaxParallelism(maxParallelism);
        }

        /**
         * kafka数据源接入
         */
        DataStream<Tuple2<String, byte[]>> swapSource
                = env.addSource(KafkaCreator.getSwapSourceKafkaConsumer(kafkaStartType,timestamp))
                .name(SWAP_SOURCE_SRC).uid(SWAP_SOURCE_SRC)
                .setParallelism(sourceParrel);
        /**
         * kafka数据解析处理
         */
        SingleOutputStreamOperator<Tuple2<String, byte[]>> swapOut
                = swapSource
                .process(new SwapKafkaProcessFunction(circles))
                .name(SWAP_PROCESS).uid(SWAP_PROCESS)
                .setParallelism(processParrel);
        /**
         * 解析后的结果输出到kafka
         */
        swapOut
                .addSink(KafkaCreator.getSwapOutProducer())
                .name(SWAP_OUT_KAFKA_SINK).uid(SWAP_OUT_KAFKA_SINK)
                .setParallelism(sinkParrel);

        try {
            env.execute("swap_kafka_to_kafka" + FlinkTaskPropertiesUtil.getEnv());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
