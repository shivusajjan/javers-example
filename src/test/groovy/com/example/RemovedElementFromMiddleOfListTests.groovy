package com.example

import example.AddressClass
import example.EmployeeClass
import org.javers.core.Changes
import org.javers.core.Javers
import org.javers.core.JaversBuilder
import org.javers.repository.jql.QueryBuilder
import org.junit.jupiter.api.Test

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE

//TODO:: In 3rd scenario - I removed device1 from position 0 of device list but it shows position 1. Output mentioned below in Java doc
// I just delete device, result shows value changed. So output is confusing to me
/**
 * Output:
 * * object removed: Node/n1#devices/1
 * * changes on Node/n1 :
 *   - 'devices' collection changes :
 *     1. 'Node/n1#devices/1' removed
 *   - 'devices/0.deviceCode' value changed from 'd1' to 'd2'
 *   - 'devices/0.name' value changed from 'device1' to 'device2'
 */
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