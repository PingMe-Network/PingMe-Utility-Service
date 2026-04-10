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
    private String id;

    private Long userId;

    private ActivityType type;
    private String actionDetail;

    private String messageContent;
    private Long songId;
    private String email;

    private Instant timestamp;
}
