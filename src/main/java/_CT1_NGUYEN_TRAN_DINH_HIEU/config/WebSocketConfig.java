package _CT1_NGUYEN_TRAN_DINH_HIEU.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Mở một đường hầm tên là "/ws" để Client (Trình duyệt) kết nối vào
        // withSockJS() giúp tương thích với các trình duyệt cũ không hỗ trợ WebSocket thuần
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Cấu hình "Loa phát thanh" (Broker) có tiền tố là "/topic"
        // Bất kỳ ai đăng ký nghe kênh "/topic/..." sẽ nhận được tin nhắn
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }
}