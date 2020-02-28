package com.example;

import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.repository.jql.QueryBuilder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.javers.core.diff.ListCompareAlgorithm.LEVENSHTEIN_DISTANCE;

public class RemoveElementFromMiddleOfListTest {
    @Test
    public void removeAddressFromMiddleOfList() {
        Javers javers = JaversBuilder.javers()
                .withListCompareAlgorithm(LEVENSHTEIN_DISTANCE)
                .build();

        List<Address> addresses = new ArrayList<>();
        addresses.add( new Address("Bangalore", "Vinayaka"));
        addresses.add( new Address("Bangalore", "Vijayanagar"));
        Employee oldEmp = new Employee("Frodo", 12000, 41, addresses);
        javers.commit("me@here.com", oldEmp);


        List<Address> newAddresses = new ArrayList<>();
        newAddresses.add( new Address("Bangalore", "Vijayanagar"));
        Employee newEmp = new Employee("Frodo", 12000, 41, newAddresses);
        javers.commit("me@here.com", newEmp);

        Changes changes = javers.findChanges(QueryBuilder.byInstanceId("Frodo", Employee.class).build());
        System.out.println("Changes "+ changes.prettyPrint());

    }
}
