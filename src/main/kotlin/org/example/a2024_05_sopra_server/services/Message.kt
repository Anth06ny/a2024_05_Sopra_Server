package org.example.a2024_05_sopra_server.services

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.antlr.v4.runtime.tree.xpath.XPath.findAll
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
class MessageService(val messageRepository: MessageRepository){

    fun createMessage(messageBean: MessageBean)  {
        messageRepository.save(messageBean)
    }

    fun get10Last() = messageRepository.findFirst10ByOrderByIdDesc().reversed()

    fun filterByPseudoAndMessage(pseudo: String?, message: String?) = messageRepository.findByPseudoAndMessageContaining(pseudo, message)
}

interface MessageRepository : JpaRepository<MessageBean, Long> {
       fun findFirst10ByOrderByIdDesc() : List<MessageBean>
        fun findByPseudoAndMessageContaining(pseudo: String?, message: String?): List<MessageBean>

}

//findFirstByOrderByPublicationDateDesc

@Entity
@Table(name = "message")
data class MessageBean(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    //@field:Email(message = "Il faut que ce soit un email")
    @field:NotBlank(message = "Il faut un pseudo")
    @field:Size(min = 3, max = 50, message = "Il faut au moins 3 caractères")
    var pseudo: String = "",
    @field:Size(min = 5, max = 200, message = "Il faut entre 5 et 50 caractères") var message: String?=""
)