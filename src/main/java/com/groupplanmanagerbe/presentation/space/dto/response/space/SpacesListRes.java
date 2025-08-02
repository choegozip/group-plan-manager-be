package com.groupplanmanagerbe.presentation.space.dto.response.space;

import com.groupplanmanagerbe.domain.space.entity.Space;
import lombok.Builder;

import java.util.List;

@Builder
public record SpacesListRes(
        List<SpacesRes> list
) {
    public static SpacesListRes of(List<Space> spaces, Long userId) {
        List<SpacesRes> spacesResList = spaces.stream()
                .map(space -> SpacesRes.of(space, isOwnerOf(space, userId)))
                .toList();
        return SpacesListRes.builder()
                .list(spacesResList)
                .build();
    }

    private static boolean isOwnerOf(Space space, Long userId) {
        return space.getMembers().stream()
                .anyMatch(m -> m.isOwner() && m.getUser().getId().equals(userId));
    }
}