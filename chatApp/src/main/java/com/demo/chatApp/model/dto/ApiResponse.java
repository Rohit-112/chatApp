package com.demo.chatApp.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    private boolean status;
    private T data;
    private String message;
    private int statusCode;

    public ApiResponse(T data, String message) {
        this.status = true;
        this.data = data;
        this.message = message;
        this.statusCode = 200;
    }

    public ApiResponse(String message, int statusCode) {
        this.status = false;
        this.message = message;
        this.statusCode = statusCode;
        this.data = null;
    }

}
