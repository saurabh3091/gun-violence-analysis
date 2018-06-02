package com.data.utils

object DataUtil {
  /**
    * Parses a Date of format YYYY-MM-DD and returns YYYY as Int
    *
    * @param date Date as YYYY-MM-DD
    * @return YEAR as YYYY
    */
  def getYearFromDateString(date: String): Int = date.split("-").head.toInt

  /**
    * Parses a string having delimiters
    * 0::Female||1::Male||2::Male||3::Male
    * to describe values related to different incidents and returns them as List of String
    *
    * @param delimitedInfo Info as delimited string having || and ::
    * @return List of extracted info
    */
  def getListFromDelimitedString(delimitedInfo: String): List[String] = {
    //some data has split on double pipe(||) and some has split on single pipe(|)
    if (delimitedInfo.contains("||")) {
      delimitedInfo.split("\\|\\|").map(_.split("::").last).toList
    }
    else {
      delimitedInfo.split("\\|").map(_.split(":").last).toList
    }
  }
}
