package com.bootcamp.bankapi.util;

import com.bootcamp.bankapi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSynchronizer {

    private EmployeeService employeeService;

    @Autowired
    public DataSynchronizer(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Метод предназначен для приведения в соответствие друг с другом баланса счета и привязанных открытых карт,
     * так как база данных сгенерирована автоматически.
     */
    public void synchronizeData() {
        employeeService.synchronizeBalance();
    }
}
