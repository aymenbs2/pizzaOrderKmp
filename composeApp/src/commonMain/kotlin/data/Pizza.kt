package com.aymendev.pizzaorder.data

import org.jetbrains.compose.resources.DrawableResource

data class Pizza(val id: Int, val name: String,val image:DrawableResource,val rate:Int=0,val price:Float)