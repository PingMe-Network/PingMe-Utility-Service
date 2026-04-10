package org.ping_me.service;

import org.ping_me.dto.event.MusicListeningEvent;
import org.ping_me.dto.event.UserAuditEvent;
import org.ping_me.dto.event.UserChatEvent;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 9/04/2026, Thursday
 **/

public interface UserActivityLogService {
    void saveAuthLog(UserAuditEvent event);
    void saveChatLog(UserChatEvent event);
    void saveMusicLog(MusicListeningEvent event);
}
