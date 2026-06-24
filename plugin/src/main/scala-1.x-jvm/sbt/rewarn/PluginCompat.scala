package sbt.rewarn

import sbt._
import sbt.Keys._

object PluginCompat {

  def compilerReporterSetting(
      compilerReporterKey: TaskKey[xsbti.Reporter]
  ): Def.Setting[Task[xsbti.Reporter]] =
    compilerReporterKey := Def.taskDyn {
      val oldReporter = compilerReporterKey.value
      oldReporter.getClass.getName match {
        case "sbt.internal.server.LanguageServerReporter" =>
          RewarnLanguageServerReporter.reporter
        case _ =>
          RewarnReporterProxy.reporter(oldReporter)
      }
    }.value

}
