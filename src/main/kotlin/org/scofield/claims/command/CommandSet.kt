package org.scofield.claims.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

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

        fun newCommand(): CommandLiteralType {
           return CommandManager.literal("new")
                .then(
                    CommandManager.argument("fromX", IntegerArgumentType.integer()).then(
                        CommandManager.argument("fromY", IntegerArgumentType.integer()).then(
                            CommandManager.argument("toX", IntegerArgumentType.integer()).then(
                                CommandManager.argument("toY", IntegerArgumentType.integer())
                            )
                        )
                    )
                )
        }
    }
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