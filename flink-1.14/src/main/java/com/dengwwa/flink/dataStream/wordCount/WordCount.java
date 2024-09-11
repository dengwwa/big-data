package com.dengwwa.flink.dataStream.wordCount;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * Description:WordCount
 * Author:dengww
 * Date:2024/8/10
 */
public class WordCount {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // execute the command  nlc -lk 9999 in terminal
        env.socketTextStream("localhost", 9999)
                .flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<String,Integer>> collector) throws Exception {
                        for (String word : s.split(",")) {
                            collector.collect(Tuple2.of(word,1));
                        }
                    }
                })
                .keyBy(s -> s.f0)
                .sum(1)
                .print().setParallelism(2);

        // 本地运行模式，默认并行度为cpu的逻辑核数
        env.setParallelism(1);
        env.execute("word count demo");
    }
}
