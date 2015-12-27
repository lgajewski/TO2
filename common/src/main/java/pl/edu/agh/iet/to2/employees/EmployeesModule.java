package pl.edu.agh.iet.to2.employees;

import java.util.List;

public interface EmployeesModule {

    IEmployee getEmployeeId(long id);

    List<IEmployee> getEmployees();

    List<IEmployee> getFilteredEmployees(Predicate predicate);


    interface Predicate {
        boolean filter(IEmployee employee);
    }

}