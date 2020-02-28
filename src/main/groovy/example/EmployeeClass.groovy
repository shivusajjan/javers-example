package example

import com.example.Address
import org.javers.core.metamodel.annotation.Id

class EmployeeClass {
    @Id
    String name;

    int salary;

    int age;

    List<Address> addressList;

    EmployeeClass(String name, int salary, int age, List<Address> addressList) {
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.addressList = addressList;
    }
}
