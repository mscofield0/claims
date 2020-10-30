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
    INTERACT_WITH_DOOR,
    INTERACT_WITH_PRESSURE_PLATE,
    INTERACT_WITH_NETHER_PORTAL,
}

typealias ClaimPermissions = EnumSet<ClaimPermission>

fun defaultClaimPermissions(): EnumSet<ClaimPermission> = EnumSet.noneOf(ClaimPermission::class.java)