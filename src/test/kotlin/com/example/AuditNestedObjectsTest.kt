package com.example

import org.javers.core.JaversBuilder
import org.javers.core.diff.ListCompareAlgorithm
import org.junit.jupiter.api.Test

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
internal class AuditNestedObjectsTest {

    @Test
    fun auditNestedObjects() {

        val javers = JaversBuilder.javers()
                .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
                .build()

        var device1 = Device("d1", "device1") // devices/d1 -
        var device2 = Device("d2", "device2")  // devices/2
        var devices = mutableListOf<Device>()
        devices.add(device1)
        devices.add(device2)

        val before = Node("n1", "node1", devices)

        //1. Scenario - Remove device2

        val newDevice1 = Device("d1", "device1")
        val newDevices = mutableListOf<Device>()
        newDevices.add(newDevice1)
        var after = Node("n1", "node1", newDevices)

        var diff = javers.compare(before, after)
        println("removed $diff")


        //2. Scenario - Add device2
        device1 = Device("d1", "device1") // devices/d1 -
        device2 = Device("d2", "device2")  // devices/2
        devices = mutableListOf<Device>()
        devices.add(device1)
        devices.add(device2)

        val after1 = Node("n1", "node1", devices)

        diff = javers.compare(after, after1)
        println("added $diff")


        //3. Scenario - Remove device1 from the device list
        device2 = Device("d2", "device2")  // devices/2
        var device3 = Device("d3", "device3")  // devices/2
        var newDevices2 = mutableListOf<Device>()
        newDevices2.add(device2)
        newDevices2.add(device3)

        val after2 = Node("n1", "node1", newDevices2)

        diff = javers.compare(after1, after2)
        println("removed device1 from the device list ${diff.prettyPrint()}")


        //4. Scenario - Changed device2 to mydevice2
        device2 = Device("d2", "mydevice2")  // devices/2
        var newDevices3 = mutableListOf<Device>()
        newDevices3.add(device2)

        val after3 = Node("n1", "node1", newDevices3)

        diff = javers.compare(after2, after3)
        println("Changed device2 to mydevice2 ${diff.prettyPrint()}")

    }
}