package com.breaktru.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import java.io.IOException




@SpringBootApplication
class Application

fun main(args: Array<String>) {
//    if (args.isEmpty()) {
//        TextAreaOutputStreamTest.mainRunner(args);
//    }
    SpringApplication.run(Application::class.java, *args)
//    openHomePage();
}

@Throws(IOException::class)
fun openHomePage() {
    val rt = Runtime.getRuntime()
    rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8888")
}