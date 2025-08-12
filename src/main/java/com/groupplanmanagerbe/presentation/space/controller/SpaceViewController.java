package com.groupplanmanagerbe.presentation.space.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/spaces")
@RequiredArgsConstructor
public class SpaceViewController {

    @GetMapping("/spacemember")
    public String invite() {
        return "invite";
    }
}
