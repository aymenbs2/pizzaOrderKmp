package com.aymendev.pizzaorder.data

data class PizzaComposition(var pizza: Pizza?=null, val pizzaIngredient: MutableList<PizzaSupplement>?=null) {
}