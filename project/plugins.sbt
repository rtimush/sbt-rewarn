resolvers += Resolver.bintrayIvyRepo("rallyhealth", "sbt-plugins")

addSbtPlugin("org.scalameta"       % "sbt-scalafmt"       % "2.4.2")
addSbtPlugin("org.foundweekends"   % "sbt-bintray"        % "0.6.1")
addSbtPlugin("com.rallyhealth.sbt" % "sbt-git-versioning" % "1.4.0")
addSbtPlugin("com.eed3si9n"        % "sbt-projectmatrix"  % "0.6.0")
