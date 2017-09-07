
val catsVersion = "1.0.0-MF"

enablePlugins(ScalaJSPlugin)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.toddy",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Web Warrior",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core"       % catsVersion,
      "org.typelevel" %%% "cats-macros"     % catsVersion,
      "org.typelevel" %%% "cats-kernel"     % catsVersion,
      "org.typelevel" %%% "cats-free"       % catsVersion,
      "be.doeraene"   %%% "scalajs-jquery"  % "0.9.1"
    )
  )

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

skip in packageJSDependencies := false
jsDependencies ++= Seq(
  "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js"
)
