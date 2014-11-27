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

import java.io.IOException
import java.net.URLEncoder

import java.util.Formatter
import java.security.MessageDigest

import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

import javax.ws.rs.HttpMethod
import javax.ws.rs.client.{Client,ClientBuilder,Entity,WebTarget}
import javax.ws.rs.core.MediaType

import com.fasterxml.jackson.databind.{Module, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import org.slf4j.{Logger,LoggerFactory}
import org.apache.commons.codec.binary.Base64

import de.kp.wooc.insight.model._

import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

private case class Pair(key:String,value:String)

class WooClient(val secret:String,val key:String, val url:String) {

  private val LOG = LoggerFactory.getLogger(classOf[WooClient])

  private val JSON_MAPPER = new ObjectMapper()  
  JSON_MAPPER.registerModule(DefaultScalaModule)

  private val API_URL:String = "/wc-api/v2"
  private val ENC:String = "HMAC-SHA256"

  private val HASH_ALGORITHM:String = "HmacSHA256"
  
  private val uri = url + API_URL
  private val base64 = new Base64()
  
  val client = ClientBuilder.newClient()
  val webTarget = client.target(uri).path("/")

  def getOrders() {
      
    try {
      
      var queryTarget = webTarget.path("orders")
      if (url.contains("https")) {
        
      } else {

        val millis = String.valueOf(System.currentTimeMillis())
        val params = ArrayBuffer.empty[Pair]
        /*
         * We define the parameters as list to make sure that this
         * ordering is also used when preparing the request url
         */
        params += Pair("oauth_consumer_key",key)
        params += Pair("oauth_timestamp",time())
          
        params += Pair("oauth_nonce",sha1(millis))
        params += Pair("oauth_signature_method",ENC)

        val signature = Pair("oauth_signature", generateOAuthSignature("orders","GET",params))
        
        for (param <- params) {
         queryTarget = queryTarget.queryParam(param.key,param.value)
        }
        
        queryTarget = queryTarget.queryParam(signature.key,signature.value)
        println("URI: " + queryTarget.getUri())
        
        val response = queryTarget.request(MediaType.APPLICATION_JSON_TYPE).method("GET", null, classOf[String])

        JSON_MAPPER.readValue(response, classOf[WooOrders])    
   }
    
    } catch {
      case e:Exception => throw new Exception("Could not process query",e)
    }

  }

  private def time():String = {
	return String.valueOf(new java.util.Date().getTime()).substring(0, 10)
  }

  private def generateOAuthSignature(endpoint:String,method:String,params:ArrayBuffer[Pair]):String = {
  
    val queryParams = ArrayBuffer.empty[String]
    for (param <- params) {
      
      val k = URLEncoder.encode(param.key,"UTF-8").replace("%", "%25")
      val v = URLEncoder.encode(param.value,"UTF-8").replace("%", "%25")
      
      "" + k  + "%3D" + v
    
    }
    
    val queryString = queryParams.mkString("%26")
    val baseURI = URLEncoder.encode(url + API_URL + "/" + endpoint, "UTF-8")

    println("BASE URI: " + baseURI)
    getSignature(baseURI,method,queryString)
    
 }
  
  private def getSignature(url:String,method:String,params:String):String = {
	/*
	 * StringBuilder has three parts that are connected by "&": 
	 * 
	 * 1) protocol 
	 * 2) URL (need to be URLEncoded) 
	 * 3) Parameter List (need to be URLEncoded)
	 */
	val sb = new StringBuilder()
		
	sb.append(method.toUpperCase() + "&")
	sb.append(url)
		
	sb.append("&")
	sb.append(params)

	val keyBytes = secret.getBytes()
	val secretKey = new SecretKeySpec(keyBytes, HASH_ALGORITHM);

	val mac = Mac.getInstance(HASH_ALGORITHM)
	mac.init(secretKey)

	val signature = new String(base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))), "UTF-8").trim()
    signature
      
  }
	
  private def sha1(password:String):String = {
		
    var sha1:String = ""
	
    try {
	 
      val crypt = MessageDigest.getInstance("SHA-1")
	  crypt.reset()
			
	  crypt.update(password.getBytes())
      sha1 = byteToHex(crypt.digest())
      
	} catch {
	  case e:Exception => e.printStackTrace()
	}
	
	return sha1
	
  }

  private def byteToHex(hash:Array[Byte]):String = {
    hash.map{ b => String.format("%02X", new java.lang.Integer(b & 0xff)) }.mkString
  } 

}