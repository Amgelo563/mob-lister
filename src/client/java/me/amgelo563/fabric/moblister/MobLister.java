package me.amgelo563.fabric.moblister;

import com.mojang.brigadier.Command;
import me.amgelo563.fabric.moblister.command.CommandExecutor;
import me.amgelo563.fabric.moblister.registry.MobRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class MobLister implements ClientModInitializer {
    private final CommandExecutor executor = new CommandExecutor(new MobRegistry());

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                ClientCommandManager
                        .literal("moblister")
                        .then(
                                ClientCommandManager
                                        .literal("list")
                                        .executes(context -> {
                                            executor.executeList(context.getSource());
                                            return Command.SINGLE_SUCCESS;
                                        })
                        )
                        .then(
                                ClientCommandManager
                                        .literal("duplicates")
                                        .executes(context -> {
                                            executor.executeDuplicates(context.getSource());
                                            return Command.SINGLE_SUCCESS;
                                        })
                        )
                        .executes(context -> {
                            executor.executeUsage(context.getSource());
                            return 0;
                        })
        ));
    }
}
