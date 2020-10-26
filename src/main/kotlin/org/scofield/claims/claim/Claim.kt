package org.scofield.claims.claim

import org.scofield.claims.permission.*
import org.scofield.claims.utils.Rect
import org.scofield.claims.utils.intersects
import org.scofield.claims.utils.partOf

import java.util.*

data class Claim(
    // Identifiers
    val ownerId: UUID,

    // Location and space
    var area: Rect,

    // Relation to other claims
    var parentClaimId: UUID? = null,
    val subclaims: MutableList<UUID> = mutableListOf(),

    // Permissions
    val defaultPermissions: ClaimPermissions = defaultClaimPermissions(),
    val groupPermissions: MutableMap<String, ClaimPermissions> = mutableMapOf(),

    // Tracking flags
    var dirty: Boolean = false,
    var removed: Boolean = false,
)

infix fun Claim.intersects(other: Claim) = this.area intersects other.area
infix fun Claim.contains(other: Claim) = this.area partOf other.area