package com.example

import org.javers.core.JaversBuilder
import org.javers.core.diff.ListCompareAlgorithm
import org.javers.core.diff.changetype.container.ListChange
import org.javers.core.metamodel.annotation.Id
import org.javers.core.metamodel.annotation.TypeName
import spock.lang.Specification

import static java.util.Objects.equals

class CaseIndexChangeProblemForRemovedObjectFromList extends Specification {

    @TypeName("Address")
    class AddressClass {
        String city
        String street

        AddressClass(String city, String street) {
            this.city = city
            this.street = street
        }

        @Override
        String toString() {
            return "AddressClass{" +
                    "city='" + city + '\'' +
                    ", street='" + street + '\'' +
                    '}';
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

    def "should compare list of Values using LEVENSHTEIN_DISTANCE"() {
        given:
        def javers = JaversBuilder.javers()
                .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
                .registerValue(
                        AddressClass,
                        { a, b -> equals(a.city, b.city) && equals(a.street, b.street) },
                        {a -> a.toString() }
                )
                .build()

        def previousAddresses = [new AddressClass("Bangalore", "Vinayaka"), new AddressClass("Bangalore", "Vijayanagar")]
        def previousEmp = new EmployeeClass("Frodo", 12000, 41, previousAddresses)

        def currentAddresses =[new AddressClass("Bangalore", "Vijayanagar")]
        def newEmp = new EmployeeClass("Frodo", 12000, 41, currentAddresses)

        when:
        def diff = javers.compare(previousEmp, newEmp);


        then:
        println diff.prettyPrint()
        ListChange change = diff.getChangesByType(ListChange)[0]
        change.changes[0].index == 0
    }

    def "should compare what value is changed for the property of object in list using LEVENSHTEIN_DISTANCE"() {
        given:
        def javers = JaversBuilder.javers()
                .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
                .registerValue(
                        AddressClass,
                        { a, b -> equals(a.city, b.city) && equals(a.street, b.street) },
                        {a -> a.toString() })
                .build()

        def previousAddresses = [new AddressClass("Bangalore", "Vinayaka"), new AddressClass("Bangalore", "Vijayanagar")]
        def previousEmp = new EmployeeClass("Frodo", 12000, 41, previousAddresses)

        def currentAddresses =[new AddressClass("Bangalore", "Vinayaka"), new AddressClass("Bangalore", "Vijayanagar1")]
        def newEmp = new EmployeeClass("Frodo", 12000, 41, currentAddresses)

        when:
        def diff = javers.compare(previousEmp, newEmp);


        then:
        println diff.prettyPrint()
        ListChange change = diff.getChangesByType(ListChange)[0]
        change.changes[0].index == 1
    }
}
