import sbt._
import Keys._

object depends {

  lazy val scalazVersion = settingKey[String]("defines the current scalaz version")

  lazy val classycle = Seq("org.specs2" % "classycle" % "1.4.3")

  def compiler(scalaVersion: String) = Seq("org.scala-lang" % "scala-compiler" % scalaVersion)

  def reflect(scalaVersion: String) = Seq("org.scala-lang" % "scala-reflect" % scalaVersion)

  def scalaz(scalazVersion: String) =
    Seq("org.scalaz"        %% "scalaz-core",
        "org.scalaz"        %% "scalaz-effect",
        "org.scalaz"        %% "scalaz-concurrent").map(_ % scalazVersion) ++
      (if (scalazVersion.startsWith("7.1")) Seq("org.scalaz.stream" % "scalaz-stream_2.12.0-M2" % "0.8"    excludeAll(ExclusionRule(organization = "org.scalaz")))
       else                                 Seq("org.scalaz.stream" % "scalaz-stream_2.12.0-M2" % "0.7.3" excludeAll(ExclusionRule(organization = "org.scalaz"))))

  lazy val kindp = Seq("org.spire-math" % "kind-projector" % "0.7.1" cross CrossVersion.binary)

  lazy val scalacheck    = Seq("org.scalacheck" % "scalacheck_2.12.0-M2"   % "1.12  .5")
  lazy val mockito       = Seq("org.mockito"    % "mockito-core"  % "1.9.5")
  lazy val junit         = Seq("junit"          % "junit"         % "4.12")
  lazy val hamcrest      = Seq("org.hamcrest"   % "hamcrest-core" % "1.3")

  def shapeless(scalaVersion: String) =
    Seq("com.chuusai" % "shapeless_2.12.0-M2" % "2.2.5")

  lazy val pegdown = Seq("org.pegdown" % "pegdown" % "1.2.1")

  lazy val testInterface = Seq("org.scala-sbt"  % "test-interface" % "1.0")

  lazy val tagsoup = "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2"

  def paradise(scalaVersion: String) =
    if (scalaVersion.startsWith("2.11") || scalaVersion.startsWith("2.12")) Nil
    else  Seq(compilerPlugin("org.scalamacros" %% "paradise"    % "2.0.1" cross CrossVersion.full),
                             "org.scalamacros" %% "quasiquotes" % "2.0.1")

  lazy val resolvers =
    Seq(updateOptions := updateOptions.value.withCachedResolution(true)) ++ {
      sbt.Keys.resolvers ++=
      Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots"),
        Resolver.typesafeIvyRepo("releases"),
        "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases")
    }

}


