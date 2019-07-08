package io.project.services;

import io.project.model.Employee;
import io.project.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public Employee getEmployeeById(Long userId) {
        return employeeRepository.getEmployeeByUserId(userId);
    }
}
