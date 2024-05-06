package com.spark.learn

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Description: 弹性式数据集RDDs
 * @Author: dengww
 * @Date: 2024/4/29
 */
object SparkRDD {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("spark rdd demo").setMaster("local[*]")
    var sc = new SparkContext(conf)

//    val lines = List("spark flume spark",
//      "hadoop flume hive")
//    sc.parallelize(lines).flatMap(line => line.split(" ")).
//      map(word => (word, 1)).reduceByKey(_ + _).foreach(println)

    val list = List(List(1, 2), List(3), List(), List(4, 5))
    sc.parallelize(list).flatMap(_.toList).foreach(println)

//    makeRddFromOutDataSet(sc)
  }


  /**
   * 创建RDD的两种方式：
   * 1.由现有集合创建；
   * 2.读取外部存储系统中的数据集
   */


  /**
   * 由现有集合作为数据源创建RDD
   *
   * @param ctx
   */
  def makeRddFromInnerDataSet(ctx: SparkContext): Unit = {
    val data = Array(1, 2, 3, 4, 5)
    // 由现有集合创建 RDD,默认分区数为程序所分配到的 CPU 的核心数10
    val dataRDD = ctx.parallelize(data)
    // 查看分区数
    println(dataRDD.getNumPartitions)

    // 指定分区数
    val dataRDD2 = ctx.parallelize(data, 2)
    println(dataRDD2.getNumPartitions)
  }

  /**
   * 读取外部数据集作为数据源创建RDD
   *
   * @param ctx
   */
  def makeRddFromOutDataSet(ctx: SparkContext): Unit = {
    // textFile：其返回格式是 `RDD[String]` ，返回的是就是文件内容，RDD 中每一个元素对应一行数据；
    val fieRDD = ctx.textFile("/Users/cds-dn-670/Documents/dev/ideaWorkplace/big-data/spark/src/main/resources/wc.txt")
    // 获取第一行稳步
    val strings = fieRDD.take(1)

    // wholeTextFiles：其返回格式是 `RDD[(String, String)]`，元组中第一个参数是文件路径，第二个参数是文件内容
    val fieRDD2 = ctx.wholeTextFiles("/Users/cds-dn-670/Documents/dev/ideaWorkplace/big-data/spark/src/main/resources/wc.txt")


  }


}

