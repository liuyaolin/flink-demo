package com.it.dbswap.creator;

import org.apache.flink.configuration.ConfigConstants;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @description:
 * @author:
 * @date:
 */
public class FlinkEnvBuilder {

    private String env;
    private Configuration conf;
    private StateBackend stateBackend;
    public int localPort;
    private boolean localStartWebserver;
    //checkpoint的间隔时长
    private long checkpointInterval;
    private int maxConcurrentCheckpoints;
    private long minPauseBetweenCheckpoints;
    //checkpoint的超时时间
    private long checkpointTimeoutMilliseconds;
    public FlinkEnvBuilder env(String env) {
        this.env = env;
        return this;
    }


    public FlinkEnvBuilder localPort(int localPort) {
        this.localPort = localPort;
        return this;
    }

    public FlinkEnvBuilder stateBackend(StateBackend stateBackend) {
        this.stateBackend = stateBackend;
        return this;
    }

    public FlinkEnvBuilder checkpointInterval(long checkpointInterval) {
        this.checkpointInterval = checkpointInterval;
        return this;
    }

    public FlinkEnvBuilder maxConcurrentCheckpoints(int maxConcurrentCheckpoints) {
        this.maxConcurrentCheckpoints = maxConcurrentCheckpoints;
        return this;
    }

    public FlinkEnvBuilder minPauseBetweenCheckpoints(long minPauseBetweenCheckpoints) {
        this.minPauseBetweenCheckpoints = minPauseBetweenCheckpoints;
        return this;
    }

    public FlinkEnvBuilder checkpointTimeoutMilliseconds(long checkpointTimeoutMilliseconds){
        this.checkpointTimeoutMilliseconds=checkpointTimeoutMilliseconds;
        return this;
    }


    /**
     * 构建flink的执行环境
     * @return
     */
    public StreamExecutionEnvironment build() {
        StreamExecutionEnvironment environment;
        if ("dev".equalsIgnoreCase(env)) {
            conf = new Configuration();
            conf.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true);
            conf.setInteger(RestOptions.PORT, localPort);
            //dev本地启动时，采用createLocalEnvironmentWithWebUI创建
            environment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf);
        } else {
            environment = StreamExecutionEnvironment.getExecutionEnvironment();
        }
        if(stateBackend!=null)
        environment.setStateBackend(stateBackend);

        if (checkpointInterval > 0) {
            environment.getCheckpointConfig().setCheckpointInterval(checkpointInterval);
        }

        if (maxConcurrentCheckpoints > 0) {
            environment.getCheckpointConfig().setMaxConcurrentCheckpoints(maxConcurrentCheckpoints);
        }

        if (minPauseBetweenCheckpoints > 0) {
            environment.getCheckpointConfig().setMinPauseBetweenCheckpoints(minPauseBetweenCheckpoints);
        }

        if(checkpointTimeoutMilliseconds>0){
            environment.getCheckpointConfig().setCheckpointTimeout(checkpointTimeoutMilliseconds);
        }

        return environment;
    }

}
