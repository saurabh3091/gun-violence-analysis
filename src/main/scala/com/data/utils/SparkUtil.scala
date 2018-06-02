package com.data.utils

import org.apache.logging.log4j.scala.{Logger, Logging}
import org.apache.spark.sql.SparkSession

object SparkUtil extends Logging {
  def apply(appName: String): SparkSession = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName(appName)
      .config("spark.master", "local[*]")
      .getOrCreate()

    spark
  }

}
