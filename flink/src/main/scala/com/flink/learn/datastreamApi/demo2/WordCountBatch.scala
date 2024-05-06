package com.flink.learn.datastreamApi.demo2

import org.apache.flink.api.scala.{ExecutionEnvironment, createTypeInformation}

/**
 * @Description:
 * @Author: dengww
 * @Date: 2024/5/6
 */
object WordCountBatch {

  def main(args: Array[String]): Unit = {
    val benv = ExecutionEnvironment.getExecutionEnvironment
    val dataSet = benv.readTextFile("/Users/cds-dn-670/Documents/dev/ideaWorkplace/big-data/flink/src/main/resources/wordCount.txt")
    dataSet.flatMap { _.toLowerCase.split(",")}
      .filter (_.nonEmpty)
      .map { (_, 1) }
      .groupBy(0)
      .sum(1)
      .print()
  }
}
