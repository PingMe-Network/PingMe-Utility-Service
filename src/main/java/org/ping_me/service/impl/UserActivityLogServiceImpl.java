package org.ping_me.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ping_me.dto.event.MusicListeningEvent;
import org.ping_me.dto.event.UserAuditEvent;
import org.ping_me.dto.event.UserChatEvent;
import org.ping_me.model.UserActivityLog;
import org.ping_me.model.enums.ActivityType;
import org.ping_me.repository.UserActivityLogRepository;
import org.ping_me.service.UserActivityLogService;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 9/04/2026, Thursday
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserActivityLogServiceImpl implements UserActivityLogService {
    UserActivityLogRepository repository;
    public void saveAuthLog(UserAuditEvent event) {
        var log = UserActivityLog.builder()
                .userId(event.userId())
                .email(event.email())
                .type(ActivityType.AUTH)
                .actionDetail(event.action().name())
                .timestamp(Instant.ofEpochMilli(event.timestamp()))
                .build();
        repository.save(log);
    }
    public void saveChatLog(UserChatEvent event) {
        var log = UserActivityLog.builder()
                .userId(event.getSenderId())
                .type(ActivityType.CHAT)
                .messageContent(event.getMessage())
                .timestamp(Instant.ofEpochMilli(event.getTimestamp()))
                .build();
        repository.save(log);
    }
    public void saveMusicLog(MusicListeningEvent event) {
        var log = UserActivityLog.builder()
                .songId(event.getSongId())
                .type(ActivityType.MUSIC)
                .timestamp(Instant.ofEpochMilli(event.getTimestamp()))
                .build();
        repository.save(log);
    }
}
