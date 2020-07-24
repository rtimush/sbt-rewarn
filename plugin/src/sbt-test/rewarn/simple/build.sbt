import sbt.complete.DefaultParsers._

scalaVersion := "2.12.12"
scalacOptions := Seq("-Xlint:unused")

InputKey[Unit]("contains") := {
  val expected = (OptSpace ~> StringBasic).parsed
  val file1    = baseDirectory.value / "target/streams/_global/_global/_global/streams/out"
  val file2    = baseDirectory.value / "target/streams/$global/$global/$global/streams/out"
  val source   = if (file2.exists()) file2 else file1
  val content  = IO.read(source)
  if (!content.contains(expected))
    sys.error(s"File $source did not contain '$expected':\n$content")
}

TaskKey[Unit]("wipe") := {
  val file1 = baseDirectory.value / "target/streams/_global/_global/_global/streams/out"
  val file2 = baseDirectory.value / "target/streams/$global/$global/$global/streams/out"
  file1.delete()
  file2.delete()
}
