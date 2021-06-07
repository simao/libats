import java.net.URI

import sbt.Keys._
import sbt._

object Publish {
  private def readSettings(envKey: String, propKey: Option[String] = None): String = {
    sys.env
      .get(envKey)
      .orElse(propKey.flatMap(sys.props.get(_)))
      .getOrElse("")
  }

  lazy val repoHost = URI.create(repoUrl).getHost

  lazy val repoUser = readSettings("GITHUB_USER")

  lazy val repoPassword = readSettings("GITHUB_TOKEN")

  lazy val repoUrl = readSettings("PUBLISH_URL")

  lazy val repoRealm = readSettings("PUBLISH_REALM")

  lazy val settings = Seq(
    credentials += Credentials(repoRealm, repoHost, repoUser, repoPassword),
    publishTo := version { v: String =>
      val server = repoUrl
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at server)
      else
        Some("releases"  at server)
    }.value
  )

  lazy val disable = Seq(
    publishArtifact := false,
    publish := (()),
    publishLocal := (())
  )
}
