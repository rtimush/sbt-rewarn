import SbtAxis.RichProjectMatrix
import com.rallyhealth.sbt.versioning.SnapshotVersion

ThisBuild / name := "sbt-updates"
ThisBuild / organization := "com.timushev.sbt"
ThisBuild / isSnapshot := (ThisBuild / versionFromGit).value.isInstanceOf[SnapshotVersion]
ThisBuild / version := (ThisBuild / version).value.replaceAll("""-SNAPSHOT$""", "")
ThisBuild / homepage := Some(url("https://github.com/rtimush/sbt-updates"))
ThisBuild / licenses += (("BSD 3-Clause", url("https://github.com/rtimush/sbt-updates/blob/master/LICENSE")))

ThisBuild / scalacOptions := Seq("-deprecation", "-unchecked", "-feature")

Global / scriptedLaunchOpts += s"-Dsbt.rewarn.version=${version.value}"

lazy val `sbt-1.x`      = SbtAxis("1.x", "1.1.5")
lazy val `sbt-1.4.0-M2` = SbtAxis("1.4.0-M2")
lazy val `sbt-1.3.13`   = SbtAxis("1.3.13")
lazy val `sbt-1.2.0`    = SbtAxis("1.2.0")
lazy val `sbt-1.1.0`    = SbtAxis("1.1.0")
lazy val `sbt-1.0.0`    = SbtAxis("1.0.0")

lazy val publishSettings = Seq(
  publishMavenStyle := false,
  bintrayRepository := (if (isSnapshot.value) "sbt-plugin-snapshots" else "sbt-plugins"),
  bintrayOrganization in bintray := None,
  bintrayReleaseOnPublish := isSnapshot.value
)

lazy val `sbt-rewarn-common` = (project in file("common"))
  .disablePlugins(GitVersioningPlugin)
  .settings(
    scalaVersion := `sbt-1.x`.scalaVersion,
    libraryDependencies += "org.scala-sbt" %% "main" % `sbt-1.x`.fullVersion % Provided,
    publish / skip := true
  )

lazy val `sbt-rewarn-adapter-lsp` = (project in file("adapters/lsp"))
  .dependsOn(`sbt-rewarn-common`)
  .disablePlugins(GitVersioningPlugin)
  .settings(
    scalaVersion := `sbt-1.x`.scalaVersion,
    libraryDependencies += "org.scala-sbt" %% "main" % `sbt-1.3.13`.fullVersion % Provided,
    publish / skip := true
  )

lazy val `sbt-rewarn-adapter-bsp` = (project in file("adapters/bsp"))
  .dependsOn(`sbt-rewarn-common`)
  .disablePlugins(GitVersioningPlugin)
  .settings(
    scalaVersion := `sbt-1.x`.scalaVersion,
    libraryDependencies += "org.scala-sbt" %% "main" % `sbt-1.4.0-M2`.fullVersion % Provided,
    publish / skip := true
  )

lazy val `sbt-rewarn` = (projectMatrix in file("plugin"))
  .sbtPluginRow(
    `sbt-1.x`,
    _.dependsOn(`sbt-rewarn-adapter-lsp` % Provided, `sbt-rewarn-adapter-bsp` % Provided)
      .settings(
        Compile / products ++= (`sbt-rewarn-common` / Compile / products).value,
        Compile / products ++= (`sbt-rewarn-adapter-lsp` / Compile / products).value,
        Compile / products ++= (`sbt-rewarn-adapter-bsp` / Compile / products).value
      )
      .settings(publishSettings)
  )
  .sbtScriptedRow(`sbt-1.0.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.1.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.2.0`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.3.13`, `sbt-1.x`)
  .sbtScriptedRow(`sbt-1.4.0-M2`, `sbt-1.x`)

lazy val root = project
  .withId("sbt-rewarn")
  .in(file("."))
  .aggregate(`sbt-rewarn-adapter-lsp`, `sbt-rewarn-adapter-bsp`)
  .aggregate(`sbt-rewarn`.projectRefs: _*)
  .settings(
    publish / skip := true,
    compile / skip := true
  )
