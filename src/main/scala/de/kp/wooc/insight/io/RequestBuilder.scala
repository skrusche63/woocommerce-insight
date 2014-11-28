package de.kp.wooc.insight.io
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

import de.kp.spark.core.model._

import de.kp.wooc.insight.model._
import scala.collection.mutable.{ArrayBuffer,HashMap}
/**
 * The RequestBuilder is responsible for building tracking request based
 * on a specific predictive engine and respective woocommerce data
 */
class RequestBuilder {

  def build(req:ServiceRequest,order:Order):ServiceRequest = {
    
    req.service match {
      
      case Services.ASSOCIATION => buildItemRequest(req,order)
      
      case Services.SERIES => buildItemRequest(req,order)
      
      case _ => null
    
    }
    
  }
  
  /**
   * Build service request to track items to estanblish a transaction & sequence
   * database; this method is used by the association & series analysis engine
   */
  private def buildItemRequest(req:ServiceRequest,order:Order):ServiceRequest = {
        
    val task = "track:item"
    val data = HashMap.empty[String,String]
        
    /* The unique identifier of this tracking task */
    data += "uid" -> req.data("uid")
          
    /* Specification of the Elasticsearch index */
    data += "index" -> req.data("index")
    data += "type"  -> req.data("type")
          
    /*Add item specific information */
    val head = order.items.head
        
    data += "site" -> head.site
    data += "user" -> head.user
        
    data += "timestamp" -> head.timestamp.toString
    data += "group" -> head.group
        
    val items = ArrayBuffer.empty[Int]
    items += head.item
        
    for (record <- order.items.tail) {
      items += record.item
    }
        
    data += "item" -> items.mkString(",")
    new ServiceRequest(req.service,task,data.toMap)
    
  }
  
}