package com.learn.spark.core.rdd.operator.transformation

import com.learn.spark.core.rdd.SparkBase

/**
 * @Desparkription: spark rdd 转换算子
 * @Author: dengww
 * @Date: 2024/6/25
 */
object TransformationOperators extends SparkBase {

  def main(args: Array[String]): Unit = {
    // map算子，功能：根据传入的函数f，对每个元素做映射
    spark.parallelize(List(1, 2, 3, 4, 5)).map(_ * 2).foreach(println)

    // flatmap
    spark.parallelize(Array("a b c", "d f e", "h i j")).flatMap(_.split(' ')).foreach(println)
    spark.parallelize(List(List("a b c", "a b b"), List("e f g", "a f g"))).flatMap(_.flatMap(_.split(" "))).foreach(println)

    // filter算子,功能：根据传入的函数f，对每个元素做判断以过滤数据
    spark.parallelize(List(5, 6, 4, 7, 3, 8, 2, 9, 1, 10)).filter(_ % 2 == 0).foreach(println)

    // mapPartitions，类似于map算子，但执行逻辑不同：mapPartitions算子，是将一个分区的数据对应的迭代器传入用户的函数（相当于一次性传入一个分区的数据让用户的函数来处理）。
    spark.parallelize(List(1, 2, 3, 4, 5), 2).mapPartitions(it => it.map(x => x * 10)).foreach(println) // it 为迭代器

    // mapPartitionsWithIndex，类似于mapPartitions, 不过函数要输入两个参数，第一个参数为分区的索引，第二个是对应分区的迭代器。函数的返回的是一个经过该函数转换的迭代器。
    spark.parallelize(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 2).mapPartitionsWithIndex((index, it) => it.map(e => s"partition:$index,val:$e")).foreach(println)

    // sortBy算子，排序
    spark.parallelize(List(5, 11, 22, 13, 2, 1, 10)).sortBy(x => x, ascending = true).foreach(println)

    // groupBy算子，指定字段分组
    spark.parallelize(List(("hello", 9), ("tom", 8), ("kitty", 7), ("tom", 2))).groupBy(_._2).foreach(println)
    // groupByKey 按照key进行分组
    spark.parallelize(List(("hello", 9), ("tom", 8), ("kitty", 7), ("tom", 2))).groupByKey().foreach(println)

    // reduceByKey算子，按照key进行聚合
    spark.parallelize(List(("jerry", 9), ("tom", 8), ("shuke", 7), ("tom", 2))).reduceByKey(_ + _).foreach(println)

    // distinct算子，去重
    spark.parallelize(List(5, 5, 6, 6, 7, 8, 8, 8)).distinct.foreach(println)

    // union算子，并集，两个RDD类型要一样
    spark.parallelize(List(5, 6, 4, 7), 2).union(spark.parallelize(List(1, 2, 3, 4), 3)).foreach(println)

    // cogroup算子，协分组，有点跟fullOuterJoin类似，但是没有关联上的返回CompactBuffer ()
    val rdd1 = spark.parallelize(List(("tom", 1), ("tom", 2), ("jerry", 3), ("kitty", 2)))
    val rdd2 = spark.parallelize(List(("jerry", 2), ("tom", 1), ("shuke", 2)))
    val rdd3 = rdd1.cogroup(rdd2)
    rdd3.foreach(println)
    rdd3.map(t => (t._1, t._2._1.sum + t._2._2.sum)).foreach(println)

    // intersection算子，交集
    val rdd6 = spark.parallelize(List(5, 6, 4, 7))
    val rdd7 = spark.parallelize(List(1, 2, 3, 4))
    rdd6.intersection(rdd7).foreach(println)

    // subtract算子，差集
    rdd6.subtract(rdd7).foreach(println)

    // join，相当于SQL中的内关联join
    val rdd8 = spark.parallelize(List(("tom", 1), ("jerry", 2), ("kitty", 3)))
    val rdd9 = spark.parallelize(List(("jerry", 9), ("tom", 8), ("shuke", 7), ("tom", 2)))
    rdd8.join(rdd9).foreach(println)

    // leftOuterJoin，相当于SQL中的左外关联
    rdd8.leftOuterJoin(rdd9).foreach(println)

    // rightOuterJoin，相当于SQL中的右外关联
    rdd8.rightOuterJoin(rdd9).foreach(println)

    // fullOuterJoin，相当于SQL中的全关联
    rdd8.fullOuterJoin(rdd9).foreach(println)

    // cartesian算子，笛卡尔积
    val rdd10 = spark.parallelize(List("tom", "jerry"))
    val rdd11 = spark.parallelize(List("tom", "kitty", "shuke"))
    rdd10.cartesian(rdd11)

    // aggregateByKey,按照key进行聚合，跟reduceByKey类似，
    // 可以输入两个函数，第一个函数局部聚合，第二个函数全局聚合。初始值只在局部聚合时使用，全局聚合不使用
    val pairRDD = spark.parallelize(List(("cat", 2), ("cat", 5), ("mouse", 4),
      ("cat", 12), ("dog", 12), ("mouse", 2)), 2)
    pairRDD.aggregateByKey(0)(math.max, _ + _).collect.foreach(println)
    pairRDD.aggregateByKey(100)(math.max, _ + _).collect.foreach(println)

    // combineByKey，需要输入3个参数，
    // 第1个参数为分组后value的第一个元素，
    // 第2个函数为局部聚合函数，
    // 第3个函数为全局聚合函数

    // reduceByKey、foldByKey、aggregateByKey、combineByKey底层调用的都是combineByKeyWithClassTag
    pairRDD.combineByKey(x => x, (a: Int, b: Int) => a + b, (m: Int, n: Int) => m + n).foreach(println)

    val a = spark.parallelize(List("dog","cat","gnu","salmon","rabbit","turkey","wolf","bear","bee"), 3)
    val b = spark.parallelize(List(1,1,2,2,2,1,2,2,2), 3)
    val c = b.zip(a)
    b.zip(a).foreach(println)
    val d = c.combineByKey(List(_), (x:List[String], y:String) => y :: x,
      (x:List[String], y:List[String]) => x ::: y)
    d.collect.foreach(println)
  }

}
