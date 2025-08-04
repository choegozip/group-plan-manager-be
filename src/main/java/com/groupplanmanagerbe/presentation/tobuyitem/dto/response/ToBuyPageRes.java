package com.groupplanmanagerbe.presentation.tobuyitem.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record ToBuyPageRes(
        List<ToBuyListRes> list,
        boolean hasNext
) {
    public static ToBuyPageRes of(List<ToBuyListRes> list, int size) {
        return ToBuyPageRes.builder()
                .list(list)
                .hasNext(list.size() > size)
                .build();
    }
}
