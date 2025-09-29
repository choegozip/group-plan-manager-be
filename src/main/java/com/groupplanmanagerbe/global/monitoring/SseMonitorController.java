package com.groupplanmanagerbe.global.monitoring;

import com.groupplanmanagerbe.global.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/sse/connections")
@RequiredArgsConstructor
public class SseMonitorController {

    private final SseService sseService;


    @GetMapping("/spaces/{spaceId}")
    public ResponseEntity<?> listConnections(
            @PathVariable Long spaceId,
            @RequestParam String type
    ) {
        List<SseEmitter> emitters = sseService.getEmitterMap()
                .get(spaceId)
                .get(type);

        int connectionCount = emitters.size();

        return ResponseEntity.ok(Map.of(
                "spaceId", spaceId,
                "type", type,
                "connections", connectionCount
        ));
    }

    @DeleteMapping
    public ResponseEntity<?> closeAllConnections() {
        sseService.getEmitterMap().values().forEach(
                typeMap -> typeMap.values().forEach(list ->
                        list.forEach(SseEmitter::complete)));
        sseService.getEmitterMap().clear();

        return ResponseEntity.ok("모든 SSE 커넥션 종료");
    }

    @DeleteMapping("/spaces/{spaceId}")
    public ResponseEntity<?> closeConnectionBySpace(
            @PathVariable Long spaceId,
            @RequestParam String type
    ) {
        Map<String, List<SseEmitter>> typeMap = sseService.getEmitterMap().get(spaceId);

        if (type != null) {
            // 특정 type 종료
            List<SseEmitter> emitters = typeMap.get(type);
            emitters.forEach(SseEmitter::complete);
            typeMap.remove(type);
        } else {
            // spaceId 전체 종료
            typeMap.values().forEach(list -> list.forEach(SseEmitter::complete));
            typeMap.clear();
        }

        if (typeMap.isEmpty()) {
            sseService.getEmitterMap().remove(spaceId);
        }

        return ResponseEntity.ok("Closed SSE connections for spaceId=" + spaceId +
                (type != null ? ", type=" + type : ""));
    }
}
