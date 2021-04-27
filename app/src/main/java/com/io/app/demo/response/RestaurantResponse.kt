package com.io.app.demo.response

class RestaurantResponse(
    val status: Int,
    val data:List<RestaurantResponseItem>,
    val message:String

){
    data class RestaurantResponseItem(
        val address: String,
        val city: String,
        val name: String,
        val state: String,
        val storeImage: String,
        val zip: String
    )
}

