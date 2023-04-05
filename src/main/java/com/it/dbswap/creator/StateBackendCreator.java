package com.it.dbswap.creator;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackendFactory;

import java.io.IOException;

import static org.apache.flink.contrib.streaming.state.RocksDBNativeMetricOptions.MONITOR_NUM_IMMUTABLE_MEM_TABLES;

/**
 * @description:
 * @author: huangchm-01328365
 * @date: 2020-04-16 10:50
 */
public class StateBackendCreator {


    public static RocksDBStateBackend getRocksDBStateBackend(String checkpointDir) throws IOException {
        RocksDBStateBackendFactory stateBackendFactory = new RocksDBStateBackendFactory();
        Configuration configuration = new Configuration();
        // 设置 state backend 类型
        configuration.setString("state.backend", "rocksdb");
        // 设置 rocksdb 文件在本地存储的位置
        // 这里需要绝对路径,不能在window中使用
        // configuration.setString("state.backend.rocksdb.localdir", "file:///D/test/checkpoint/state-restore/local");
        // 设置异步 checkpoint
        configuration.setBoolean("state.backend.async", true);
        // 设置增量 checkpoint
        configuration.setBoolean("state.backend.incremental", true);
        // configuration.setString("state.checkpoints.dir", "file:///D/test/checkpoint/state-restore");
        // 设置 checkpoint 保存地址
        configuration.setString("state.checkpoints.dir", checkpointDir);

        configuration.setBoolean("state.backend.rocksdb.ttl.compaction.filter.enabled", true);

/*
        configuration.setBoolean("state.backend.rocksdb.metrics.actual-delayed-write-rate", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.background-errors", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.compaction-pending", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.cur-size-active-mem-table", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.cur-size-all-mem-tables", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.estimate-live-data-size", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.estimate-num-keys", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.estimate-pending-compaction-bytes", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.estimate-table-readers-mem", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.mem-table-flush-pending", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-deletes-active-mem-table", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-deletes-imm-mem-tables", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-entries-active-mem-table", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-entries-imm-mem-tables", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-immutable-mem-table", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-live-versions", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-running-compactions", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-running-flushes", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.num-snapshots", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.size-all-mem-tables", true);
        configuration.setBoolean("state.backend.rocksdb.metrics.total-sst-files-size", true);


        if (configuration.getBoolean(MONITOR_NUM_IMMUTABLE_MEM_TABLES)) {
            System.out.println("");
        }

        */

        // configuration.setInteger("state.checkpoints.num-retained", 10);

        // 设置本地恢复,用于恢复大状态
        // configuration.setBoolean("state.backend.local-recovery", true);
        // configuration.setString("taskmanager.state.local.root-dirs", "file:///D/test/checkpoint/state-restore/local");
        // configuration.setString("state.backend.rocksdb.predefined-options", "SPINNING_DISK_OPTIMIZED");

        // 设置rocksdb 调优配置
        RocksDBStateBackend rocksDBStateBackend = stateBackendFactory.createFromConfig(configuration, Thread.currentThread().getContextClassLoader());
        // rocksDBStateBackend1.setPredefinedOptions(PredefinedOptions.SPINNING_DISK_OPTIMIZED);
        // rocksDBStateBackend1.setOptions();CheckpointStateOutputStream
        return rocksDBStateBackend;
    }
}
