import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "firefly-ui"
    val appVersion      = "0.1-SNAPSHOT"


    val appDependencies = Seq(
     "com.nevilon.firefly" % "firefly-core" % "0.1-SNAPSHOT"
    
  // Add your project dependencies here,
    )


    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
     resolvers += (
    "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
)
      
// Add your own project settings here      
    )

}
