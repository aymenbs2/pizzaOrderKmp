package ui.shared


interface BackPressHandler {
    fun onBackPressed()

}

object SharedBackPressHandler : BackPressHandler {
    lateinit var backPress: BackPressHandler
    var enabled = false
    override fun onBackPressed() {
        backPress.onBackPressed()
    }
}