package pageobject

import com.codeborne.selenide.Selectors.byName
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.SelenideElement

class GooglePage {
    private val searchField: SelenideElement = `$`(byName("q"))

    fun searchFor(text: String?) {
        searchField.`val`(text).pressEnter()
    }
}
