package com.data

import com.data.ExploratoryAnalysis._
import com.data.models._
import com.data.utils.DataUtil._
import com.data.utils.SparkUtil
import com.data.utils.SparkUtil._
import com.data.yaml.YamlConfig
import org.apache.logging.log4j.scala.Logging
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.storage.StorageLevel

object GunViolenceAnalyzer extends Logging {
  def main(args: Array[String]): Unit = {
    //Initialize spark context
    implicit val spark: SparkSession = SparkUtil(appName = "Gun-violence-Analysis")
    val csvPath = args.headOption match {
      case Some(path) => path
      case None => throw new IllegalArgumentException("Please specify csv path in program arguments")
    }

    process(csvPath)
    spark.stop()
  }

  def process(path: String)(implicit spark: SparkSession): Unit = {
    import spark.implicits._
    val schema: StructType = Encoders.product[GunViolenceData].schema
    val gunViolenceDataset: Dataset[GunViolenceData] =
      readCSVAsDataFrame(path, header = true, schema = schema).as[GunViolenceData]

    gunViolenceDataset.persist(StorageLevel.MEMORY_AND_DISK_SER)

    //do a basic EDA of csv
    analyseDataset(gunViolenceDataset)

    //state wise violence incidents
    val mostViolentStates: Dataset[Row] = gunViolenceDataset
      .select(col = "state")
      .groupBy(col1 = "state")
      .count()
      .orderBy(desc(columnName = "count"))

    saveDatasetAsCSV(mostViolentStates, YamlConfig().csvPaths("stateWiseViolence"))
    logger.info(s"Top 10 most violent cities: ${mostViolentStates.take(10)}")

    //year wise crimes
    val crimesByYear: Dataset[Row] = gunViolenceDataset
      .filter(_.date.isDefined)
      .map(r => getYearFromDateString(r.date.get))
      .toDF()
      .withColumnRenamed("value", "year")
      .groupBy("year")
      .count()
      .orderBy(desc("year"))
    saveDatasetAsCSV(crimesByYear, YamlConfig().csvPaths("yearWiseViolence"))
    logger.info(s"Year wise violence: ${crimesByYear.show()}")


    //total recoded injuries
    val totalInjuries: Int = gunViolenceDataset
      .filter(_.n_injured.isDefined)
      .map(r => r.n_injured.get.toInt)
      .reduce(_ + _)
    logger.info(s"Total recorded injuries: $totalInjuries")

    //total recorded killings
    val totalKilled: Int = gunViolenceDataset
      .filter(_.n_killed.isDefined)
      .map(r => r.n_killed.get.toInt)
      .reduce(_ + _)
    logger.info(s"Total recorded killings: $totalKilled")

    //different gun types used
    val gunTypes = gunViolenceDataset
      .filter(row => row.n_guns_involved.isDefined && row.gun_type.isDefined)
      .map( row => getListFromDelimitedString(row.gun_type.get))
      .withColumn("value", explode(col("value")))
      .withColumnRenamed("value", "gun_type")
      .groupBy("gun_type")
      .count()
      .orderBy(desc("count"))

    saveDatasetAsCSV(gunTypes, YamlConfig().csvPaths("gunTypes"))

    val stateParticipantsDF: DataFrame = gunViolenceDataset
      .filter(r => r.participant_gender.isDefined && r.state.isDefined && r.participant_age_group.isDefined)
      .map{r =>
        val genderList = getListFromDelimitedString(r.participant_gender.get)
        val ageList = getListFromDelimitedString(r.participant_age_group.get)

        StateParticipantsInfo(r.state.get, genderList, ageList)
      }.toDF()

    //state-gender wise counts of participants
    val genderWiseParticipants: Dataset[Row] = stateParticipantsDF
      .withColumn("gender", explode(col("gender")))
      .groupBy("state", "gender")
      .count()
      .orderBy(asc("state"), desc("gender"))

    saveDatasetAsCSV(genderWiseParticipants, YamlConfig().csvPaths("stateGenderWise"))

    //state-age wise counts of participants
    val ageWiseParticipants: Dataset[Row] = stateParticipantsDF
      .withColumn("age", explode(col("age")))
      .groupBy("state", "age")
      .count()
      .orderBy(asc("state"), asc("age"))

    saveDatasetAsCSV(ageWiseParticipants, YamlConfig().csvPaths("stateAgeWise"))
  }
}
