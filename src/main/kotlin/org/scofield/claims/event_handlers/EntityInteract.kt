@file:JvmName("EntityInteract")

package org.scofield.claims.event_handlers

import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import org.scofield.claims.claim.hasPermission
import org.scofield.claims.claim.storage.claimAtPos

import org.scofield.claims.ext.toPoint
import org.scofield.claims.ext.toServerWorld
import org.scofield.claims.ext.getClaimStorage
import org.scofield.claims.ext.toPermissionCheckType
import org.scofield.claims.permission.ClaimPermission
import org.scofield.claims.permission.EntityInteractEventPermissionMap

fun permitProjectileHit(projectile: ProjectileEntity, hitResult: HitResult): Boolean {
    val claimStorage = projectile.world.toServerWorld().getClaimStorage()
    val pos = (hitResult as BlockHitResult).blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    // Allows for any projectile interaction
    if (claim.hasPermission(ClaimPermission.GENERIC_PROJECTILE_INTERACTION)) return true

    // If the projectile has no owner, it originated from an automated source
    // thus we need to cancel the event as it poses a security risk.
    val owner = projectile.owner ?: return false

    val entityType = owner.toPermissionCheckType()
    val neededPermission = EntityInteractEventPermissionMap.PROJECTILE_PERMISSION_MAP[entityType] ?: return false

    return claim.hasPermission(owner.uuid, neededPermission)
}