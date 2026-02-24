package com.StudentManageApp.StudentManagment.execption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class APIException extends RuntimeException {
    private String message;
    private HttpStatus status;
    private String causes;
    public APIException(String message) {
        super(message);
        this.message=message;
    }
}
