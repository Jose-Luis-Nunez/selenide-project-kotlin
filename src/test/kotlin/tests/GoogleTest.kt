package tests

import pageobject.GooglePage

import com.codeborne.selenide.Selenide.*
import org.junit.jupiter.api.Test
import pageobject.SearchResultsPage

class GoogleTest {
    @Test
    fun userCanSearch() {
        open("https://duckduckgo.com")
        GooglePage().searchFor("selenide java")
        val results = SearchResultsPage()
        results.checkResultsSizeIsAtLeast(1)
    }
}
