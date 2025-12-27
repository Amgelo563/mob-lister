package me.amgelo563.fabric.moblister.command;

import me.amgelo563.fabric.moblister.registry.MobRegistry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.List;

// Separated and with static executors to avoid adding state and business logic to the mixin, which is the only user of this class
public class CommandExecutor {
    private static final MobRegistry registry = new MobRegistry();

    public static void executeList(ServerCommandSource source) {
        List<MobRegistry.MobEntry> allMobs = registry.getAllMobs();

        source.sendFeedback(
                () -> Text.literal("§6=== All Registered Mobs ===\n§f"), false);
        source.sendFeedback(
                () -> Text.literal("§7Total: §f" + allMobs.size()), false);

        for (int i = 0; i < allMobs.size(); i++) {
            MobRegistry.MobEntry entry = allMobs.get(i);
            int index = i + 1;

            source.sendFeedback(() -> Text.literal(
                    String.format("§7[%d]§f %s §8(§7%s§8)",
                            index, entry.name(), entry.id())
            ), false);
        }
    }

    public static void executeDuplicates(ServerCommandSource source) {
        MobRegistry.DuplicateResult duplicates = registry.getDuplicates();

        source.sendFeedback(
                () -> Text.literal("§6=== Duplicate Analysis ===\n§f"), false);

        if (duplicates.isEmpty()) {
            source.sendFeedback(
                    () -> Text.literal("§aNo duplicates found!"), false);
            return;
        }

        if (!duplicates.duplicateIds().isEmpty()) {
            source.sendFeedback(() -> Text.literal(
                    "§cDuplicate IDs: " + duplicates.duplicateIds().size()
            ), false);

            duplicates.duplicateIds().values().forEach(list -> {
                source.sendFeedback(() -> Text.literal(
                        "  §7ID: §f" + list.get(0).id()
                ), false);

                list.forEach(e ->
                        source.sendFeedback(() -> Text.literal(
                                "    §8- §f" + e.name()
                        ), false));
            });
        }

        if (!duplicates.duplicateNames().isEmpty()) {
            source.sendFeedback(() -> Text.literal(
                    "§cDuplicate Names: " + duplicates.duplicateNames().size()
            ), false);

            duplicates.duplicateNames().values().forEach(list -> {
                source.sendFeedback(() -> Text.literal(
                        "  §7Name: §f" + list.get(0).name()
                ), false);

                list.forEach(e ->
                        source.sendFeedback(() -> Text.literal(
                                "    §8- §f" + e.id()
                        ), false));
            });
        }
    }
}
