package org.ping_me.consumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ping_me.dto.event.MusicListeningEvent;
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
public class MusicConsumer {
    UserActivityLogService activityLogService;
    ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topic.listen-music-dev}",
            groupId = "ping-me-audit-group-v2",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeMusic(String message) {
        try {
            MusicListeningEvent event = objectMapper.readValue(message, MusicListeningEvent.class);
            activityLogService.saveMusicLog(event);

            log.info("[KAFKA] Đã lưu log Music (Nghe bài hát) cho bài hát ID: {}", event.getSongId());
        } catch (Exception e) {
            log.error("[KAFKA] Lỗi xử lý message Music: {}", message, e);
        }
    }
}
