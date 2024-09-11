package com.learn.spark.core.sql

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

/**
 * Description:
 * Author:dengww
 * Date:${date}
 */
 object SparkSql {
  Logger.getLogger("org").setLevel(Level.WARN)
  def main(args: Array[String]): Unit = {
   val spark = SparkSession
     .builder()
     .master("local[*]")
     .appName("spark sql demo")
     .getOrCreate()


  val schema = StructType(Array(
    StructField("name",StringType,nullable = false),
    StructField("age",IntegerType,nullable = false)
   ))


   val df =spark.read.json("/Users/cds-dn-670/Dev/ideaWorkplace/big-data/spark-3.1.0/src/main/resources/user.json")
   df.printSchema()
   df.show()
   df.filter("age > 19").show
  }
}
