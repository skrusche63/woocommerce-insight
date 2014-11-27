package de.kp.wooc.insight.model
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

import org.codehaus.jackson.annotate.JsonProperty
import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonIgnore}

@JsonIgnoreProperties(ignoreUnknown = true)
case class WooShippingAddress (
   
  @JsonProperty("first_name")
  first_name:String,

  @JsonProperty("last_name")
  last_name:String,
  
  @JsonProperty("company")
  company:String,

  @JsonProperty("address_1")
  address_1:String,

  @JsonProperty("address_2")
  address_2:String,

  @JsonProperty("city")
  city:String,

  @JsonProperty("state")
  state:String,

  @JsonProperty("postcode")
  postcode:String,

  @JsonProperty("country")
  country:String,

  @JsonProperty("email")
  email:String,

  @JsonProperty("phone")
  phone:String

)
