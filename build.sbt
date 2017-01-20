//import de.surfice.angulate2.sbtplugin.Angulate2Plugin.autoImport.ngBootstrap
import com.scalapenos.sbt.prompt.SbtPrompt.autoImport.{promptTheme, _}

name := "trucking-frontend"

version := "0.3"

lazy val scalaV = "2.11.8"
lazy val ngVersion = "2.4.3"

lazy val webapp = (project in file(".")).
  settings(

//    name := "scalapenos-theme",
//    version := "0.1",
//    description := """This project has been configured with the fancy Scalapenos theme, which
//                      needs special font support to render correctly.
//                      See the README for more details.""",
//    startYear := Some(2014),
//    homepage := Some(url("https://github.com/agemooij/sbt-prompt")),
//    organization := "com.scalapenos",
//    organizationHomepage := Some(url("http://scalapenos.com/")),
//    licenses := Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
    promptTheme := ScalapenosTheme
  )

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := Seq(map),

  unmanagedResources in Assets ++= Seq(
    baseDirectory.value / "../trucking-map/target/scala-2.11/trucking-map-sjsx.js",
    baseDirectory.value / "../trucking-map/target/scala-2.11/trucking-map-fastop.js"
  ),
  unmanagedResourceDirectories in Assets += baseDirectory.value / "../trucking-map/src/main/resources",

  pipelineStages in Assets := Seq(scalaJSPipeline),
  //pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
  //compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (map, Compile)),
  libraryDependencies ++= Seq(
    filters,
    cache,
    ws,
    "com.vmunier" %% "scalajs-scripts" % "1.0.0",
    "org.webjars" %% "webjars-play" % "2.5.0",

    //angular2 dependencies
    "org.webjars.npm" % "angular__common" % ngVersion,
    "org.webjars.npm" % "angular__compiler" % ngVersion,
    "org.webjars.npm" % "angular__core" % ngVersion,
    "org.webjars.npm" % "angular__http" % ngVersion,
    "org.webjars.npm" % "angular__forms" % ngVersion,
    "org.webjars.npm" % "angular__platform-browser-dynamic" % ngVersion,
    "org.webjars.npm" % "angular__platform-browser" % ngVersion,
    "org.webjars.npm" % "angular__router" % "3.4.3",

    "org.webjars.npm" % "systemjs" % "0.19.41",
    "org.webjars.npm" % "rxjs" % "5.0.3",
    "org.webjars.npm" % "reflect-metadata" % "0.1.9",
    "org.webjars.npm" % "zone.js" % "0.6.26",
    "org.webjars.npm" % "core-js" % "2.4.1",
    "org.webjars.npm" % "symbol-observable" % "1.0.4",

    "org.webjars.bower" % "compass-mixins" % "0.12.10",
    "org.webjars.bower" % "bootstrap-sass" % "3.3.6"
  ),

  promptTheme := ScalapenosTheme,
  shellPrompt := (state ⇒ promptTheme.value.render(state))
).enablePlugins(PlayScala)

lazy val map = (project in file("trucking-map")).settings(
  name := "trucking-map",
  scalaVersion := scalaV,
  //persistLauncher := true,
  //persistLauncher in Test := false,
  resolvers += "jitpack" at "https://jitpack.io",
  libraryDependencies ++= Seq(
    //"org.scala-js" %%% "scalajs-dom" % "0.9.1",
    "com.hortonworks.orendainx" %% "trucking-shared" % "0.3",
    "com.github.fancellu.scalajs-leaflet" % "scalajs-leaflet_sjs0.6_2.11" % "v0.1",
    "io.github.cquiroz" %%% "scala-java-time" % "2.0.0-M6"
  ),
  jsDependencies ++= Seq(
    "org.webjars.npm" % "leaflet" % "1.0.2" / "leaflet.js" commonJSName "Leaflet"
  ),
  //ngBootstrap := Some("Frontend") //qualified name (including packages) of Scala class called NAME_OF_THE_MODULE_TO_BOOTSTRAP
  ngBootstrap := Some("com.hortonworks.orendainx.trucking.webapp.AppModule") //qualified name (including packages) of Scala class called NAME_OF_THE_MODULE_TO_BOOTSTRAP
).enablePlugins(ScalaJSPlugin, ScalaJSWeb, Angulate2Plugin)

// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
