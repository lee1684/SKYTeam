package kr.co.ssalon.chat;

import io.jsonwebtoken.ExpiredJwtException;
import kr.co.ssalon.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 메시지가 아닌 경우에는 인증 처리 X
        if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String accessToken = null;

        // STOMP 헤더에서 Authorization 헤더 추출
        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Bearer 토큰에서 실제 토큰 값만 추출
            accessToken = authorizationHeader.substring(7);
        }

        if (accessToken == null) {
            log.error("No JWT token found in request headers");
            return null;
        }

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
            return null;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!"access".equals(category)) {
            log.error("Invalid JWT token category");
            return null;
        }

        // 웹소켓 메시지에 username 저장
        String token = accessToken;
        String username = jwtUtil.getUsername(token);
        accessor.getSessionAttributes().put("username", username);

        return message;
    }
}
