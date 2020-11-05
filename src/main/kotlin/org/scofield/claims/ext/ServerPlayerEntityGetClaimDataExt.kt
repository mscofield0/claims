package org.scofield.claims.ext

import net.minecraft.server.world.ServerWorld
import org.scofield.claims.claim.storage.IClaimDataContainer
import org.scofield.claims.claim.storage.PlayerClaimData

fun ServerWorld.getClaimData(): PlayerClaimData = (this as IClaimDataContainer).getClaimData_()