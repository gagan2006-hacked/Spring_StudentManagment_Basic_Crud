package com.StudentManageApp.StudentManagment.execption;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler{
    @org.springframework.web.bind.annotation.ExceptionHandler(APIException.class)
    public ResponseEntity<Map<String,String>> apiExceptHandle(APIException e){
        HashMap<String,String>map=new HashMap<>();
        map.put("message",e.getMessage());
        map.put("status",e.getStatus().toString());
        map.put("cause",e.getCauses());
        return new ResponseEntity<>(map,e.getStatus());
    }
}
