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
    USE_INTERACTABLE_BLOCKS,
    OPEN_DOORS,

}

typealias ClaimPermissions = EnumSet<ClaimPermission>

fun defaultClaimPermissions(): EnumSet<ClaimPermission> = EnumSet.noneOf(ClaimPermission::class.java)