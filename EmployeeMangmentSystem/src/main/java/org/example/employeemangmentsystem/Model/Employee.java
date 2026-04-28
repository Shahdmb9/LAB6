package org.example.employeemangmentsystem.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Employee {
//    Employees Model has: ID , name, email , phoneNumber ,age, position, onLeave, hireDate and
//    annualleave.
    @NotEmpty(message = "id can not be empty")
    @Size(min=3,message = "id length must be more than 2 letter")
    private String id;
    @NotEmpty(message = "name can not be empty")
    @Size(min = 5,message = "name length must be more than 5 letter")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "On name Only letters are allowed")
    private String name;
    @Email(message = "enter valid email")
    private String email;
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and contain 10 digits")
    private String phoneNumber;
    @NotNull(message = "age can not be null")
    @Min(value = 26,message = "age must be more then 25")
    private int age;
    @NotEmpty(message = "position can not be empty")
    @Pattern(regexp = "(?i)^(supervisor|coordinator)$", message = "position must be supervisor or coordinator")
    private String position;
    private boolean onLeave=false;
    @NotNull(message = "hire date can not be null")
    @PastOrPresent
    private LocalDate hireDate;
    @NotNull(message = "annual Leave can not be null")
    @PositiveOrZero
    private int annualLeave;
}
