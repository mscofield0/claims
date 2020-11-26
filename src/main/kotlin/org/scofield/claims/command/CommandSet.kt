package org.scofield.claims.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import org.scofield.claims.claim.Claim
import org.scofield.claims.ext.getClaimStorage
import org.scofield.claims.permission.ClaimPermission
import org.scofield.claims.utils.Point
import org.scofield.claims.utils.Rect

private typealias CommandLiteralType = LiteralArgumentBuilder<ServerCommandSource>
private typealias CommandWithArgumentType<R> = RequiredArgumentBuilder<ServerCommandSource, R>

class CommandSet {

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>, dedicated: Boolean) {
            dispatcher.register(
                CommandManager.literal("claims")
                    .then(newCommand())
            )
        }

        private fun newCommand(): CommandLiteralType {
           return CommandManager.literal("new")
                .then(
                    CommandManager.argument("fromX", IntegerArgumentType.integer()).then(
                        CommandManager.argument("fromY", IntegerArgumentType.integer()).then(
                            CommandManager.argument("toX", IntegerArgumentType.integer()).then(
                                CommandManager.argument("toY", IntegerArgumentType.integer()).executes(::newClaim)
                            )
                        )
                    )
                )
        }
    }
}

fun newClaim(ctx: CommandContext<ServerCommandSource>): Int {
    val player = ctx.source.player
    val world = player.serverWorld
    val claimStorage = world.getClaimStorage()

    val from = Point(
        IntegerArgumentType.getInteger(ctx, "fromX"),
        IntegerArgumentType.getInteger(ctx, "fromY"),
    )

    val to = Point(
        IntegerArgumentType.getInteger(ctx, "toX"),
        IntegerArgumentType.getInteger(ctx, "toY"),
    )

    // Check point validity

    val area = Rect(from, to)

    claimStorage.createClaim(player, area)
}

/*

Base command
/claims

/claims new
- /claims new <from> <to>
    <from> - 2 4
    <to> - 5 10
-

/claims delete
/claims edit
/claims groups
/claims info
/claims list


*/