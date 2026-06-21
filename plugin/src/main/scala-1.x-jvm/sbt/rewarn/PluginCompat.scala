package sbt.rewarn

import sbt._
import sbt.Keys._

object PluginCompat {

  def compilerReporterKey: TaskKey[xsbti.Reporter] =
    Accessors.compilerReporter in compile

  def compilerReporterSetting(): Def.Setting[Task[xsbti.Reporter]] =
    compilerReporterKey := Def.taskDyn {
      val oldReporter = compilerReporterKey.value
      oldReporter.getClass.getName match {
        case "sbt.internal.server.LanguageServerReporter" =>
          RewarnLanguageServerReporter.reporter
        case _ =>
          RewarnReporterProxy.reporter(oldReporter)
      }
    }.value

  implicit class DefOps(private val singleton: Def.type) extends AnyVal {
    def uncached[A](a: A): A = a
  }
}
