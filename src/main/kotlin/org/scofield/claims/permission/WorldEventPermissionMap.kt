package org.scofield.claims.permission

import net.minecraft.entity.projectile.thrown.EnderPearlEntity
import net.minecraft.fluid.*
import kotlin.reflect.KClass

class WorldEventPermissionMap {
    companion object {
        val FLUID_PERMISSION_MAP: HashMap<KClass<*>, ClaimPermission> = hashMapOf(
            Pair(WaterFluid::class, ClaimPermission.WATER_FLOW),
            Pair(LavaFluid::class, ClaimPermission.LAVA_FLOW),
        )
    }
}