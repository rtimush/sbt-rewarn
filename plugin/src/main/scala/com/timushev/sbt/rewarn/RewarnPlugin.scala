package com.timushev.sbt.rewarn

import sbt.Defaults.foldMappers
import sbt.Keys._
import sbt._
import sbt.rewarn.Accessors.compilerReporter
import sbt.rewarn._
import xsbti.Reporter

object RewarnPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(Compile, Test).flatMap { config =>
      inConfig(config)(
        Seq(
          compile / compilerReporter := Def.taskDyn {
            val oldReporter = (compile / compilerReporter).value
            oldReporter.getClass.getName match {
              case "sbt.internal.server.LanguageServerReporter" =>
                RewarnLanguageServerReporter.reporter
              case "sbt.internal.server.BuildServerReporter" =>
                RewarnBuildServerReporter.reporter
              case _ =>
                RewarnReporterProxy.reporter(oldReporter)
            }
          }.value,
          compile := {
            val analysis = compile.value
            val reporter = (compile / compilerReporter).value
            reporter match {
              case rewarn: RewarnReporter =>
                rewarn.printOldProblems(analysis)
              case _ =>
                ()
            }
            analysis
          }
        )
      )
    }

}
