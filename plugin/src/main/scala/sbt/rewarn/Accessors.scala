package sbt.rewarn

import sbt.TaskKey
import xsbti.Reporter

object Accessors {

  val compilerReporter: TaskKey[Reporter] =
    sbt.Keys.compilerReporter

}
