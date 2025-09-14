package com.groupplanmanagerbe.global.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping("/{spaceId}/subscriptions")
    public SseEmitter subscribe(
            @PathVariable Long spaceId,
            @RequestParam String type
    ) {
        return sseService.subscribe(spaceId, type);
    }
}
