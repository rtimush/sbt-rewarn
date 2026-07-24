package com.timushev.sbt.rewarn

import sbt.Keys._
import sbt._
import sbt.rewarn._
import sbt.rewarn.PluginCompat._

object RewarnPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def projectSettings: Seq[Def.Setting[_]] =
    Seq(Compile, Test).flatMap { config =>
      inConfig(config)(
        Seq(
          PluginCompat.compilerReporterSetting(),
          compile := Def.uncached {
            val analysis = compile.value
            val reporter = compilerReporterKey.value
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
