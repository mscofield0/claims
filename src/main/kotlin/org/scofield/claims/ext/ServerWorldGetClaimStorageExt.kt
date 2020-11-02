package org.scofield.claims.ext

import net.minecraft.server.world.ServerWorld
import org.scofield.claims.claim.storage.ClaimStorage
import org.scofield.claims.claim.storage.IClaimStorageContainer

fun ServerWorld.getClaimStorage(): ClaimStorage = ((this as IClaimStorageContainer).getClaimStorageData()) as ClaimStorage