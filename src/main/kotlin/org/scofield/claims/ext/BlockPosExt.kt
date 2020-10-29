package org.scofield.claims.ext

import org.scofield.claims.claim.Claim

import net.minecraft.util.math.BlockPos
import org.scofield.claims.utils.Point

fun BlockPos.toPoint() = Point(this.x, this.y)