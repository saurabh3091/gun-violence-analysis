//package com.data.yaml
//
//import net.jcazevedo.moultingyaml._
//import org.apache.spark.internal.Logging
//
//import scala.io.Source
//import scala.util.control.NonFatal
//
//case class PropConfigs(config: Map[String, Any])
//
//object YamlConfig extends Logging with DefaultYamlProtocol {
//  lazy private val prop: PropConfigs = {
//    try {
//      val prop: PropConfigs =
//        Source.fromInputStream(this.getClass.getResource("/configs/config.yaml").openStream()).mkString.parseYaml.convertTo[PropConfigs]
//      log.info(s"Config: $prop")
//      prop
//    } catch {
//      case NonFatal(e) =>
//        log.error("Exception while creating YAML for configs: ", e)
//        throw new IllegalAccessException()
//    }
//  }
//  implicit val formatPropConfigs = yamlFormat1(PropConfigs)
//
//  def apply(): PropConfigs = prop
//
//}
