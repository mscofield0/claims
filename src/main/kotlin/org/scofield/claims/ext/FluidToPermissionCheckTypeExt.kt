package org.scofield.claims.ext

import net.minecraft.fluid.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

fun Fluid.toPermissionCheckType(): KClass<*> = this::class