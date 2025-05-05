package com.demo.chatApp.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private int statusCode;

    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.statusCode = 200;
    }

    public ApiResponse(String message, int statusCode) {
        this.success = false;
        this.message = message;
        this.statusCode = statusCode;
    }

}
