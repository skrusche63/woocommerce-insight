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
import de.kp.wooc.insight.io.ItemBuilder

import scala.collection.mutable.HashMap

class WooContext {

  private val site = Configuration.site
  
  private val (secret,key,url) = Configuration.woocommerce
  private val client = new WooClient(secret,key,url)
  
  /**
   * This method is responsible for retrieving a set of orders representing
   * a certain time period; in order to e.g. fill a transaction darabase for
   * later data mining and predictive analytics, this method may be called
   * multiple times (e.g. with the help of a scheduler)
   */
  def getOrders(req:ServiceRequest):List[Order] = {
    
    val params = validateOrderParams(req.data)
    val orders = client.getOrders(params).orders
    
    orders.map(order => {
      
      val items = ItemBuilder.build(site,order)
      new Order(items)
      
    })
  }
  
  def getProduct(pid:Int) = client.getProduct(pid)

  /**
   * This method is used to format a certain timestamp, provided with 
   * a request to collect data from a certain Shopify store
   */
  private def formatted(time:Long):String = {

    //2008-12-31
    val pattern = "yyyy-MM-dd"
    val formatter = DateTimeFormat.forPattern(pattern)
    
    formatter.print(time)
    
  }

  /**
   * A helper method to transform the request parameters into validated params
   */
  private def validateOrderParams(params:Map[String,String]):Map[String,String] = {

    val requestParams = HashMap.empty[String,String]
    
    if (params.contains("created_at_min")) {
      /*
       * Show orders created after date (format: 2008-12-31)
       */
      val time = params("created_at_min").toLong
      requestParams += "created_at_min" -> formatted(time)
      
    }
    
    if (params.contains("created_at_max")) {
      /*
       * Show orders created before date (format: 2008-12-31)
       */
      val time = params("created_at_max").toLong
      requestParams += "created_at_max" -> formatted(time)
      
    }
    
    if (params.contains("updated_at_min")) {
      /*
       * Show orders last updated after date (format: 2008-12-31)
       */
      val time = params("updated_at_min").toLong
      requestParams += "updated_at_min" -> formatted(time)
      
    }
    
    if (params.contains("updated_at_max")) {
      /*
       * Show orders last updated before date (format: 2008-12-31)
       */
      val time = params("updated_at_max").toLong
      requestParams += "updated_at_max" -> formatted(time)
      
    }
    
    requestParams.toMap
  
  }

}