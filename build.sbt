name := "geotrellis-sbt-template"
version := "0.1.0"
scalaVersion := "2.11.8"

organization := "com.azavea"
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Yinline-warnings",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:existentials",
  "-feature"
)

resolvers += Resolver.bintrayRepo("azavea", "geotrellis")

libraryDependencies ++= Seq(
  "com.azavea.geotrellis" %% "geotrellis-raster" % "1.0.0-f510e5a",
  "org.scalatest"       %%  "scalatest"      % "2.2.0" % "test"
)

// When creating fat jar, remote some files with
// bad signatures and resolve conflicts by taking the first
// versions of shared packaged types.
assemblyMergeStrategy in assembly := {
  case "reference.conf" => MergeStrategy.concat
  case "application.conf" => MergeStrategy.concat
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case "META-INF\\MANIFEST.MF" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.SF" => MergeStrategy.discard
  case _ => MergeStrategy.first
}
