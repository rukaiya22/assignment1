import ie.setu.config.DbConfig
import ie.setu.helpers.DatabaseHelper
import ie.setu.helpers.ServerContainer
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Dimension
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.test.assertEquals

class FrontendUserTest {
    private lateinit var driver: WebDriver
    private lateinit var js: JavascriptExecutor
    private val vars = mutableMapOf<String, Any?>()

    private val db = DbConfig().getDbConnection(true)
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Before
    fun setUp() {
        DatabaseHelper.setupTestDatabase(db)
        driver = ChromeDriver()
        js = driver as JavascriptExecutor
    }

    @After
    fun tearDown() {
        driver.quit()
        DatabaseHelper.cleanupTestDatabase(db)
    }

    @Test
    fun frontendUser() {
        driver.get(origin)
        driver.manage().window().size = Dimension(1936, 1056)
        driver.findElement(By.linkText("Users")).click()
        driver.findElement(By.cssSelector(".form-control:nth-child(3)")).click()
        driver.findElement(By.cssSelector(".form-control:nth-child(3)")).sendKeys("test1")
        driver.findElement(By.cssSelector(".form-control:nth-child(5)")).click()
        driver.findElement(By.cssSelector(".form-control:nth-child(5)")).sendKeys("test1@test.com")
        driver.findElement(By.cssSelector("button:nth-child(7)")).click()

    }
}
