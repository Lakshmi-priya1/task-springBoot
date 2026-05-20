package com.example.taskManagmentSystem.Controller;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.taskManagmentSystem.Dto.Request.EmployeeRequest;
import com.example.taskManagmentSystem.Dto.Response.EmployeeResponse;
import com.example.taskManagmentSystem.Service.EmployeeService;
import com.example.taskManagmentSystem.Service.ExcelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    public final EmployeeService employeeService;
    private final ExcelService excelService;

    public EmployeeController(EmployeeService employeeService, ExcelService excelService) {
        this.employeeService = employeeService;
        this.excelService = excelService;
    }
    @PostMapping("/add")
    public EmployeeResponse addEmployee(@Valid @RequestBody EmployeeRequest request) {
        return employeeService.createEmployee(request);
    }
    @GetMapping("/all")
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    @GetMapping("/{employeeId}")
    public EmployeeResponse getEmployeeById(@PathVariable Long employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @PutMapping("/update/{employeeId}")
    public EmployeeResponse updateEmployee(@PathVariable Long employeeId,@Valid @RequestBody EmployeeRequest request) {
         return employeeService.updateEmployee(employeeId, request);
    }
    
    @DeleteMapping("/delete/{employeeId}")
    public String deleteEmployee(@PathVariable Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return "Employee deleted successfully";
    }  

    @DeleteMapping("/soft-delete/{employeeId}")
    public String softDeleteEmployee(@PathVariable Long employeeId) {
        employeeService.softDeleteEmployee(employeeId);
        return "Employee soft-deleted successfully";
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<EmployeeResponse>> bulkCreate(@Valid @RequestBody List<@Valid EmployeeRequest> requests) {
        return ResponseEntity.ok(employeeService.bulkCreateEmployees(requests));
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<EmployeeResponse>> importFromExcel(@RequestParam("file") MultipartFile file) {
        List<EmployeeRequest> requests = excelService.parseExcel(file);
        return ResponseEntity.ok(employeeService.bulkCreateEmployees(requests));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        byte[] excelBytes = excelService.exportToExcel(employees);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("employees.xlsx").build().toString())
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }   
    @GetMapping
    public Page<EmployeeResponse> getEmployees(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return employeeService.searchFilterEmployees(keyword, department, page, size, sortBy, direction);
    } 
}