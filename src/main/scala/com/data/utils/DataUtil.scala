package com.data.utils

object DataUtil {

  def getYearFromDateString(s: String): Int = s.split("-").head.toInt

  def getListFromDelimitedString(s: String): List[String] = {
    //some data has split on double pipe(|) and some has split on single pipe
    if(s.contains("||")){
      s.split("\\|\\|").map(_.split("::").last).toList
    }
    else{
      s.split("\\|").map(_.split(":").last).toList
    }
  }

}
