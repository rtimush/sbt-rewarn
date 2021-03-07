import sbt.complete.DefaultParsers._

scalaVersion := "2.12.13"
scalacOptions := Seq("-Xlint:unused")

InputKey[Unit]("contains") := {
  val expected = (OptSpace ~> StringBasic).parsed
  val file1    = baseDirectory.value / "target/streams/_global/_global/_global/streams/out"
  val file2    = baseDirectory.value / "target/streams/$global/$global/$global/streams/out"
  val file3    = baseDirectory.value / "target/streams/compile/compile/$global/streams/out"
  val file4    = baseDirectory.value / "target/streams/compile/compile/_global/streams/out"
  val content = Seq(
    if (file1.exists()) IO.read(file1) else "",
    if (file2.exists()) IO.read(file2) else "",
    if (file3.exists()) IO.read(file3) else "",
    if (file4.exists()) IO.read(file4) else ""
  ).mkString("")
  if (!content.contains(expected))
    sys.error(s"Output did not contain '$expected':\n$content")
}

TaskKey[Unit]("wipe") := {
  val file1 = baseDirectory.value / "target/streams/_global/_global/_global/streams/out"
  val file2 = baseDirectory.value / "target/streams/$global/$global/$global/streams/out"
  val file3 = baseDirectory.value / "target/streams/compile/compile/$global/streams/out"
  val file4 = baseDirectory.value / "target/streams/compile/compile/_global/streams/out"
  file1.delete()
  file2.delete()
  file3.delete()
  file4.delete()
}
