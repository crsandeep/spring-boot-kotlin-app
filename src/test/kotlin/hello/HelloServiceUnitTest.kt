package hello

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HelloServiceUnitTest {

    @InjectMocks
    lateinit var helloService: HelloService

    @Test
    fun testHelloController() {
        val result = helloService.getHello()
        assertNotNull(result)
        assertEquals("Hello service!", result)
    }
}
