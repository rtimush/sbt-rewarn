package sbt.rewarn

import sbt._
import xsbti.{Position, Problem, Reporter}

class RewarnReporterProxy0(underlying: Reporter) extends Reporter {
  override def hasErrors: Boolean                        = underlying.hasErrors
  override def hasWarnings: Boolean                      = underlying.hasWarnings
  override def problems(): Array[Problem]                = underlying.problems()
  override def comment(pos: Position, msg: String): Unit = underlying.comment(pos, msg)
  override def reset(): Unit                             = underlying.reset()
  override def log(problem: Problem): Unit               = underlying.log(problem)
  override def printSummary(): Unit                      = underlying.printSummary()
}

class RewarnReporterProxy(underlying: Reporter) extends RewarnReporterProxy0(underlying) with RewarnReporter

object RewarnReporterProxy {
  def reporter(underlying: Reporter): Def.Initialize[Task[Reporter]] =
    Def.task {
      new RewarnReporterProxy(underlying)
    }
}
