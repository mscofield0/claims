package org.scofield.claims.permission

import java.util.*

class GroupClaimPermissions(
    val playersInGroup: MutableList<UUID>,
    var permissions: ClaimPermissions
)