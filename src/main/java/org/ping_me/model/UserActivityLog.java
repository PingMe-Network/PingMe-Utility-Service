package org.ping_me.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.ping_me.model.enums.ActivityType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 9/04/2026, Thursday
 **/
@Document(collection = "user_activity_logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserActivityLog {
    @Id
    String id;

    Long userId;

    ActivityType type;
    String actionDetail;

    String messageContent;
    Long songId;
    String email;

    Instant timestamp;

    String songTitle;
    String artistName;

    String userName;
}
