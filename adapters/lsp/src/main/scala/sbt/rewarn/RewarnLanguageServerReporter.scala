package sbt.rewarn

import sbt.Defaults._
import sbt.Keys._
import sbt._
import sbt.internal.server.LanguageServerReporter
import sbt.internal.util.ManagedLogger
import xsbti.{Position, Reporter}

class RewarnLanguageServerReporter(
    maximumErrors: Int,
    logger: ManagedLogger,
    sourcePositionMapper: Position => Position = identity
) extends LanguageServerReporter(maximumErrors, logger, sourcePositionMapper)
    with RewarnReporter

object RewarnLanguageServerReporter {
  def reporter: Def.Initialize[Task[Reporter]] =
    Def.task {
      new RewarnLanguageServerReporter(
        maxErrors.value,
        streams.value.log,
        foldMappers(sourcePositionMappers.value)
      )
    }
}
