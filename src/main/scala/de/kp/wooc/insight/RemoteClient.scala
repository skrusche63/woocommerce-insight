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

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.Duration._
import scala.concurrent.duration.DurationInt

import scala.concurrent.Future

class RemoteClient(service:String) {

  private val path = "client.conf"    
  private val conf = ConfigFactory.load(path)

  private val duration = conf.getConfig(service).getInt("timeout")
  implicit val timeout = Timeout(DurationInt(duration).second)
  
  private val url = Registry.get(service)
    
  private val system = ActorSystem(service, conf)
  private val remote = system.actorSelection(url)

  def send(req:Any):Future[Any] = ask(remote, req)    
  def shutdown() = system.shutdown

}