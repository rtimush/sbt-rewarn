package sbt.rewarn

import sbt._
import sbt.Keys._

object PluginCompat {

  def compilerReporterSetting(
      compilerReporterKey: TaskKey[xsbti.Reporter]
  ): Def.Setting[Task[xsbti.Reporter]] =
    compilerReporterKey := Def.uncached {
      new RewarnReporterProxy(compilerReporterKey.value)
    }

}
