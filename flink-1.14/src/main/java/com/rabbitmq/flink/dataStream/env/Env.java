package com.rabbitmq.flink.dataStream.env;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Description:Env
 * Author:dengww
 * Date:2024/8/10
 */
public class Env {
    public static void main(String[] args) throws Exception {
        // 批处理环境
        ExecutionEnvironment batchEnv = ExecutionEnvironment.getExecutionEnvironment();
        // todo
        batchEnv.execute();

        // 批流处理环境
        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        // 设置处理模式
        streamEnv.setRuntimeMode(RuntimeExecutionMode.BATCH);
        streamEnv.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        streamEnv.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        streamEnv.execute();

        // 显式声明为本地运行环境，且带webUI
        Configuration configuration = new Configuration();
        configuration.setInteger("rest.port", 8081);
        StreamExecutionEnvironment localEnv = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        localEnv.execute();

    }
}
