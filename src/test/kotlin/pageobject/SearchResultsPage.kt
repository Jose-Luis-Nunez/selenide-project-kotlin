package pageobject

import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selenide


class SearchResultsPage {
    private val results = Selenide.`$$`(".results .result")

    fun checkResultsSizeIsAtLeast(expectedSize: Int) {
        results.shouldHave(CollectionCondition.sizeGreaterThan(expectedSize))
    }

    fun checkResultHasTest(index: Int, expectedText: String?) {
        results[index].shouldHave(Condition.text(expectedText))
    }
}