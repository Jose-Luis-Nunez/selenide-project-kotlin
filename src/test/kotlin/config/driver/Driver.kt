package config.driver

enum class Driver(val value: String) {
    FIREFOX("firefox"),
    FIREFOX_HEADLESS("firefox-headless"),
    CHROME("chrome"),
    CHROME_HEADLESS("chrome-headless"),
    SAFARI("safari"),
    EDGE("edge"),
    DEFAULT("");

    companion object {
        fun byString(browserString: String) = values().find { it.value == browserString }
            ?: throw RuntimeException("invalid browser '$browserString' requested")
    }
}
