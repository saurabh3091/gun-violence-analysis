package com.data.utils

import com.data.utils.DataUtil._
import org.scalatest.{FlatSpec, Matchers}

class DataUtilSpec extends FlatSpec with Matchers {
  "getYearFromDateString" should "return year from date of format YYYY-MM-DD" in {

    getYearFromDateString(date = "2018-06-04") should be(2018)
    getYearFromDateString(date = "") should be(0)
  }

  "getListFromDelimitedString" should "return List of values from || and :: delimited string" in {

    getListFromDelimitedString(delimitedInfo = "0::Female||1::MaLe||2::Male||3::Male") should be(List("female", "male", "male", "male"))

    getListFromDelimitedString(delimitedInfo = "0::Male|1:male|2:FeMale|3:Male") should be(List("male", "male", "female", "male"))
  }

}
