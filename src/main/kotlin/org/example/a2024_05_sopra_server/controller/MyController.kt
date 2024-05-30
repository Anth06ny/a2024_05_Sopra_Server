package org.example.a2024_05_sopra_server.controller

import jakarta.servlet.http.HttpSession
import org.example.a2024_05_sopra_server.StudentBean
import org.example.a2024_05_sopra_server.services.StudentService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MyController {

    //http://localhost:8080/form
    @GetMapping("/form") //Affiche le formulaire
    fun form(studentBean: StudentBean, httpSession: HttpSession ): String {
        println("/form studentBean=$studentBean sessionid=${httpSession.id}")
        return "studentForm" //Lance studentForm.html
    }

    @PostMapping("/formSubmit") //traite le formulaire
    fun formSubmit(studentBean: StudentBean, redirect:RedirectAttributes ) : String {
        println("/formSubmit : $studentBean")
        try {
            if (studentBean.name.isBlank()) {
                throw Exception("Nom manquant")
            }
            //Cas qui marche
            redirect.addFlashAttribute("studentBean", studentBean)
            return "redirect:formResult" // Redirection sur /formResult
        } catch (e:Exception) {
            e.printStackTrace()

            //Cas d'erreur
            redirect.addFlashAttribute("errorMessage", e.message)
            redirect.addFlashAttribute("studentBean", studentBean)
            return "redirect:form" //Redirige sur /form
        }
    }

    @GetMapping("/formResult") //Affiche la page résultat
    fun formResult(studentBean: StudentBean): String {
           println("/formResult studentBean=$studentBean")

        return "studentFormResult" //Lance studentForm.html
    }

    /* -------------------------------- */
    // Formulaire
    /* -------------------------------- */

    /* -------------------------------- */
    //
    /* -------------------------------- */

    //http://localhost:8080/hello
    @GetMapping("/hello")
    fun testHello(model: Model): String {
        println("/hello")


        model.addAttribute("text", "Bonjour")

        val student = StudentBean("Toto", 12)
        model.addAttribute("studentBean", student)

        val list = arrayListOf(
            StudentBean("Tata", 4),
            StudentBean("Bobby", 5),
            StudentBean("Coco", 6)
        )
        model.addAttribute("studentList", list)



        return "welcome"
    }


    //http://localhost:8080/add?name=John&note=15
    @GetMapping("/add")
    fun add(model: Model, name: String = "", note: Int = 0): String {
        println("/add name=$name note=$note")

        val student = StudentBean(name, note)
        StudentService.save(student)

        model.addAttribute("text", "Ajout de :")
        model.addAttribute("studentBean", student)

        model.addAttribute("studentList", StudentService.load())

        return "welcome"
    }

    //http://localhost:8080/filter?name=John&note=15
    @GetMapping("/filter")
    fun filter(model: Model, name: String? = null, note: Int? = null): String {
        println("/filter name=$name note=$note")

        model.addAttribute("text", "Recherche : name=$name note=$note")
        model.addAttribute("studentBean", null)

        val filterList = StudentService.load().filter {
            name == null || it.name.equals(name, true)
        }.filter { note == null || it.note == note }

        model.addAttribute("studentList", filterList)

        return "welcome"
    }
}