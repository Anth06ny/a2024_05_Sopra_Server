package org.example.a2024_05_sopra_server.controller

import jakarta.servlet.http.HttpSession
import org.example.a2024_05_sopra_server.StudentBean
import org.example.a2024_05_sopra_server.UserBean
import org.example.a2024_05_sopra_server.services.UserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/user")
class UserController {

    //http://localhost:8080/user/login
    @GetMapping("/login") //Affiche le formulaire
    fun login(userBean: UserBean, httpSession: HttpSession): String {
        println("/login sessionid=${httpSession.id}")

        //Est ce que l'user n'est pas déjà connu par son ssession id
        val userBdd = UserService.findBySessionId(httpSession.id)
        if (userBdd != null) {
            return "redirect:/user/userregister"
        }

        return "login" //Lance studentForm.html
    }

    @PostMapping("/loginSubmit") //traite le formulaire
    fun loginSubmit(
        userBean: UserBean,
        redirect: RedirectAttributes,
        httpSession: HttpSession
    ): String {
        println("/loginSubmit : $userBean")
        try {
            if (userBean.login.isBlank()) {
                throw Exception("Login manquant")
            }
            else if (userBean.password.isBlank()) {
                throw Exception("Password manquant")
            }
            //Cas qui marche
            val userBDD = UserService.findByLogin(userBean.login)

            if (userBDD == null) {
                userBean.sessionId = httpSession.id
                //Si pas en base je l'enregistre en base
                UserService.save(userBean)

                return "redirect:/user/userregister" // Redirection sur /formResult
            }
            else if (userBDD.password == userBean.password) {
                userBDD.sessionId = httpSession.id
                UserService.save(userBDD)
                return "redirect:/user/userregister" // Redirection sur /formResult
            }
            else {
                throw Exception("Mot de passe incorrect")
            }

        }
        catch (e: Exception) {
            e.printStackTrace()

            //Cas d'erreur
            redirect.addFlashAttribute("errorMessage", e.message)
            redirect.addFlashAttribute("userBean", userBean)
            return "redirect:login" //Redirige sur /form
        }
    }

    @GetMapping("/userregister") //Affiche la page résultat
    fun userregister(model: Model, httpSession: HttpSession, redirect: RedirectAttributes): String {
        println("/userregister")

        val userBdd = UserService.findBySessionId(httpSession.id)
        if (userBdd == null) {
            //Acces sans etre enregistré
            redirect.addFlashAttribute("errorMessage", "Veuillez vous reconnecter")
            return "redirect:/user/login"
        }

        model.addAttribute("userBean", userBdd)
        model.addAttribute("userList", UserService.load())

        return "userRegister" //Lance studentForm.html
    }
}