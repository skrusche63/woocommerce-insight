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

import com.typesafe.config.ConfigFactory

object Configuration {

  val path = "application.conf"
  val config = ConfigFactory.load(path)

  def actor():(Int,Int) = {
  
    val cfg = config.getConfig("actor")
    
    val heartbeat = cfg.getInt("heartbeat")
    val timeout = cfg.getInt("timeout")
    
    (heartbeat,timeout)
    
  }

  def cache():Int = {
  
    val cfg = config.getConfig("cache")
    val size = cfg.getInt("size")
    
    size
    
  }

  def rest():(String,Int) = {
      
    val cfg = config.getConfig("rest")
      
    val host = cfg.getString("host")
    val port = cfg.getInt("port")

    (host,port)
    
  }

  def router():(Int,Int,Int) = {
  
    val cfg = config.getConfig("router")
  
    val time    = cfg.getInt("time")
    val retries = cfg.getInt("retries")  
    val workers = cfg.getInt("workers")
    
    (time,retries,workers)

  }
  
  def spark():Map[String,String] = {
  
    val cfg = config.getConfig("spark")
    
    Map(
      "spark.executor.memory"          -> cfg.getString("spark.executor.memory"),
	  "spark.kryoserializer.buffer.mb" -> cfg.getString("spark.kryoserializer.buffer.mb")
    )

  }

  def site():String = {
  
    val cfg = config.getConfig("site")
    val site = cfg.getString("name")
    
    site
    
  }
  
  def woocommerce():(String,String,String) = {
  
    val cfg = config.getConfig("woocommerce")
    
    val secret = cfg.getString("secret")
    val key    = cfg.getString("key")

    val url = cfg.getString("url")
    
    (secret,key,url)
    
  }
  
}