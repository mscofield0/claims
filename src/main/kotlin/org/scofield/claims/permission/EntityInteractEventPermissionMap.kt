package org.scofield.claims.permission

import net.minecraft.block.*
import net.minecraft.entity.projectile.thrown.EnderPearlEntity
import kotlin.reflect.KClass

class EntityInteractEventPermissionMap {
    companion object {
        val ENTITY_PERMISSION_MAP: HashMap<KClass<*>, ClaimPermission> = hashMapOf(
            Pair(AbstractPressurePlateBlock::class, ClaimPermission.ABSTRACT_PRESSURE_PLATE_BLOCK),
        )
        val PROJECTILE_PERMISSION_MAP: HashMap<KClass<*>, ClaimPermission> = hashMapOf(
            Pair(EnderPearlEntity::class, ClaimPermission.THROW_ENDER_PEARL),
        )
    }
}