package de.kp.wooc.insight
/* Copyright (c) 2014 Dr. Krusche & Partner PartG
 * 
 * This file is part of the WooCommerce-Insight project
 * (https://github.com/skrusche63/woocommerce-insight).
 * 
 * WooCommerce-Insight is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * WooCommerce-Insight is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * WooCommerce-Insight. 
 * 
 * If not, see <http://www.gnu.org/licenses/>.
 */

import org.joda.time.format.DateTimeFormat
import de.kp.spark.core.model._

import de.kp.wooc.insight.model._

class WooContext {

  private val (secret,key,url) = Configuration.woocommerce
  private val client = new WooClient(secret,key,url)
  
  /**
   * This method is responsible for retrieving a set of orders representing
   * a certain time period; in order to e.g. fill a transaction darabase for
   * later data mining and predictive analytics, this method may be called
   * multiple times (e.g. with the help of a scheduler)
   */
  def getOrders(req:ServiceRequest):List[Order] = {
	  // TODO
    null
  }


}