package de.kp.wooc.insight.actor
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

import de.kp.wooc.insight.{RemoteContext,WooContext}
import de.kp.wooc.insight.io.RequestBuilder

import de.kp.wooc.insight.model._

class FeedWorker(ctx:RemoteContext) extends WorkerActor(ctx) {
  
  private val wtx = new WooContext()  
  override def receive = {
    
    case req:ServiceRequest => {

      val origin = sender
      val service = req.service
      
      try {
        
        req.task.split(":")(1) match {
          
          case "order" => {
            /*
             * Retrieve orders from a certain woocommerce store and
             * convert them into an internal format 
             */
            val orders = wtx.getOrders(req)

            val uid = req.data("uid")      
            val data = Map("uid" -> uid, "message" -> Messages.TRACKED_DATA_RECEIVED(uid))
      
            val response = new ServiceResponse(req.service,req.task,data,ResponseStatus.SUCCESS)	
            origin ! Serializer.serializeResponse(response)

            /*
             * Build tracking requests to send the collected orders
             * to the respective service or engine; the orders are
             * sent independently following a fire-and-forget strategy
             */
            for (order <- orders) {
            
              val request = new RequestBuilder().build(req,order)           
              val message = Serializer.serializeRequest(request)
      
              ctx.send(service,message)
            
            }            
          }
          
          case "product" => {
            
            // TODO
            
          }
            
          case _ => {/* do nothing */}
          
        }
        
      } catch {
        case e:Exception => origin ! failure(req,e.getMessage)

      }
      
    }
    
  }
  override def getResponse(service:String,message:String) = ctx.send(service,message).mapTo[String] 

}