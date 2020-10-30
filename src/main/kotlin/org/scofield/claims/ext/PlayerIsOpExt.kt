package org.scofield.claims.ext

import net.minecraft.server.network.ServerPlayerEntity

fun ServerPlayerEntity.isOp() = this.server.playerManager.isOperator(this.gameProfile)