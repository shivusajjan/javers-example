package com.example

import org.javers.core.metamodel.annotation.TypeName
import java.util.*

@TypeName("Device")
data class Device(val deviceCode: String, val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Device) return false

        if (deviceCode != other.deviceCode) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
       return Objects.hash(deviceCode, name)
    }
}