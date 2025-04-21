package com.visionex.employeemanagementsystem.service;

import com.visionex.employeemanagementsystem.dto.ReqRes;
import com.visionex.employeemanagementsystem.entity.Employees;
import com.visionex.employeemanagementsystem.repository.EmpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeManagementService {

    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try{
            Employees employees = new Employees();
            employees.setEmail(registrationRequest.getEmail());
            employees.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            employees.setName(registrationRequest.getName());
            employees.setAddress(registrationRequest.getAddress());
            employees.setDepartment(registrationRequest.getDepartment());
            employees.setRole(registrationRequest.getRole());
            employees.setPhone(registrationRequest.getPhone());
            employees.setDesignation(registrationRequest.getDesignation());
            employees.setJobTitle(registrationRequest.getJobTitle());
            employees.setSalary(registrationRequest.getSalary());
            employees.setStatus(registrationRequest.getStatus());
            Employees employeesResult = empRepo.save(employees);
            if (employeesResult.getId()>0) {
                resp.setEmployees(employeesResult);
                resp.setMessage("Employee Registration Successful");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());
        }
        return resp;
    }
    public ReqRes login(ReqRes loginRequest) {
        ReqRes resp = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var employees = empRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(employees);
            var refreshToken= jwtUtils.generateRefreshToken(new HashMap<>(), employees);
            resp.setStatusCode(200);
            resp.setToken(jwt);
            resp.setRefreshToken(refreshToken);
            resp.setRole(employees.getRole());
            resp.setExpirationTime("24Hrs");
            resp.setMessage("Employee Login Successful");


        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setMessage(e.getMessage());

        }
        return resp;

    }
    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes response = new ReqRes();
        try{
            String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            Employees employees = empRepo.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), employees)) {
                var jwt = jwtUtils.generateToken(employees);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }
    public ReqRes getAllEmployees() {
        ReqRes reqRes = new ReqRes();

        try {
            List<Employees> result = empRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setEmployeesList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No employee found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }
    public ReqRes getEmployeesById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            Employees employeeById = empRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setEmployees(employeeById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Employees with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }
    public ReqRes deleteEmployee(Integer employeeId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Employees> userOptional = empRepo.findById(employeeId);
            if (userOptional.isPresent()) {
                empRepo.deleteById(employeeId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }//have to make changes
    public ReqRes updateEmployee(Integer userId, Employees updatedEmployee) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Employees> userOptional = empRepo.findById(userId);
            if (userOptional.isPresent()) {
                Employees existingUser = userOptional.get();
                existingUser.setEmail(updatedEmployee.getEmail());
                existingUser.setName(updatedEmployee.getName());
                existingUser.setJobTitle(updatedEmployee.getJobTitle());
                existingUser.setRole(updatedEmployee.getRole());

                // Check if password is present in the request
                if (updatedEmployee.getPassword() != null && !updatedEmployee.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedEmployee.getPassword()));
                }

                Employees savedUser = empRepo.save(existingUser);
                reqRes.setEmployees(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Employees> userOptional = empRepo.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setEmployees(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting employee info: " + e.getMessage());
        }
        return reqRes;

    }

}
