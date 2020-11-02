package org.scofield.claims.permission

import net.minecraft.block.*
import kotlin.reflect.KClass

class BlockInteractEventPermissionMap {
    companion object {
        val ENTITY_PERMISSION_MAP: HashMap<KClass<*>, ClaimPermission> = hashMapOf(
            Pair(AbstractPressurePlateBlock::class, ClaimPermission.ABSTRACT_PRESSURE_PLATE_BLOCK),
            Pair(DetectorRailBlock::class, ClaimPermission.DETECTOR_RAIL_BLOCK),
            Pair(EndPortalBlock::class, ClaimPermission.END_PORTAL_BLOCK),
            Pair(NetherPortalBlock::class, ClaimPermission.NETHER_PORTAL_BLOCK),
            Pair(LilyPadBlock::class, ClaimPermission.LILY_PAD_BLOCK),
            Pair(TripwireBlock::class, ClaimPermission.TRIPWIRE_BLOCK),
            Pair(WitherRoseBlock::class, ClaimPermission.WITHER_ROSE_BLOCK),
        )
        val PROJECTILE_PERMISSION_MAP: HashMap<KClass<*>, ClaimPermission> = hashMapOf(
            Pair(AbstractButtonBlock::class, ClaimPermission.ABSTRACT_BUTTON_BLOCK),
            Pair(AbstractPressurePlateBlock::class, ClaimPermission.ABSTRACT_PRESSURE_PLATE_BLOCK),
            Pair(DetectorRailBlock::class, ClaimPermission.DETECTOR_RAIL_BLOCK),
            Pair(EndPortalBlock::class, ClaimPermission.END_PORTAL_BLOCK),
            Pair(NetherPortalBlock::class, ClaimPermission.NETHER_PORTAL_BLOCK),
            Pair(TripwireBlock::class, ClaimPermission.TRIPWIRE_BLOCK),
        )
    }
}

/*
val BLOCK_PERMISSION_MAP: HashMap<KClass<*>, ClaimPermission> = hashMapOf(
    // Block interact events
    Pair(AbstractButtonBlock::class, ClaimPermission.ABSTRACT_BUTTON_BLOCK),
    Pair(AbstractPressurePlateBlock::class, ClaimPermission.ABSTRACT_PRESSURE_PLATE_BLOCK),
    Pair(DetectorRailBlock::class, ClaimPermission.DETECTOR_RAIL_BLOCK),
    Pair(EndPortalBlock::class, ClaimPermission.END_PORTAL_BLOCK),
    Pair(NetherPortalBlock::class, ClaimPermission.NETHER_PORTAL_BLOCK),
    Pair(LilyPadBlock::class, ClaimPermission.LILY_PAD_BLOCK),
    Pair(TripwireBlock::class, ClaimPermission.TRIPWIRE_BLOCK),
    Pair(WitherRoseBlock::class, ClaimPermission.WITHER_ROSE_BLOCK),
)
*/