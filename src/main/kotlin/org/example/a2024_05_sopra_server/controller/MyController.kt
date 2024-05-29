package org.example.a2024_05_sopra_server.controller

import org.example.a2024_05_sopra_server.StudentBean
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MyController {

    //http://localhost:8080/hello
    @GetMapping("/hello")
    fun testHello(model: Model): String {
        println("/hello")


        model.addAttribute("text", "Bonjour")

        val student = StudentBean("Toto", 12)
        model.addAttribute("studentBean", null)

        val list = arrayListOf(
            StudentBean("Tata", 4),
            StudentBean("Bobby", 5),
            StudentBean("Coco", 6)
        )
        model.addAttribute("studentList", list)



        return "welcome"
    }


}