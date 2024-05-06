package com.flink.learn.datastreamApi.demo2

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Description:
 * @Author: dengww
 * @Date: 2024/5/6
 */
object Example {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val value = env.fromElements(Person("dengww", 28),
      Person("dengww2", 29),
      Person("dengww2", 30))


    val adults = value.filter(new FilterFunction[Person] {
      override def filter(value: Person): Boolean = {
        value.age > 28
      }
    })
    adults.print()

    env.execute("my test")

  }
}

case class Person(name: String, age: Integer)
