package com.example

import org.javers.core.metamodel.annotation.Id
import org.javers.core.metamodel.annotation.TypeName

@TypeName("Node")
data class Node(@Id val nodeCode: String, val name: String, val devices: List<Device>)