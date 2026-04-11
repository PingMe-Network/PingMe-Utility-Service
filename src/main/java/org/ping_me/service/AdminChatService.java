package org.ping_me.service;

import org.ping_me.dto.response.DailyTrendResponse;
import org.ping_me.model.UserActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 11/04/2026, Saturday
 **/
public interface AdminChatService {
    long getTotalChatCount(Instant start, Instant end);
    Page<UserActivityLog> getRecentChatLogs(Instant start, Instant end, Pageable pageable);
    List<DailyTrendResponse> getChatDailyTrend(Instant start, Instant end);
}
