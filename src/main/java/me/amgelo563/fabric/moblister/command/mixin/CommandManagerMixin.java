package me.amgelo563.fabric.moblister.command.mixin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import me.amgelo563.fabric.moblister.command.CommandExecutor;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// My precious little mixin (to avoid FAPI), my precious...
@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;Lnet/minecraft/command/CommandRegistryAccess;)V",
            at = @At("TAIL")
    )
    private void onInit(
            CommandManager.RegistrationEnvironment environment, CommandRegistryAccess commandRegistryAccess, CallbackInfo ci
    ) {
        if (environment != CommandManager.RegistrationEnvironment.DEDICATED &&
                environment != CommandManager.RegistrationEnvironment.INTEGRATED) {
            return;
        }

        CommandDispatcher<ServerCommandSource> dispatcher =
                ((CommandManager) (Object) this).getDispatcher();

        dispatcher.register(
                CommandManager.literal("moblister")
                        .then(CommandManager.literal("list")
                                .executes(context -> {
                                    CommandExecutor.executeList(context.getSource());
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .then(CommandManager.literal("duplicates")
                                .executes(context -> {
                                    CommandExecutor.executeDuplicates(context.getSource());
                                    return Command.SINGLE_SUCCESS;
                                }))
                        .executes(context -> {
                            context.getSource().sendFeedback(
                                    () -> Text.literal("§cUsage: /moblister <list|duplicates>"),
                                    false
                            );
                            return 0;
                        })
        );
    }
}