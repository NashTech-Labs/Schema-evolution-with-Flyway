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

import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import slick.jdbc.PostgresProfile.api._
import slick.util.AsyncExecutor

import scala.concurrent.ExecutionContext

class SqlRepositoryInstantiator(dbConfig: Config)(implicit ec: ExecutionContext) {

  private val DEFAULT_CONNECTION_TIMEOUT: Int = 5000
  private val DEFAULT_QUEUE_SIZE: Int         = 1000

  private val dbMaxConnection: Int = dbConfig.getInt("max-connections")

  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(dbConfig.getString("url"))
  hikariConfig.setUsername(dbConfig.getString("username"))
  hikariConfig.setPassword(dbConfig.getString("password"))
  hikariConfig.setDriverClassName(dbConfig.getString("driver"))
  hikariConfig.setMaximumPoolSize(dbMaxConnection)
  hikariConfig.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT)

  val dataSource = new HikariDataSource(hikariConfig)

  private val db: Database = Database.forDataSource(
    dataSource,
    Some(dbMaxConnection),
    AsyncExecutor
      .apply("slick-microsite-async-executor", dbMaxConnection, dbMaxConnection, DEFAULT_QUEUE_SIZE, dbMaxConnection)
  )

  val userRepository: UserRepository = new UserRepository(db)

}
