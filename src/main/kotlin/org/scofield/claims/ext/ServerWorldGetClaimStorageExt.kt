package org.scofield.claims.ext

import net.minecraft.server.world.ServerWorld
import org.scofield.claims.claim.storage.ClaimStorage
import org.scofield.claims.claim.storage.IClaimDataContainer

fun ServerWorld.getClaimStorage(): ClaimStorage = ((this as IClaimDataContainer).getClaimStorageData()) as ClaimStorage