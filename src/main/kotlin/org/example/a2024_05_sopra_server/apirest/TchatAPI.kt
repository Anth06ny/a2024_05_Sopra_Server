package org.example.a2024_05_sopra_server.apirest

import org.example.a2024_05_sopra_server.StudentBean
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
    fun saveMessage(@RequestBody message: MessageBean) {
        println("/saveMessage : $message")

        list.add(message)
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

data class MessageBean(var pseudo:String, var message:String)