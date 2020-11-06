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
    // Claim manipulation
    DELETE_CLAIM,

    // Block interact events
    ABSTRACT_BUTTON_BLOCK,
    ABSTRACT_PRESSURE_PLATE_BLOCK,
    DETECTOR_RAIL_BLOCK,
    END_PORTAL_BLOCK,
    NETHER_PORTAL_BLOCK,
    LILY_PAD_BLOCK,
    TRIPWIRE_BLOCK,
    WITHER_ROSE_BLOCK,
    PLACE_BLOCKS,
    REMOVE_BLOCKS,
    TRAMPLE_TURTLE_EGG,
    OPEN_STORAGE,
    OPEN_DOOR,

    // Entity interact events
    THROW_ENDER_PEARL,
    ENTER_BOAT,
    ACCESS_MINECART_CHEST,
    ENTER_MINECART,
    TRADE_WITH_VILLAGER,
    ROTATE_ITEM_FRAME,
    INTERACT_WITH_ANIMALS,
    ATTACK_ENTITY,
    ABSORB_XP,

    // World events
    FIRE_SPREAD,
    EXPLOSIONS,
    WATER_FLOW,
    LAVA_FLOW,
    INTERCLAIM_PISTON_PUSH_PULL,
    INTERCLAIM_PISTON_DESTROY,
    START_RAID,

    // Generic permissions for compatibility with other mods
    GENERIC_FLUID_FLOW,
    GENERIC_BLOCK_INTERACTION,
    GENERIC_PROJECTILE_INTERACTION,
    GENERIC_ENTITY_INTERACTION,
}

typealias ClaimPermissions = EnumSet<ClaimPermission>

fun defaultClaimPermissions(): EnumSet<ClaimPermission> = EnumSet.noneOf(ClaimPermission::class.java)