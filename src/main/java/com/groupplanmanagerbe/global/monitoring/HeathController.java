package com.groupplanmanagerbe.global.monitoring;

import com.groupplanmanagerbe.global.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/health")
@RequiredArgsConstructor
public class HeathController {

    private final SseService sseService;

    private static final String HEALTHY = "HEALTHY";
    private static final String CRITICAL = "CRITICAL";
    private static final String WARNING = "WARNING";

    // 1. 한 번에 모든 정보 가져오기
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllHealth() {
        log.info("헬스체크 요청 받음");
        Map<String, Object> health = new HashMap<>();

        try {
            // 스레드 정보
            health.put("threads", getThreadInfo());
            // SSE 정보
            health.put("sse", getSseInfo());
            // 메모리 정보
            health.put("memory", getMemoryInfo());
            // 전체 상태 판단
            health.put("overallStatus", determineOverallStatus(health));
            health.put("timestamp", LocalDateTime.now());

            log.info("Health check 완료: {}", health.get("overallStatus"));
            return ResponseEntity.ok(health);
        } catch (Exception e) {
            log.error("Health check 실패", e);
            return ResponseEntity.status(500)
                    .body(Map.of("error", e.getMessage(), "timestamp", LocalDateTime.now()));
        }
    }

    // 스레드만 확인
    @GetMapping("/threads")
    public Map<String, Object> getThreadsOnly() {
        log.info("Thread health 요청");
        return getThreadInfo();
    }

    // SSE만 확인
    @GetMapping("/sse")
    public Map<String, Object> getSseOnly() {
        log.info("SSE heath 요청");
        return getSseInfo();
    }

    // 간단한 OK/NOT_OK 체크
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        try {
            // 기본적인 체크만
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            int threadCount = threadBean.getThreadCount();

            if (threadCount > 500) { // 임계값 초과
                return ResponseEntity.status(503).body("NOT_OK: Too many threads");
            }

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("NOT_OK: " + e.getMessage());
        }
    }

    private Map<String, Object> getThreadInfo() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

        int count = threadBean.getThreadCount();
        String status = count < 100 ? HEALTHY
                : count > 200 ? WARNING
                : CRITICAL;

        return Map.of(
                "count", count,
                "peak", threadBean.getPeakThreadCount(),
                "daemon", threadBean.getDaemonThreadCount(),
                "status", status);
    }

    private Map<String, Object> getSseInfo() {
        int connections = sseService.getTotalConnections();
        int spaces = sseService.getTotalSpaces();
        String status = connections < 50 ? HEALTHY
                : connections < 100 ? WARNING
                : CRITICAL;

        return Map.of(
                "connections", connections,
                "spaces", spaces,
                "status", status);
    }

    private Map<String, Object> getMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        long max = runtime.maxMemory();
        double usagePercent = (used * 100.0) / max;

        return Map.of(
                "usedMB", used / 1024 / 1024,
                "maxMb", max / 1024 / 1024,
                "usagePercent", (Math.round(usagePercent)),
                "status", usagePercent < 80 ? HEALTHY
                        : usagePercent < 90 ? WARNING
                        : CRITICAL
        );
    }

    private String determineOverallStatus(Map<String, Object> health) {
        Map<String, Object> threads = (Map<String, Object>) health.get("threads");
        Map<String, Object> sse = (Map<String, Object>) health.get("sse");
        Map<String, Object> memory = (Map<String, Object>) health.get("memory");

        boolean allHealthy = HEALTHY.equals(threads.get("status")) &&
                HEALTHY.equals(sse.get("status")) &&
                HEALTHY.equals(memory.get("status"));

        if (allHealthy) return HEALTHY;

        boolean hasCritical = CRITICAL.equals(threads.get("status")) ||
                CRITICAL.equals(sse.get("status")) ||
                CRITICAL.equals(memory.get("status"));

        return hasCritical ? CRITICAL : WARNING;
    }
}

