@file:JvmName("EntityInteract")

package org.scofield.claims.event_handlers

import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.WitherEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.entity.passive.AnimalEntity
import net.minecraft.entity.passive.TameableEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.entity.vehicle.AbstractMinecartEntity
import net.minecraft.entity.vehicle.BoatEntity
import net.minecraft.entity.vehicle.StorageMinecartEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import org.scofield.claims.claim.hasPermission

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

fun permitEntityInteractionWithHand(world: ServerWorld, player: ServerPlayerEntity, entity: Entity): Boolean {
    val claimStorage = world.getClaimStorage()
    val pos = entity.blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    return when(entity) {
        is BoatEntity -> claim.hasPermission(player.uuid, ClaimPermission.ENTER_BOAT)
        is AbstractMinecartEntity -> {
            when(entity) {
                is StorageMinecartEntity -> claim.hasPermission(player.uuid, ClaimPermission.ACCESS_MINECART_CHEST)
                else -> claim.hasPermission(player.uuid, ClaimPermission.ENTER_MINECART)
            }
        }
        is VillagerEntity -> claim.hasPermission(player.uuid, ClaimPermission.TRADE_WITH_VILLAGER)
        is ItemFrameEntity -> claim.hasPermission(player.uuid, ClaimPermission.ROTATE_ITEM_FRAME)
        is TameableEntity -> entity.isOwner(player)
        is AnimalEntity -> claim.hasPermission(player.uuid, ClaimPermission.INTERACT_WITH_ANIMALS)

        // If it is an unchecked entity, it is better just to deny the interaction rather than allowing a
        // potential security breach.
        else -> false
    }
}

fun permitPreventDamage(entity: LivingEntity, damageSource: DamageSource): Boolean {
    val world = entity.world.toServerWorld()
    val claimStorage = world.getClaimStorage()
    val pos = entity.blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    // [WARNING] - Potential bypass here, the attacker would have to place a Dispenser or something
    // aimed at the player in a claim, in which case, the player would receive damage if found
    // on the correct trajectory from where the Dispenser is attacking.
    //
    // Marked as Will Not Solve until a good reason
    val attacker = entity.attacker ?: return false
    return if (attacker is ServerPlayerEntity) {
        !claim.hasPermission(attacker.uuid, ClaimPermission.ATTACK_ENTITY)
    } else {
        false
    }
}

fun permitWitherDestruction(wither: WitherEntity): Boolean {
    val world = wither.world.toServerWorld()
    val claimStorage = world.getClaimStorage()
    val pos = wither.blockPos.toPoint()

    // [Note] - Maybe change to destroy blocks outside the claim at some future date.
    for (x in -1..1) {
        for (z in -1..1) {
            claimStorage.claimAtPos(pos.add(x, z)) ?: return false
        }
    }

    return true
}

fun permitXpAbsorb(player: ServerPlayerEntity): Boolean {
    val world = player.serverWorld
    val claimStorage = world.getClaimStorage()
    val pos = player.blockPos.toPoint()
    val claim = claimStorage.claimAtPos(pos) ?: return true

    return claim.hasPermission(player.uuid, ClaimPermission.ABSORB_XP)
}