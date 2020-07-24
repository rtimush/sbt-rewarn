package sbt.rewarn

import sbt.Defaults.foldMappers
import sbt.Keys._
import sbt._
import sbt.internal.bsp.BuildTargetIdentifier
import sbt.internal.server.BuildServerReporter
import sbt.internal.util.ManagedLogger
import xsbti.{FileConverter, Position, Reporter}

class RewarnBuildServerReporter(
    buildTarget: BuildTargetIdentifier,
    maximumErrors: Int,
    logger: ManagedLogger,
    sourcePositionMapper: Position => Position = identity,
    converter: FileConverter
) extends BuildServerReporter(buildTarget, maximumErrors, logger, sourcePositionMapper, converter)
    with RewarnReporter

object RewarnBuildServerReporter {
  def reporter: Def.Initialize[Task[Reporter]] =
    Def.task {
      new RewarnBuildServerReporter(
        bspTargetIdentifier.value,
        maxErrors.value,
        streams.value.log,
        foldMappers(sourcePositionMappers.value),
        fileConverter.value
      )
    }
}
