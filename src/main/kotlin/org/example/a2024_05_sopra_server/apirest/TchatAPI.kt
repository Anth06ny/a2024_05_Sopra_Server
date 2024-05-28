package org.example.a2024_05_sopra_server.apirest

import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import jakarta.validation.Validator
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.example.a2024_05_sopra_server.StudentBean
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tchat")
class TchatAPI {

    val list = ArrayList<MessageBean>()
        //Jeu de donnée
    init {
        //repeat(20) {
            list.add(MessageBean("Toto", "Coucou"))
            list.add(MessageBean("Tata", "hello"))
            list.add(MessageBean("Toto", "hello"))
            list.add(MessageBean("Tata", "Coucou"))
        //}
    }


    //http://localhost:8080/tchat/saveMessage
    //Json Attendu : {"pseudo": "toto","message": "hello""}
    @PostMapping("/saveMessage")
    fun saveMessage(@Valid @RequestBody message: MessageBean, httpServletResponse: HttpServletResponse) {
        println("/saveMessage : $message")
        list.add(message)

        httpServletResponse.status = HttpStatus.CREATED.value()

    }


    //http://localhost:8080/tchat/allMessages
    @GetMapping("/allMessages")
    fun allMessages(): List<MessageBean> {
        println("/allMessages")

        return list.takeLast(10)
    }


    //filter null  pseudo null
    //filter null  pseudo toto
    //filter coucou  pseudo null
    //filter coucou  pseudo toto
    //http://localhost:8080/tchat/filter (4)
    //http://localhost:8080/tchat/filter?filter=cou (2)
    //http://localhost:8080/tchat/filter?pseudo=toto (2)
    //http://localhost:8080/tchat/filter?filter=cou&pseudo=toto (1)
    @GetMapping("/filter")
    fun filter(filter:String?, pseudo:String?): List<MessageBean> {
        println("/filter")

        return list.filter { filter == null || it.message.contains(filter, true) }
            .filter { pseudo == null || it.pseudo.equals(pseudo, true) }
    }

}

data class MessageBean(
   @field:Email(message = "Il faut que ce soit un email")
   @field:NotBlank(message = "Il faut un pseudo")
   @field:Size(min = 3, message = "Il faut au moins 3 caractères")
   var pseudo:String,
   @field:Size(min = 5, max = 50, message = "Il faut entre 5 et 50 caractères") var message:String)