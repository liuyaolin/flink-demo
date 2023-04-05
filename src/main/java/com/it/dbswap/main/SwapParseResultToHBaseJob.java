package com.it.dbswap.main;

import com.alibaba.fastjson.JSON;
import com.it.dbswap.creator.FlinkEnvBuilder;
import com.it.dbswap.creator.KafkaCreator;
import com.it.dbswap.process.ParseResultProcessFunction;
import com.it.dbswap.process.SwapProcessFunction;
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
public class SwapParseResultToHBaseJob {
    //numRecordsInpersecond
    //--processParrel 4 --sourceParrel 2 --sinkParrel 4 --kafkaStartType current
    //-yD state.checkpoints.num-retained=20 -yjm 4096m -ytm 4096m -ys 2  -yqu root.default   -c com.it.dbswap.main.SwapKafkaJob  dbswap-1.0-SNAPSHOT.jar --kafkaStartType current
    private static final Logger logger = LoggerFactory.getLogger(SwapParseResultToHBaseJob.class);
    private static final String SWAP_SOURCE_SRC = "parse_result_swap_source_src";
    private static final String SWAP_PROCESS = "parse_result_swap_process";
    private static final String SWAP_OUT_KAFKA_SINK = "parse_result_swap_out_kafka_sink";

    //--kafkaStartType latest --sourceParrel 14  --processParrel 20 --tableCount 11 --flushSize 1000
    //--kafkaStartType current --sourceParrel 24 --tableCount 3 --needPrint 1
    public static void main(String[] args) throws IOException {

        // 本地测试用
        if ("dev".equalsIgnoreCase(FlinkTaskPropertiesUtil.getEnv())) {
            args = new String[]{
                    "--kafkaStartType", "latest,earliest",
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
        //保存hbase处理并行度
        int processParrel = parameterTool.getInt("processParrel", FlinkTaskPropertiesUtil.getHbaseProcessParrel());
        //kafka数据源接入并行度
        int sourceParrel = parameterTool.getInt("sourceParrel", FlinkTaskPropertiesUtil.getHbaseSourceParrel());
        //批量写入hbase，每批次条数
        int flushSize =  parameterTool.getInt("flushSize",500);
        //是否需要开启打印，用于调试
        int needPrint = parameterTool.getInt("needPrint",0);
        //最大并行度，最好跟并行度是倍数关系
        int maxParallelism = parameterTool.getInt("maxParallelism",512);
        //保存到hbase中的表的数量，默认1个表，数据很大时，可加大此参数提升性能
        int tableCount =  parameterTool.getInt("tableCount",1);

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
         * 加载解析车辆数据的kafka数据
         */
        DataStream<Tuple2<String, byte[]>> swapSource
                = env.addSource(KafkaCreator.getParseResultSourceKafkaConsumer(kafkaStartType,timestamp))
                .name(SWAP_SOURCE_SRC).uid(SWAP_SOURCE_SRC)
                .setParallelism(sourceParrel);
        /**
         * 处理车辆数据，批量保存到hbase
         */
        SingleOutputStreamOperator<Tuple2<String, byte[]>> swapOut
                = swapSource
                .keyBy(0)
                .process(new ParseResultProcessFunction(flushSize,needPrint,tableCount))
                .name(SWAP_PROCESS).uid(SWAP_PROCESS)
                .setParallelism(processParrel);
//        swapOut
//                .addSink(KafkaCreator.getSwapOutProducer())
//                .name(SWAP_OUT_KAFKA_SINK).uid(SWAP_OUT_KAFKA_SINK)
//                .setParallelism(sinkParrel);

        try {
            env.execute("SwapParseResultToHBaseJob" + FlinkTaskPropertiesUtil.getEnv());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
