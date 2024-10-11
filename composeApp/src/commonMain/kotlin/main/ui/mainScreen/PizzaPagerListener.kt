package main.ui.mainScreen

interface PizzaPagerListener {
   fun onPizzaClicked(index: Int)
   fun onScrollToPage(index: Int)
   fun onScrollUpdate(offsetFraction: Float)
}