package com.learn.spark.core.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: dengww
 * @Date: 2024/6/25
 */
class SparkBase {

  val conf = new SparkConf()
    .setMaster("local")
    .setAppName("wordCount")

  // 创建spark上下文对象
  val spark = new SparkContext(conf)
}
