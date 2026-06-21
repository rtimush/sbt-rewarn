package sbt.rewarn

import xsbti.compile.CompileAnalysis
import xsbti.compile.analysis.SourceInfo
import xsbti.{Problem, Reporter}

import scala.collection.mutable
import scala.collection.JavaConverters._

trait RewarnReporter extends Reporter {

  private lazy val reportedProblems = mutable.Set[Problem]()

  abstract override def reset(): Unit = {
    super.reset()
    reportedProblems.clear()
  }

  abstract override def log(problem: Problem): Unit = {
    super.log(problem)
    reportedProblems.add(problem)
  }

  abstract override def printSummary(): Unit =
    if (hasErrors) super.printSummary()

  def printOldProblems(compileAnalysis: CompileAnalysis): Unit = {
    val sourceInfos = compileAnalysis.readSourceInfos()
    val allProblems = sourceInfos.getAllSourceInfos.asScala.values.flatMap { info =>
      info.getReportedProblems.toSeq ++ info.getUnreportedProblems.toSeq
    }
    allProblems.filterNot(reportedProblems).foreach(p => log(p))
    super.printSummary()
  }

}
