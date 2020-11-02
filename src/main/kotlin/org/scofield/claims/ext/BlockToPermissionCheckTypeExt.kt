package org.scofield.claims.ext

import net.minecraft.block.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

fun Block.toPermissionCheckType(): KClass<*> = when {
    this::class.isSubclassOf(AbstractButtonBlock::class) -> AbstractButtonBlock::class
    this::class.isSubclassOf(AbstractPressurePlateBlock::class) -> AbstractPressurePlateBlock::class
    else -> this::class
}