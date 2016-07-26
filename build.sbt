name := "miniAccumuloCluster"

version := "0.1.0"

scalaVersion := "2.11.8"

organization := "geotrellis.minicluster"

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
  "-feature")

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

resolvers ++= Seq(
  "geosolutions" at "http://maven.geo-solutions.it/",
  "osgeo" at "http://download.osgeo.org/webdav/geotools/",
  "boundlessgeo" at "https://boundless.artifactoryonline.com/boundless/main",
  "geowave" at "http://geowave-maven.s3-website-us-east-1.amazonaws.com/snapshot"
)

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "mil.nga.giat" % "geowave-adapter-vector" % "0.9.2-SNAPSHOT",
  "mil.nga.giat" % "geowave-datastore-accumulo" % "0.9.2-SNAPSHOT",
  "org.apache.accumulo" % "accumulo-monitor" % "1.7.0"
)

mainClass in assembly := Some("geotrellis.minicluster.Main")

mainClass in run := Some("geotrellis.minicluster.Main")

assemblyMergeStrategy in assembly := {
  case "reference.conf" => MergeStrategy.concat
  case "application.conf" => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) =>
    xs match {
      case ("MANIFEST.MF" :: Nil) => MergeStrategy.discard
      // Concatenate everything in the services directory to keep
      // GeoTools happy.
      case ("services" :: _ :: Nil) =>
        MergeStrategy.concat
      // Concatenate these to keep JAI happy.
      case ("javax.media.jai.registryFile.jai" :: Nil) | ("registryFile.jai" :: Nil) | ("registryFile.jaiext" :: Nil) =>
        MergeStrategy.concat
      case (name :: Nil) => {
        // Must exclude META-INF/*.([RD]SA|SF) to avoid "Invalid
        // signature file digest for Manifest main attributes"
        // exception.
        if (name.endsWith(".RSA") || name.endsWith(".DSA") || name.endsWith(".SF"))
          MergeStrategy.discard
        else
          MergeStrategy.first
      }
      case _ => MergeStrategy.first
    }
  case _ => MergeStrategy.first
}

