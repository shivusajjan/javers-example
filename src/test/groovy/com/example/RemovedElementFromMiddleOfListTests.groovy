package com.example

import example.AddressClass
import example.EmployeeClass
import org.javers.core.Changes
import org.javers.core.Javers
import org.javers.core.JaversBuilder
import org.javers.repository.jql.QueryBuilder
import org.junit.jupiter.api.Test

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE


class RemovedElementFromMiddleOfListTests {

    @Test
    void removeAddressFromMiddleOfList() {
        Javers javers = JaversBuilder.javers()
                .withListCompareAlgorithm(LEVENSHTEIN_DISTANCE)
                .build();

        List<AddressClass> addresses = new ArrayList<>();
        addresses.add( new AddressClass("Bangalore", "Vinayaka"));
        addresses.add( new AddressClass("Bangalore", "Vijayanagar"));
        EmployeeClass oldEmp = new EmployeeClass("Frodo", 12000, 41, addresses);
        javers.commit("me@here.com", oldEmp);

        //Removed new Address("Bangalore", "Vinayaka") from the position 0 of list but result is saying removed from position 1
        //even though i used LEVENSHTEIN_DISTANCE algorithm. Output is mentioned below
        /**
         * Actual Output:
         *---------------
         *  * changes on Employee/Frodo :
         *     - 'addressList' collection changes :
         *       1. 'Employee/Frodo#addressList/1' removed
         */

        //I am expecting output in below
        /**
         * Expected output:
         *----------------
         *  * changes on Employee/Frodo :
         *     - 'addressList' collection changes :
         *       0. 'Employee/Frodo#addressList/0' removed
         */

        List<AddressClass> newAddresses = new ArrayList<>();
        newAddresses.add( new AddressClass("Bangalore", "Vijayanagar"));
        EmployeeClass newEmp = new EmployeeClass("Frodo", 12000, 41, newAddresses);
        javers.commit("me@here.com", newEmp);

        Changes changes = javers.findChanges(QueryBuilder.byInstanceId("Frodo", EmployeeClass.class).build());
        System.out.println("Changes "+ changes.prettyPrint());

    }
}