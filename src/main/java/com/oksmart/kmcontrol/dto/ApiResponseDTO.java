package com.oksmart.kmcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private String status;
    private String message;
    private T data;
    private Object errors;
}
