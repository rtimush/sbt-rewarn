import SbtAxis.RichProjectMatrix

ThisBuild / organization := "com.timushev.sbt"
ThisBuild / homepage     := Some(url("https://github.com/rtimush/sbt-rewarn"))
ThisBuild / licenses += (("BSD 3-Clause", url("https://github.com/rtimush/sbt-rewarn/blob/master/LICENSE")))
ThisBuild / publishTo := sonatypePublishToBundle.value
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

sonatypeProfileName := "com.timushev"

ThisBuild / scalacOptions := Seq("-deprecation", "-unchecked", "-feature")

Global / scriptedLaunchOpts += s"-Dsbt.rewarn.version=${version.value}"

lazy val `sbt-1.x`    = SbtAxis("1.x", "1.1.5")
lazy val `sbt-latest` = SbtAxis()
lazy val `sbt-1.3.13` = SbtAxis("1.3.13")
lazy val `sbt-1.2.0`  = SbtAxis("1.2.0")
lazy val `sbt-1.1.0`  = SbtAxis("1.1.0")
lazy val `sbt-1.0.0`  = SbtAxis("1.0.0")

lazy val `sbt-rewarn-common` = (project in file("common"))
  .disablePlugins(GitVersioningPlugin)
  .settings(
    scalaVersion                           := `sbt-1.x`.scalaVersion,
    libraryDependencies += "org.scala-sbt" %% "main" % `sbt-1.x`.fullVersion.value % Provided,
    publish / skip                         := true
  )

lazy val `sbt-rewarn-adapter-lsp` = (project in file("adapters/lsp"))
  .dependsOn(`sbt-rewarn-common`)
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
        Compile / products ++= (`sbt-rewarn-common` / Compile / products).value,
        Compile / products ++= (`sbt-rewarn-adapter-lsp` / Compile / products).value
      )
  )
  .sbtScriptedRow(`sbt-1.0.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.1.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.2.8`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.3.13`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-latest`, `sbt-1.x`)

lazy val root = project
  .withId("sbt-rewarn")
  .in(file("."))
  .aggregate(`sbt-rewarn-adapter-lsp`)
  .aggregate(`sbt-rewarn`.projectRefs: _*)
  .settings(
    publish / skip := true,
    compile / skip := true
  )
