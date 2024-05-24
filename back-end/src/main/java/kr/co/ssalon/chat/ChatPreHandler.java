package kr.co.ssalon.chat;

import kr.co.ssalon.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPreHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final Map<String, String> memberToChatRoomIdMap = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String accessToken = null;
        String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            accessToken = authorizationHeader.substring(7);
        }

        if (accessToken != null) {
            String username = jwtUtil.getUsername(accessToken);
            accessor.getSessionAttributes().put("username", username);

            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String moimId = accessor.getFirstNativeHeader("moimId");
                memberToChatRoomIdMap.put(username, moimId);
            } else if (Objects.equals(accessor.getDestination(), "/send/disconnect")) {
                memberToChatRoomIdMap.remove(username);
            }
        }

        return message;
    }

    public Map<String, String> getConnectedMembers() {
        return memberToChatRoomIdMap;
    }
}
