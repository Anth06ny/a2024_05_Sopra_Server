package org.example.a2024_05_sopra_server.websocket

import org.example.a2024_05_sopra_server.services.MessageBean
import org.example.a2024_05_sopra_server.services.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import org.springframework.web.socket.messaging.SessionSubscribeEvent


@Controller
@RequestMapping("/ws") // Chemin de base pour toutes les méthodes de ce contrôleur
class WebSocketController(private val messagingTemplate: SimpMessagingTemplate, val messageService: MessageService) {

    //private val messageHistory = ArrayList<MessageBean>()

    @MessageMapping("/chat")
    fun receiveMessage(message: MessageBean) {
        println("/ws/chat $message")

        messageService.createMessage(message)


        // Envoyer la liste des messages sur le channel
        messagingTemplate.convertAndSend(CHANEL_NAME, messageService.get10Last())
    }

    @EventListener
    fun handleWebSocketSubscribeListener(event: SessionSubscribeEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        if ((CHANEL_NAME) == headerAccessor.destination) {
            messagingTemplate.convertAndSend(CHANEL_NAME, messageService.get10Last())
        }
    }
}

const val CHANEL_NAME: String = "/ws/myChanel"

@Configuration //Création d'un fichier de configuration pour les WebSocket
@EnableWebSocketMessageBroker
open class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    // Le point de terminaison pour les connexions WebSocket (handshake)
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws/chat-app").withSockJS()
    }

    // Configurer les channels (broker) de messages que les clients viendront écouter
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        //Il peut y avoir plusieurs channels
        registry.enableSimpleBroker(CHANEL_NAME)
        //Définir les préfixes des destinations
        registry.setApplicationDestinationPrefixes("/ws")
    }
}