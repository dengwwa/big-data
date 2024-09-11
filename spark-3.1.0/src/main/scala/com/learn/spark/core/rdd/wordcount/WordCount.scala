package com.learn.spark.core.rdd.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description:
 * @Author: dengww
 * @Date: 2024/6/24
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .setMaster("local")
      .setAppName("wordCount")

    // 创建spark上下文对象
    val context = new SparkContext(conf)

    // 加载文件获得RDD数据集
    val rdd1: RDD[String] = context.textFile("/Users/cds-dn-670/Dev/ideaWorkplace/big-data/spark-3.1.0/src/main/resources/wordcount.txt")
    // 对“集合[String]”中的每条数据应用函数： 按空格切分成数组，并压平，得到一个新的 "集合[String]"
    val rdd2 = rdd1.flatMap(s => s.split("\\s+"))
    // 对 "集合[String]"中的每条数据应用函数：字符串=>元组，得到一个新的"集合[(String,Int)]"
    val rdd3 = rdd2.map(s => (s, 1))
    // 对"集合[(String,Int)]"进行分组聚合运算，分组key:单词, 聚合函数: 将1累加
    val rdd4 = rdd3.reduceByKey((c1, c2) => c1 + c2)
    // 将统计结果打印  (machine,1) (variable,3)
    rdd4.foreach(println)

    // scala 风格
    val unit = context.textFile("/Users/cds-dn-670/Dev/ideaWorkplace/big-data/spark-3.1.0/src/main/resources/wordcount.txt")
      .flatMap(s => s.split("\\s+"))
      .map((_, 1))
      .reduceByKey(_ + _)
      .foreach(println)


    context.stop()
  }
}
