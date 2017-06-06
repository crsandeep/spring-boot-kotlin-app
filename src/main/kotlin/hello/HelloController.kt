package hello

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("hello")
class HelloController {

    @Autowired
    lateinit var helloService: HelloService

    @GetMapping("/string")
    fun helloString() = "Hello string!"

    @GetMapping("/service")
    fun helloService() = helloService.getHello()

    @GetMapping("/data")
    fun helloData() = Hello("Hello data!")
}
