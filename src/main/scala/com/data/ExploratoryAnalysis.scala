package com.data

import com.data.models.GunViolenceData
import org.apache.logging.log4j.scala.Logging
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object ExploratoryAnalysis extends Logging{

  def analyseDataset(ds: Dataset[GunViolenceData])(implicit spark: SparkSession): Unit =  {
    logger.info(s"The dataframe has ${ds.count} rows")
    logger.info(s"The schema of dataframe is:")
    ds.printSchema()
    ds.describe("date","state","n_killed","n_injured","gun_stolen","gun_type","n_guns_involved","participant_age","participant_age_group","participant_gender","participant_name","participant_relationship","participant_status","participant_type").show()
  }
}
