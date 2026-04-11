package org.ping_me.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ping_me.dto.response.DailyTrendResponse;
import org.ping_me.model.UserActivityLog;
import org.ping_me.model.enums.ActivityType;
import org.ping_me.repository.UserActivityLogRepository;
import org.ping_me.service.AdminAuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 11/04/2026, Saturday
 **/
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AdminAuthServiceImpl implements AdminAuthService {
    UserActivityLogRepository repository;
    MongoTemplate mongoTemplate;
    public long getTotalAuthCount(Instant start, Instant end) {
        return repository.countByTypeAndTimestampBetween(ActivityType.AUTH, start, end);
    }
    public Page<UserActivityLog> getRecentAuthLogs(Instant start, Instant end, Pageable pageable) {
        return repository.findByTypeAndTimestampBetween(ActivityType.AUTH, start, end, pageable);
    }
    public List<DailyTrendResponse> getAuthDailyTrend(Instant start, Instant end) {
        Criteria criteria = Criteria.where("type").is(ActivityType.AUTH)
                .andOperator(
                        Criteria.where("timestamp").gte(start),
                        Criteria.where("timestamp").lte(end)
                );

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project()
                        .andExpression("{$dateToString: {format: '%Y-%m-%d', date: '$timestamp', timezone: 'Asia/Ho_Chi_Minh'}}").as("date"),
                Aggregation.group("date").count().as("count"),
                Aggregation.project("count").and("date").previousOperation(),
                Aggregation.sort(Sort.Direction.ASC, "date")
        );

        AggregationResults<DailyTrendResponse> results = mongoTemplate.aggregate(
                aggregation, UserActivityLog.class, DailyTrendResponse.class
        );

        return results.getMappedResults();
    }
}
