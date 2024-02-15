package com.example.demo.domain.util;

import lombok.Builder;

public class FailResponse<T> extends BaseResponse<T> {
    @Builder
    public FailResponse(T data) {
        super(false, data, "요청이 실패했습니다. ");
    }
}