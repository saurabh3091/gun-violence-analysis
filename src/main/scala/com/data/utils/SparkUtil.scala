package com.data.utils

import org.apache.logging.log4j.scala.Logging
import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.types.StructType

object SparkUtil extends Logging {
  /**
    * Creates and returns a spark session
    *
    * @param appName Name of your spark application
    * @return Returns a [[SparkSession]]
    */
  def apply(appName: String): SparkSession = {
    val conf = new SparkConf()
    if (conf.getOption("spark.master").isEmpty)
      conf.setMaster("local[*]")

    val spark: SparkSession = SparkSession
      .builder()
      .appName(appName)
      .config(conf)
      .getOrCreate()

    spark
  }

  /**
    * Reads a CSV as [[DataFrame]]
    *
    * @param path   Path of the CSV to read
    * @param header Boolean to specify whether to infer schema
    * @param schema [[StructType]] to read csv as strongly typed DataSet(avoids issues for casting datatypes later)
    * @param spark  implicit [[SparkSession]]
    * @return a [[DataFrame]]
    */
  def readCSVAsDataFrame(path: String, header: Boolean, schema: StructType)(implicit spark: SparkSession): DataFrame = {
    spark
      .read
      .option("header", value = header)
      .schema(schema)
      .csv(path)
  }

  /**
    * Saves a [[Dataset]] as CSV file having a single partition
    *
    * @param ds   Inpt dataset to be saved
    * @param path Path for the CSV to be saved
    */
  def saveDatasetAsCSV(ds: Dataset[Row], path: String): Unit = {
    ds.coalesce(numPartitions = 1)
      .write.mode(SaveMode.Overwrite).option("header", value = true)
      .csv(path)
  }
}
