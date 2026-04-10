package org.ping_me.consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ping_me.dto.event.UserChatEvent;
import org.ping_me.service.UserActivityLogService;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 5/04/2026, Sunday
 **/

@Component
@Slf4j
@Lazy(false)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatConsumer {
    UserActivityLogService activityLogService;
    ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.user-chat-dev}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeChat(String message) {
        try {
            UserChatEvent event = objectMapper.readValue(message, UserChatEvent.class);
            activityLogService.saveChatLog(event);
            log.info("Saved Chat log for user: {}", event.getSenderId());
        } catch (Exception e) {
            log.error("Lỗi parse JSON: ", e);
        }
    }
}
