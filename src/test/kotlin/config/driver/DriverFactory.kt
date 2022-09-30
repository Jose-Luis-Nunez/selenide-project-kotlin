package config.driver

import config.driver.Driver.*
import io.github.bonigarcia.wdm.WebDriverManager.chromedriver
import io.github.bonigarcia.wdm.WebDriverManager.edgedriver
import io.github.bonigarcia.wdm.WebDriverManager.firefoxdriver
import org.openqa.selenium.Proxy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.logging.LoggingPreferences
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.safari.SafariOptions
import config.utils.PropertiesReader
import java.util.logging.Level

class DriverFactory {
    private val userAgent = PropertiesReader().getProp("chrome.user.agent") ?: "unsupported"

    fun get(requestedDriver: Driver?): WebDriver {
        return webDriver(requestedDriver)
    }

    private fun webDriver(requestedDriver: Driver?): WebDriver {
        return when (requestedDriver) {
            CHROME_HEADLESS -> chromeHeadless()
            CHROME -> chrome()
            FIREFOX -> firefox()
            FIREFOX_HEADLESS -> firefoxHeadless()
            SAFARI -> safari()
            EDGE -> edge()
            DEFAULT,
            null -> default()
        }.exhaustive
    }

    private fun firefox(): WebDriver {
        firefoxdriver().setup()
        return FirefoxDriver(firefoxOptions())
    }

    private fun firefoxHeadless(): WebDriver {
        firefoxdriver().setup()
        return FirefoxDriver(firefoxOptions().setHeadless(true))
    }

    private fun chrome(): WebDriver {
        val proxyString = "213.136.89.121:80"
        val proxy = Proxy()
        proxy.httpProxy = proxyString
        proxy.sslProxy = proxyString
        chromeOptions().setCapability("proxy", proxy)
        chromedriver().setup()
        return ChromeDriver(chromeOptions())
    }

    private fun chromeHeadless(): WebDriver {
        chromedriver().setup()
        return ChromeDriver(chromeOptions().setHeadless(true))
    }

    private fun safari(): WebDriver {
        return SafariDriver(safariOptions())
    }

    private fun edge(): WebDriver {
        edgedriver().setup()
        return EdgeDriver(edgeOptions())
    }

    private fun default(): WebDriver {
        return webDriver(getDefaultBrowser())
    }

    private fun getDefaultBrowser(): Driver {
        val invokedBrowser = System.getProperty("browser").orEmpty()

        if (invokedBrowser.isNotBlank()) {
            return Driver.byString(invokedBrowser)
        }

        val defaultBrowser: String = PropertiesReader().getProp("default.browser") ?: "unsupported"

        return Driver.byString(defaultBrowser)
    }

    private fun firefoxOptions() = FirefoxOptions().merge(capabilities())
    private fun safariOptions() = SafariOptions().merge(capabilities())
    private fun edgeOptions() = EdgeOptions().merge(capabilities())
    private fun chromeOptions() = ChromeOptions()
        .addArguments("--disable-gpu")
        .addArguments("--dns-prefetch-disable")
        .addArguments("disable-infobars")
        .addArguments("--disable-extensions")
        .addArguments("--disable-dev-shm-usage")
        .addArguments("--start-maximized")
        .addArguments("--disable-popup-blocking")
        .addArguments("--lang=de")
        .addArguments("--incognito")
        .addArguments("--ignore-certificate-errors")
        .addArguments("user-agent=$userAgent")
        .merge(capabilities())

    private fun capabilities(): DesiredCapabilities {
        val logPrefs = LoggingPreferences().apply { enable(LogType.BROWSER, Level.ALL) }

        return DesiredCapabilities().apply {
            setCapability(ChromeOptions.LOGGING_PREFS, logPrefs)
            setCapability("acceptInsecureCerts", true)
        }
    }

    private val <T> T.exhaustive: T
        get() = this
}
