package sbt.rewarn

import sbt.internal.inc.Analysis
import xsbti.compile.CompileAnalysis
import xsbti.{Problem, Reporter}

import scala.collection.mutable

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
    val allProblems = compileAnalysis match {
      case a: Analysis =>
        a.infos.allInfos.values
          .flatMap(i => i.getReportedProblems ++ i.getUnreportedProblems)
      case _ => Nil
    }
    allProblems.filterNot(reportedProblems).foreach(log)
    super.printSummary()
  }

}
