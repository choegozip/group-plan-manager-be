package com.groupplanmanagerbe.global.sse;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@Getter
public class SseService {

    private final Map<Long, Map<String, List<SseEmitter>>> emitterMap = new ConcurrentHashMap<>();

    public final String TYPE_OF_TO_BUY = "toBuy";
    public final String TYPE_OF_TO_DO = "toDo";

    public SseEmitter subscribe(Long spaceId, String type) {
        SseEmitter emitter = new SseEmitter(0L); // 30분

        emitterMap
                .computeIfAbsent(spaceId, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(type, k -> new CopyOnWriteArrayList<>())
                .add(emitter);

        int currentSubscribers = getConnectionsBySpace(spaceId);
        log.info("SSE 구독 시작: spaceId={}, type={},currentSubscribers={}", spaceId, type, currentSubscribers);

        emitter.onCompletion(() -> {
            removeEmitter(spaceId, type, emitter);
            log.info("SSE 구독 종료(completion): spaceId={}, currentSubscribers={}",
                    spaceId, currentSubscribers);
        });

        emitter.onTimeout(() -> {
            removeEmitter(spaceId, type, emitter);
            log.info("SSE 구독 종료(timeout): spaceId={}, currentSubscribers={}",
                    spaceId, currentSubscribers);
        });

        emitter.onError((throwable) -> {
            removeEmitter(spaceId, type, emitter);
            log.error("SSE 구독 종료(error): spaceId={}, currentSubscribers={}, error={}",
                    spaceId, currentSubscribers, throwable.getMessage());
        });

        return emitter;
    }

    public void sendEvent(Long spaceId, String type, Object data) {
        List<SseEmitter> emitters =
                emitterMap.getOrDefault(spaceId, Map.of())
                        .getOrDefault(type, List.of());

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(type + "_UPDATED")
                        .data(data));
                log.info("이벤트 생성 완료, spaceId: {}, type: {}", spaceId, type);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }
    }

    private void removeEmitter(Long spaceId, String type, SseEmitter emitter) {
        Map<String, List<SseEmitter>> typeMap = emitterMap.get(spaceId);
        if (typeMap != null) {
            List<SseEmitter> emitters = typeMap.get(type);
            if (emitters != null) {
                emitters.remove(emitter);
                if (emitters.isEmpty()) {
                    typeMap.remove(type);
                }
            }
            if (typeMap.isEmpty()) {
                emitterMap.remove(spaceId);
            }
        }
    }

    public int getTotalConnections() {
        return emitterMap.values().stream()
                .flatMap(typeMap -> typeMap.values().stream())
                .mapToInt(List::size)
                .sum();
    }

    public int getConnectionsBySpace(Long spaceId) {
        return emitterMap.getOrDefault(spaceId, Map.of())
                .values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    public int getTotalSpaces() {
        return emitterMap.size();
    }
}
