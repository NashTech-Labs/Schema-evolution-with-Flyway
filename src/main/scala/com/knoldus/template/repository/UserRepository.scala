/* Copyright (c) 2020 Spendlabs Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Spendlabs
 * Inc. ("Confidential Information").  You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Spendlabs Inc.
 *
 * SPENDLABS INC. MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SPENDLABS INC. WILL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.knoldus.template.repository

import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}

case class UserRow(
                 id       : Option[String],
                 name     : String,
                 username : String,
                 password : String,
               )

//scalastyle:off
class UserRepository(db: Database)(implicit ec: ExecutionContext) {

  class UserTable(tag: Tag) extends Table[UserRow](tag, "user_table") {
    override def * : ProvenShape[UserRow] =
      (
        id,
        name,
        username,
        password
      ).mapTo[UserRow]

    def id : Rep[Option[String]] = column[Option[String]]("user_id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def username : Rep[String] = column[String]("username")
    def password : Rep[String] = column[String]("password")

  }

  val userTable: TableQuery[UserTable]                 = TableQuery[UserTable]

  def insertUserRecord(userRow: UserRow): Future[Unit] = {
    val setup = DBIO.seq(
      //fileTrackerTable.schema.createIfNotExists,
      userTable.insertOrUpdate(userRow)
    )
    db.run(setup)
  }

  def getAll: Future[Option[UserRow]] = {
    db.run(userTable.result.headOption)
  }

}
