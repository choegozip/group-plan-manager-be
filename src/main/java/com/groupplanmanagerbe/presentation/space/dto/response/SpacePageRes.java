package com.groupplanmanagerbe.presentation.space.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SpacePageRes(
        List<SpacesRes> list,
        boolean hasNext
) {
    public static SpacePageRes of(List<SpacesRes> list, int size) {
        return SpacePageRes.builder()
                .list(list)
                .hasNext(list.size() > size)
                .build();
    }
}
