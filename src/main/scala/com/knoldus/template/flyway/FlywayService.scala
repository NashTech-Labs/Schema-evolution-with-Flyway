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
package com.knoldus.template.flyway

import com.typesafe.config.Config
import org.flywaydb.core.Flyway

import scala.util.Try

class FlywayService(DBConfig: Config) {

  private val url      = DBConfig.getString("url")
  private val user     = DBConfig.getString("username")
  private val password = DBConfig.getString("password")

  private val flyway =
    Flyway.configure
      .dataSource(url, user, password)
      .group(true)
      .outOfOrder(true)
      .baselineOnMigrate(true)

  def migrateDatabaseSchema(): Int = Try(flyway.load().migrate().migrationsExecuted).getOrElse {
    flyway.load().repair()
    flyway.load().migrate().migrationsExecuted
  }

  def dropDatabase(): Unit = flyway.load().clean()
}
