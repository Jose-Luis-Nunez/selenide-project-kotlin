package tests

import com.codeborne.selenide.Selenide
import org.junit.jupiter.api.Test
import pageobject.GooglePage
import pageobject.SearchResultsPage

class Check24Test {
    @Test
    fun userCanSearch() {
        Selenide.open("https://duckduckgo.com")
        GooglePage().searchFor("selenide java")
        val results = SearchResultsPage()
        results.checkResultsSizeIsAtLeast(1)
    }
}