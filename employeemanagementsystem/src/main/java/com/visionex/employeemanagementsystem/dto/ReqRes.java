package com.visionex.employeemanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.visionex.employeemanagementsystem.entity.Employees;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String department;
    private String designation;
    private String jobTitle;
    private String address;
    private String password;
    private String salary;
    private String status;
    private Employees employees;
    private List<Employees> employeesList;



}
