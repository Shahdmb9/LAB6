package org.example.employeemangmentsystem.Controller;


import jakarta.validation.Valid;
import org.example.employeemangmentsystem.ApiResponse.ApiResponse;
import org.example.employeemangmentsystem.Model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees=new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }

        for (Employee e:employees){
            if(e.getId().equals(employee.getId())){
                return ResponseEntity.status(400).body(new ApiResponse("Employee already exists"));
            }
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully"));

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id,@RequestBody @Valid Employee employee, Errors errors){
        if(errors.hasErrors()){
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());
        }
        for (int i = 0; i < employees.size(); i++) {
            if(employees.get(i).getId().equals(id)){
                employees.set(i, employee);
                return ResponseEntity.status(200).body(new ApiResponse("Employee updated successfully"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String id){
        for (Employee e:employees){
            if(e.getId().equals(id)){
                employees.remove(e);
                return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("Employee not found"));
    }


    @GetMapping("/get-employees-by-position/{position}")
    public ResponseEntity<?> getEmployeeByPosition(@PathVariable String position){
        if (!position.matches("(?i)^(supervisor|coordinator)$")) {
            return ResponseEntity.status(400).body(new ApiResponse("Must be 'supervisor', 'coordinator'"));
        }
        ArrayList<Employee> newemployees=new ArrayList<>();
        for (Employee e:employees){
            if(e.getPosition().equals(position)){
                newemployees.add(e);
            }
        }
        if(newemployees.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No employees in this position found"));
        }
        return ResponseEntity.status(200).body(newemployees);
    }

    @GetMapping("/get-employee-by-age-range/{min}/{max}")
    public ResponseEntity<?> getEmployeeByAgeRange(@PathVariable Integer min, @PathVariable Integer max){
        if(min<=25){
            return ResponseEntity.status(400).body(new ApiResponse("the min age must be more than 25"));
        }
        if(min>max){
            return ResponseEntity.status(400).body(new ApiResponse("the max age must be more than the min age"));
        }
        ArrayList<Employee> newemployees=new ArrayList<>();

        for (Employee e:employees){
            if(e.getAge()>=min && e.getAge()<=max){
                newemployees.add(e);
            }
        }
        if(newemployees.isEmpty()){
            return ResponseEntity.status(400).body(new ApiResponse("No employees with this age range found"));
        }
        return ResponseEntity.status(200).body(newemployees);
    }

    @PutMapping("/apply-for-annual-leave/{id}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable String id){
        for (Employee e:employees){
            if(e.getId().equals(id)){
                if(e.isOnLeave())
                    return ResponseEntity.status(400).body(new ApiResponse("employee is on leave"));
                if(e.getAnnualLeave()<1)
                    return ResponseEntity.status(400).body(new ApiResponse("employee does not have any annual leave"));
                e.setOnLeave(true);
                e.setAnnualLeave(e.getAnnualLeave()-1);
                return ResponseEntity.status(200).body(new ApiResponse("employee leave approved, The remaining leaves day :"+e.getAnnualLeave()));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse("employee not found"));
    }

    @GetMapping("/get-employee-with-no-leaves")
    public ResponseEntity<?> getEmployeeWithNoLeaves(){
        ArrayList<Employee> newemployees=new ArrayList<>();
        for (Employee e:employees){
            if(e.getAnnualLeave()==0)
                newemployees.add(e);
        }
        if(newemployees.size()==0){
            return ResponseEntity.status(400).body(new ApiResponse("no employee with 0 leaves"));
        }
        return ResponseEntity.status(200).body(newemployees);
    }

    @PutMapping("/promote-employee/{reqid}/{emid}")
    public ResponseEntity<?> promoteEmployee(@PathVariable("reqid") String requsterId,@PathVariable("emid") String employeeid){
        Employee req=null;
        Employee em=null;

        for (Employee e:employees){
            if(e.getId().equals(employeeid)){
                em=e;
            }
            if(e.getId().equals(requsterId)){
                req=e;
            }
        }
        if(em==null)
            return ResponseEntity.status(400).body(new ApiResponse("employee not found"));
        if(!req.getPosition().equalsIgnoreCase("supervisor")){
            return ResponseEntity.status(400).body(new ApiResponse("the requester is not a supervisor"));
        }
        if(em.getAge()<30)
            return ResponseEntity.status(400).body(new ApiResponse("employee age should be at least 30"));
        if(em.isOnLeave())
            return ResponseEntity.status(400).body(new ApiResponse("employee is on leave"));
        em.setPosition("supervisor");
        return ResponseEntity.status(200).body("employee with id "+employeeid+" promoted successfully");
    }

}
