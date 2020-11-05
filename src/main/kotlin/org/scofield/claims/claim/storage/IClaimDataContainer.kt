package org.scofield.claims.claim.storage

import java.util.*

interface IClaimDataContainer {
    fun getClaimData_(): PlayerClaimData
    fun getPlayerUUID(): UUID
}