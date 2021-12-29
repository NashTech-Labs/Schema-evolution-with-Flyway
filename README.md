# Schema evolution with Flyway using SBT


In this project, I will show you that how you can evolve/migrate your DB table with Flyway in an SBT project.

---

First you have to add the flyway dependency for sbt in your project.
i.e.
```
  "org.flywaydb" % "flyway-core" % "8.2.0"
```

After this, you have to create the SQL script for creating your table.
The file name of the SQL script must start with `V(version no. of evolution script)__`

For example:-
```
V1__create_table.sql
V2__alter_table.sql
```
First I create the table in `V1__create_table.sql` then I add a column in the table using the `ALTER` in `V2__alter_table.sql` file.

Now you have to put these file in folder. The default path for the Flyway is `resources/db/migration`

After all this now you have to configure the Flyway with the database configuration.
```
Flyway.configure
      .dataSource(url, user, password)
      .group(true)
      .outOfOrder(true)
      .baselineOnMigrate(true)
```
here `url` is the url for database, `user` is the username for database, and `password` is password for the particular user of db.

after this now you have to load the Flyway.

The one way to load the Flyway is that you have to run a terminal command but here we are loading the Flyway with the factory method so that it will create and evolve our table when we run our project without running any external command on terminal.
```
flyway.load().migrate().migrationsExecuted
```
above method will load your Flyway and execute the migration.



To test the code simply run `sbt test`