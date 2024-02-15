package com.example.demo.domain.util;

import lombok.Builder;

public class SuccessResponse<T> extends BaseResponse<T> {
    @Builder
    private SuccessResponse(T data, String message) {
        super(true, data, message);
    }
}