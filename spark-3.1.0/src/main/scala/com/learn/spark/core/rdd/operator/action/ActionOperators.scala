package com.learn.spark.core.rdd.operator.action

import com.learn.spark.core.rdd.SparkBase

/**
 * Description:
 * Author:dengww
 * Date:${date}
 */
object ActionOperators extends SparkBase{

  def main(args: Array[String]): Unit = {
  // reduce
  val int = spark.parallelize(List(1, 2, 3, 4, 5)).reduce((x, y) => x + y)
    println(int)

//    List(1,2,3).map()
  }
}
