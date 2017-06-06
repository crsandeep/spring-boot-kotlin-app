package hello

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HelloControllerUnitTest {

    @InjectMocks
    lateinit var helloController: HelloController

    @Mock
    lateinit var helloService: HelloService

    @Test
    fun testHelloController() {
        val result = helloController.helloString()
        assertNotNull(result)
        assertEquals("Hello string!", result)
    }

    @Test
    fun testHelloService() {
        doReturn("Hello service!").`when`(helloService).getHello()
        val result = helloController.helloService()
        assertNotNull(result)
        assertEquals("Hello service!", result)
    }

    @Test
    fun testHelloDto() {
        val result = helloController.helloData()
        assertNotNull(result)
        assertEquals(Hello("Hello data!"), result)
    }
}
