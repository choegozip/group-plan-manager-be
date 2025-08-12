package com.groupplanmanagerbe.presentation.space.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/invite")
@RequiredArgsConstructor
public class SpaceViewController {

    @GetMapping("/{inviteCode}")
    public String invite(
            @PathVariable String inviteCode
    ) {
        return "invite";
    }
}
