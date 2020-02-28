package com.example

import org.javers.core.JaversBuilder
import org.javers.core.diff.ListCompareAlgorithm
import org.javers.core.diff.changetype.container.ListChange
import org.javers.core.metamodel.annotation.Id
import org.javers.core.metamodel.annotation.TypeName
import org.javers.repository.jql.QueryBuilder
import spock.lang.Specification

class CaseIndexChangeProblemForRemovedObjectFromList extends Specification {

    @TypeName("Address")
    class AddressClass {
        String city

        String street

        AddressClass(String city, String street) {
            this.city = city
            this.street = street
        }
    }

    @TypeName("Employee")
    class EmployeeClass {
        @Id
        String name

        int salary

        int age

        List<AddressClass> addressList

        EmployeeClass(String name, int salary, int age, List<AddressClass> addressList) {
            this.name = name
            this.salary = salary
            this.age = age
            this.addressList = addressList
        }
    }

    def "should not change index of removed element from the middle of list"() {
        given:
        def javers = JaversBuilder.javers().withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE).build()

        def previousAddresses = new ArrayList<>();
        previousAddresses.add( new AddressClass("Bangalore", "Vinayaka"));
        previousAddresses.add( new AddressClass("Bangalore", "Vijayanagar"));
        def previousEmp = new EmployeeClass("Frodo", 12000, 41, previousAddresses);

        when:
        javers.commit("me@here.com", previousEmp);
        def currentAddresses = new ArrayList<>();
        currentAddresses.add( new AddressClass("Bangalore", "Vijayanagar"));
        def newEmp = new EmployeeClass("Frodo", 12000, 41, currentAddresses);
        javers.commit("me@here.com", newEmp);


        then:
        def changes = javers.findChanges(QueryBuilder.byInstanceId("Frodo", EmployeeClass).build())
        ListChange change = changes[0]
        change.changes[0].index == 1 //TODO:: Index should be 0, because removed address from postion 0
    }
}
