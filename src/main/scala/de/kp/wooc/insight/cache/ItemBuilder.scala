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

import de.kp.wooc.insight.model._
import org.joda.time.format.DateTimeFormat

object ItemBuilder {

  /**
   * A public method to extract those data from a woo commerce
   * order that is indexed as a purchase item
   */
  def build(site:String,order:WooOrder):List[OrderItem] = {
    
    /*
     * The unique identifier of a certain order is used
     * for grouping all the respective items; the order
     * identifier is a 'Long' and must be converted into
     * a 'String' representation
     */
    val group = order.id.toString
    /*
     * The datetime this order was created:
     * "2014-11-03T13:51:38-05:00"
     */
    val created_at = order.created_at
    val timestamp = toTimestamp(created_at)
    /*
     * The unique user identifier is retrieved from the
     * customer object and there from the 'id' field
     */
    val user = order.customer.id.toString
    /*
     * Convert all line items of the respective order
     * into 'OrderItem' for indexing
     */
    order.line_items.map(lineItem => {

      val item = lineItem.product_id.toInt
      
      /*
       * In addition, we collect the following data from the line item
       */
      val name = lineItem.name
      val quantity = lineItem.quantity
      
      val currency = order.currency
      
      val sku = lineItem.sku
      
      new OrderItem(site,user,timestamp,group,item,name,quantity,currency,sku)
    
    })

  }
  
  private def toTimestamp(text:String):Long = {
      
    //2014-11-03T13:51:38-05:00
    val pattern = "yyyy-MM-dd'T'HH:mm:ssZ"
    val formatter = DateTimeFormat.forPattern(pattern)
      
    val datetime = formatter.parseDateTime(text)
    datetime.toDate.getTime
    
  }
}