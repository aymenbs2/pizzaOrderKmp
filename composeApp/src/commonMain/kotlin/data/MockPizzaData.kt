package data

import com.aymendev.pizzaorder.data.Pizza
import com.aymendev.pizzaorder.data.PizzaSupplement
import pizzaoderkmp.composeapp.generated.resources.Res
import pizzaoderkmp.composeapp.generated.resources.capres
import pizzaoderkmp.composeapp.generated.resources.champinion
import pizzaoderkmp.composeapp.generated.resources.ham_pizza
import pizzaoderkmp.composeapp.generated.resources.mk_pizza
import pizzaoderkmp.composeapp.generated.resources.pizza
import pizzaoderkmp.composeapp.generated.resources.potato
import pizzaoderkmp.composeapp.generated.resources.red_piment

object MockPizzaData {


    val supplements= listOf(
        PizzaSupplement(id = 0,name="Pimenent", image = Res.drawable.red_piment,1F),
        PizzaSupplement(id = 1,name="Potato", image = Res.drawable.potato,1F),
        PizzaSupplement(id = 1,name="Capres", image = Res.drawable.capres,1F,),
        PizzaSupplement(id = 2,name="Champinion", image = Res.drawable.champinion,1F)
    )
    val pizzas= listOf(
        Pizza(id = 0, name = "New Orleans Pizza", image = Res.drawable.pizza,rate = 4, price= 16F),
        Pizza(id = 1, name = "Ham Pizza", image = Res.drawable.ham_pizza,rate = 5, price = 15f),
        Pizza(id = 2, name = "Harissa Pizza", image = Res.drawable.mk_pizza, rate = 3, price = 19f),
        Pizza(id = 3, name = "Bambo Pizza", image = Res.drawable.pizza, rate = 5, price = 17f),
        Pizza(id = 3, name = "Mlokhia Pizza", image = Res.drawable.pizza, rate = 4, price = 18f),

        )
}