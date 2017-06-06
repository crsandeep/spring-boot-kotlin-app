# Building and Testing RESTful Web Services using Kotlin, Spring boot and Spring MVC/JAX-RS

## Goal

In this guide, we will be building a Kotlin RESTful web services using Spring boot and Spring MVC/JAX-RS. We will be adding unit tests and integration tests.

## Setup

### Create a sample app

* Go to [Spring Initializr](https://start.spring.io/) and Generate a `Gradle/Maven` with `Kotlin` and Spring Boot project.
* Select `Web` as dependency to build a Spring MVC RESTful webservice.
* Select `Jersey (JAX-RS)` as dependency to build a JAX-RS RESTful webservice.
* Fill in the `Group` and `Artifact` and click on generate project.

### Import to IntelliJ

Import the extracted folder to [IntelliJ](https://www.jetbrains.com/idea/download/).

## Application Context

Add Spring Boot application context

```
@SpringBootApplication
class HelloApplication

fun main(args: Array<String>) {
    SpringApplication.run(HelloApplication::class.java, *args)
}
```

## Add a REST endpoint class

### Add a controller

**Spring MVC**

```
@RestController
@RequestMapping("hello")
class HelloController {
    @GetMapping("/string")
    fun helloString() = "Hello string!"
}
```

### Add a JAX-RS endpoint

**JAX-RS**

```
@Component
@Path("/hello")
class HelloResource {
    @GET
    @Path("/string")
    fun helloString() = "Hello string!"
}
```

### Add Jersey ResourceConfig

**Note: This is required only for JAX-RS based service. Do not add this for Spring MVC based service.**

Add Jersey ResourceConfig class to your Spring context and register the JAX-RS endpoint class

```
@Component
class HelloConfig : ResourceConfig() {
    init {
        registerEndpoints()
    }

    private fun registerEndpoints() {
        register(HelloResource())
    }
}
```

### Start the application

Start the application from `HelloApplication` class and load `http://localhost:8080/hello/string`

```
Hello string!
```

## Data class

### Add a data class

**Spring MVC and JAX-RS**

```
data class Hello(val message: String)
```

### Add a new REST endpoint

**Spring MVC**

```
@GetMapping("/data")
fun helloData() = Hello("Hello data!")
```

**JAX-RS**

```
@GET
@Path("/data")
@Produces("application/json")
fun helloData() = Hello("Hello data!")
```

### Add the Jackson maven dependency

**Spring MVC and JAX-RS**

**Gradle**

```
compile('com.fasterxml.jackson.module:jackson-module-kotlin')
```

**Maven**

```
<dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-kotlin</artifactId>
</dependency>
```

### Restart the application
    
Restart the application from `HelloApplication` class and load `http://localhost:8080/hello/data`

```
{
    "message":"Hello data!"
}
```

## Service

### Add a service class

**Spring MVC and JAX-RS**

```
@Service
class HelloService {
    fun getHello() = "Hello service!"
}
```

### Add a new REST endpoint

**Spring MVC**

```
@Autowired
lateinit var helloService: HelloService

@GetMapping("/service")
fun helloService() = helloService.getHello()
```

**JAX-RS**

```
@Autowired
lateinit var helloService: HelloService

@GET
@Path("/service")
fun helloService() = helloService.getHello()
```

### Restart the application
    
Restart the application from `HelloApplication` class and load `http://localhost:8080/hello/service`

```
Hello service!
```

## Integration test

**Spring MVC and JAX-RS**

```
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloEndointIntegrationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun testHelloController() {
        val result = testRestTemplate.getForEntity("/hello/string", String::class.java)
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(result.body, "Hello string!")
    }

    @Test
    fun testHelloService() {
        val result = testRestTemplate.getForEntity("/hello/service", String::class.java)
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(result.body, "Hello service!")
    }

    @Test
    fun testHelloDto() {
        val result = testRestTemplate.getForEntity("/hello/data", Hello::class.java)
        assertNotNull(result)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(result.body, Hello("Hello data!"))
    }
}
```

## Unit test

**Spring MVC**


```
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
```

**JAX-RS**


```
@RunWith(MockitoJUnitRunner::class)
class HelloResourceUnitTest {

    @InjectMocks
    lateinit var helloResource: HelloResource

    @Mock
    lateinit var helloService: HelloService

    @Test
    fun testHelloController() {
        val result = helloResource.helloString()
        assertNotNull(result)
        assertEquals("Hello string!", result)
    }

    @Test
    fun testHelloService() {
        doReturn("Hello service!").`when`(helloService).getHello()
        val result = helloResource.helloService()
        assertNotNull(result)
        assertEquals("Hello service!", result)
    }

    @Test
    fun testHelloDto() {
        val result = helloResource.helloData()
        assertNotNull(result)
        assertEquals(Hello("Hello data!"), result)
    }
}
```