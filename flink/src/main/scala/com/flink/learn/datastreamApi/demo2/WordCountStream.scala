package com.flink.learn.datastreamApi.demo2

import org.apache.flink.api.common.time.Time
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Description:
 * @Author: dengww
 * @Date: 2024/5/6
 */
object WordCountStream {
  //  192.168.11.76
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val dataStream = env.socketTextStream("192.168.11.76", 9999, '\n')
    dataStream.flatMap { line => line.split(",") }
      .filter(_.nonEmpty)
      .map(x => (x, 1))
      .keyBy(0)
      .sum(1)
      .print()
    env.execute()
  }
}
