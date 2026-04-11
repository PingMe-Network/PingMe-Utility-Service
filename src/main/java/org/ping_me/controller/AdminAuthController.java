package org.ping_me.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.ping_me.dto.response.DailyTrendResponse;
import org.ping_me.model.UserActivityLog;
import org.ping_me.service.AdminAuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author : user664dntp
 * @mailto : phatdang19052004@gmail.com
 * @created : 11/04/2026, Saturday
 **/
@RestController
@RequestMapping("/api/admin/auth-stats")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminAuthController {
    AdminAuthService adminAuthService;

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCount(
            @RequestParam(required = false) Long start,
            @RequestParam(required = false) Long end) {

        long finalStart = (start != null) ? start : 0L;
        long finalEnd = (end != null) ? end : Instant.now().toEpochMilli();

        return ResponseEntity.ok(adminAuthService.getTotalAuthCount(
                Instant.ofEpochMilli(finalStart),
                Instant.ofEpochMilli(finalEnd)
        ));
    }

    @GetMapping("/trend")
    public ResponseEntity<List<DailyTrendResponse>> getTrend(
            @RequestParam(required = false) Long start,
            @RequestParam(required = false) Long end) {

        long finalEnd = (end != null) ? end : Instant.now().toEpochMilli();
        long finalStart = (start != null) ? start : Instant.now().minus(7, ChronoUnit.DAYS).toEpochMilli();

        return ResponseEntity.ok(adminAuthService.getAuthDailyTrend(
                Instant.ofEpochMilli(finalStart),
                Instant.ofEpochMilli(finalEnd)
        ));
    }

    @GetMapping("/recent")
    public ResponseEntity<Page<UserActivityLog>> getRecentLogs(
            @RequestParam(required = false) Long start,
            @RequestParam(required = false) Long end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        long finalStart = (start != null) ? start : 0L;
        long finalEnd = (end != null) ? end : Instant.now().toEpochMilli();

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        return ResponseEntity.ok(adminAuthService.getRecentAuthLogs(
                Instant.ofEpochMilli(finalStart),
                Instant.ofEpochMilli(finalEnd),
                pageRequest
        ));
    }
}
