package org.scofield.claims.ext

import net.minecraft.entity.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

fun Entity.toPermissionCheckType(): KClass<*> = this::class