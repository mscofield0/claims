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
    OPEN_WORKBENCH,
    OPEN_LECTERN,
    TAKE_LECTERN_BOOK,
    PLACE_LECTERN_BOOK,
    OTHER_INTERACTABLE_BLOCKS,

    // Entity interact events
    THROW_ENDER_PEARL,
    ENTER_BOAT,
    ACCESS_MINECART_CHEST,
    ENTER_MINECART,
    TRADE_WITH_VILLAGER,
    ROTATE_ITEM_FRAME,
    INTERACT_WITH_ANIMALS,
    ATTACK_PLAYER,
    ATTACK_ANIMAL,
    ABSORB_XP,
    INTERACT_WITH_ARMOR_STAND,

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