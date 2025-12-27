package me.amgelo563.fabric.moblister.command;

import me.amgelo563.fabric.moblister.registry.MobRegistry;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import java.util.List;

import static net.minecraft.text.Text.literal;

public class CommandExecutor {
    private final MobRegistry registry;

    public CommandExecutor(MobRegistry reg) {
        registry = reg;
    }

    public void executeUsage(FabricClientCommandSource source) {
        source.sendFeedback(
                literal("§cUsage: /moblister <list|duplicates>")
        );
    }

    public void executeList(FabricClientCommandSource source) {
        List<MobRegistry.MobEntry> allMobs = registry.getAllMobs();

        source.sendFeedback(
                Text.literal("§6=== All Registered Mobs ===\n§f"));
        source.sendFeedback(
                Text.literal("§7Total: §f" + allMobs.size()));

        for (int i = 0; i < allMobs.size(); i++) {
            MobRegistry.MobEntry entry = allMobs.get(i);
            int index = i + 1;

            source.sendFeedback(Text.literal(
                    String.format("§7[%d]§f %s §8(§7%s§8)",
                            index, entry.name(), entry.id())
            ));
        }
    }

    public void executeDuplicates(FabricClientCommandSource source) {
        MobRegistry.DuplicateResult duplicates = registry.getDuplicates();

        source.sendFeedback(
                Text.literal("§6=== Duplicate Analysis ===\n§f"));

        if (duplicates.isEmpty()) {
            source.sendFeedback(
                    Text.literal("§aNo duplicates found!"));
            return;
        }

        if (!duplicates.duplicateIds().isEmpty()) {
            source.sendFeedback(Text.literal(
                    "§cDuplicate IDs: " + duplicates.duplicateIds().size()
            ));

            duplicates.duplicateIds().values().forEach(list -> {
                source.sendFeedback(Text.literal(
                        "  §7ID: §f" + list.get(0).id()
                ));

                list.forEach(e ->
                        source.sendFeedback(Text.literal(
                                "    §8- §f" + e.name()
                        )));
            });
        }

        if (!duplicates.duplicateNames().isEmpty()) {
            source.sendFeedback(Text.literal(
                    "§cDuplicate Names: " + duplicates.duplicateNames().size()
            ));

            duplicates.duplicateNames().values().forEach(list -> {
                source.sendFeedback(Text.literal(
                        "  §7Name: §f" + list.get(0).name()
                ));

                list.forEach(e ->
                        source.sendFeedback(Text.literal(
                                "    §8- §f" + e.id()
                        )));
            });
        }
    }
}
