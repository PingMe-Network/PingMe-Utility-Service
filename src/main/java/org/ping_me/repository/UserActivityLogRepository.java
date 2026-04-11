package org.ping_me.repository;

import org.ping_me.model.UserActivityLog;
import org.ping_me.model.enums.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 19/03/2026, Thursday
 **/
@Repository
public interface UserActivityLogRepository extends MongoRepository<UserActivityLog, String> {
    Page<UserActivityLog> findByUserId(Long userId, Pageable pageable);
    long countByTypeAndTimestampBetween(ActivityType type, Instant start, Instant end);
    Page<UserActivityLog> findByTypeAndTimestampBetween(ActivityType type, Instant start, Instant end, Pageable pageable);
}
