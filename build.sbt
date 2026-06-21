import SbtAxis.RichProjectMatrix

ThisBuild / organization := "com.timushev.sbt"
ThisBuild / homepage     := Some(url("https://github.com/rtimush/sbt-rewarn"))
ThisBuild / licenses += (("BSD 3-Clause", url("https://github.com/rtimush/sbt-rewarn/blob/master/LICENSE")))
ThisBuild / developers := List(
  Developer("rtimush", "Roman Timushev", "rtimush@gmail.com", url("https://github.com/rtimush"))
)
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/rtimush/sbt-rewarn"),
    "scm:git:https://github.com/rtimush/sbt-rewarn.git",
    Some("scm:git:git@github.com:rtimush/sbt-rewarn.git")
  )
)
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / publishTo     := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots".at(centralSnapshots))
  else localStaging.value
}

ThisBuild / scalacOptions := Seq("-deprecation", "-unchecked", "-feature")

Global / scriptedLaunchOpts += s"-Dsbt.rewarn.version=${version.value}"

lazy val `sbt-1.x`      = SbtAxis("1.x", "1.1.5")
lazy val `sbt-1.latest` = SbtAxis()
lazy val `sbt-1.3.13`   = SbtAxis("1.3.13")
lazy val `sbt-1.2.0`    = SbtAxis("1.2.0")
lazy val `sbt-1.1.0`    = SbtAxis("1.1.0")
lazy val `sbt-1.0.0`    = SbtAxis("1.0.0")
lazy val `sbt-2.x`      = SbtAxis("2.x", "2.0.0")
lazy val `sbt-2.0.0`    = SbtAxis("2.0.0", "2.0.0")

lazy val `sbt-rewarn-common` = (projectMatrix in file("common"))
  .jvmPlatform(scalaVersions = Seq(`sbt-1.x`.scalaVersion, `sbt-2.x`.scalaVersion))
  .disablePlugins(GitVersioningPlugin)
  .settings(
    publish / skip := true
  )
  .customRow(
    autoScalaLibrary = false,
    axisValues = Seq(`sbt-1.x`, SbtAxis.jvm),
    _.settings(
      scalaVersion                           := `sbt-1.x`.scalaVersion,
      libraryDependencies += "org.scala-sbt" %% "main" % `sbt-1.x`.fullVersion.value % Provided
    )
  )
  .customRow(
    autoScalaLibrary = false,
    axisValues = Seq(`sbt-2.x`, SbtAxis.jvm),
    _.settings(
      scalaVersion                           := `sbt-2.x`.scalaVersion,
      libraryDependencies += "org.scala-sbt" %% "main" % `sbt-2.x`.fullVersion.value % Provided
    )
  )
lazy val `sbt-rewarn-common-1.x` = `sbt-rewarn-common`.finder(`sbt-1.x`, SbtAxis.jvm)(false)
lazy val `sbt-rewarn-common-2.x` = `sbt-rewarn-common`.finder(`sbt-2.x`, SbtAxis.jvm)(false)

lazy val `sbt-rewarn-adapter-lsp` = (project in file("adapters/lsp"))
  .dependsOn(`sbt-rewarn-common-1.x`)
  .disablePlugins(GitVersioningPlugin)
  .settings(
    scalaVersion                           := `sbt-1.x`.scalaVersion,
    libraryDependencies += "org.scala-sbt" %% "main" % `sbt-1.3.13`.fullVersion.value % Provided,
    publish / skip                         := true
  )

lazy val `sbt-rewarn` = (projectMatrix in file("plugin"))
  .sbtPluginRow(
    `sbt-1.x`,
    _.dependsOn(`sbt-rewarn-adapter-lsp` % Provided)
      .settings(
        Compile / products ++= (`sbt-rewarn-common-1.x` / Compile / products).value,
        Compile / products ++= (`sbt-rewarn-adapter-lsp` / Compile / products).value
      )
  )
  .sbtPluginRow(
    `sbt-2.x`,
    _.settings(
      Compile / dependencyClasspath ++= (`sbt-rewarn-common-2.x` / Compile / exportedProducts).value,
      Compile / products ++= (`sbt-rewarn-common-2.x` / Compile / products).value
    )
  )
  .sbtScriptedRow(`sbt-1.0.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.1.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.2.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.3.13`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.latest`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-2.0.0`, `sbt-2.x`)

lazy val root = project
  .withId("sbt-rewarn")
  .in(file("."))
  .aggregate(`sbt-rewarn-adapter-lsp`)
  .aggregate(`sbt-rewarn`.projectRefs: _*)
  .settings(
    publish / skip := true,
    compile / skip := true
  )
