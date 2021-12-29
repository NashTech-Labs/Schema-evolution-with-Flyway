ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "Schema-evolution-with-Flyway"
  )


libraryDependencies ++= Seq(
  "com.typesafe.akka"             %% "akka-actor"                       % "2.6.15",
  "com.typesafe.slick"            %% "slick"                            % "3.3.3",
  "org.postgresql"                 % "postgresql"                       % "42.2.23",
  "com.zaxxer"                     % "HikariCP"                         % "4.0.3",
  "org.flywaydb"                   % "flyway-core"                      % "8.2.0",
  "com.dimafeng"                  %% "testcontainers-scala-scalatest"   % "0.39.5"    % "test",
  "org.scalatest"                 %% "scalatest"                        % "3.2.9"     % Test,
  "com.dimafeng"                  %% "testcontainers-scala-postgresql"  % "0.39.5"    % Test
)