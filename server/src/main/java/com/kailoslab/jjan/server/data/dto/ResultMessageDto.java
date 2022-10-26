package com.kailoslab.jjan.server.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResultMessageDto {
    public static String MESSAGE_OK = "데이터 처리에 성공하였습니다.";
    private boolean result = true;
    private String message = MESSAGE_OK;
    private Object data;

    public ResultMessageDto(Object data) {
        this.data = data;
    }

    public ResultMessageDto(boolean result, String message) {
        this(result, message, null);
    }
}