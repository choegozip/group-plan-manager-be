package com.groupplanmanagerbe.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListTokenService {
    
    
    public boolean isBlacklisted(String token) {
        return true;
    }

    public void save(String token, long mills) {
    }
}
