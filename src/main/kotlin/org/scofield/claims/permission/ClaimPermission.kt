package org.scofield.claims.permission

import java.util.*

/**
 * All possible claim permissions
 *
 * @property USE_INTERACTABLE_BLOCKS Allow using interactable blocks such as pressure plates, chests, etc.
 * @property OPEN_DOORS Allow opening doors
 *
 * */
enum class ClaimPermission {
    DELETE_CLAIM,
    DOOR_BLOCK,

    // Block interact events
    ABSTRACT_BUTTON_BLOCK,
    ABSTRACT_PRESSURE_PLATE_BLOCK,
    DETECTOR_RAIL_BLOCK,
    END_PORTAL_BLOCK,
    NETHER_PORTAL_BLOCK,
    LILY_PAD_BLOCK,
    TRIPWIRE_BLOCK,
    WITHER_ROSE_BLOCK,

    // Entity interact events
    THROW_ENDER_PEARL,

    // World events
    FIRE_SPREAD,
    WATER_FLOW,
    LAVA_FLOW,

    // Generic permissions for compatibility with other mods
    GENERIC_FLUID_FLOW,
    GENERIC_BLOCK_INTERACTION,
    GENERIC_PROJECTILE_INTERACTION,
    GENERIC_ENTITY_INTERACTION,
}

typealias ClaimPermissions = EnumSet<ClaimPermission>

fun defaultClaimPermissions(): EnumSet<ClaimPermission> = EnumSet.noneOf(ClaimPermission::class.java)