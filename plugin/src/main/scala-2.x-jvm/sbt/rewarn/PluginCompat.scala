package sbt.rewarn

import sbt._
import sbt.Keys._

object PluginCompat {

  def compilerReporterKey: TaskKey[xsbti.Reporter] =
    compile / Accessors.compilerReporter

  def compilerReporterSetting(): Def.Setting[Task[xsbti.Reporter]] =
    compilerReporterKey := Def.uncached {
      new RewarnReporterProxy(compilerReporterKey.value)
    }

}
