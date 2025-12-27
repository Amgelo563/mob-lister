package me.amgelo563.fabric.moblister.registry;

import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Calculates and stores mob entry results, including duplicates, ready to be listed
public final class MobRegistry {

    private @Nullable List<MobEntry> allMobs = null;
    private @Nullable DuplicateResult duplicates = null;

    public @NotNull DuplicateResult getDuplicates() {
        if (duplicates != null) {
            return duplicates;
        }

        List<MobEntry> mobs = getAllMobs();

        Map<String, List<MobEntry>> byId =
                mobs.stream().collect(Collectors.groupingBy(MobEntry::id));

        Map<String, List<MobEntry>> byName =
                mobs.stream().collect(Collectors.groupingBy(MobEntry::name));

        Map<String, List<MobEntry>> duplicateIds =
                byId.entrySet().stream()
                        .filter(e -> e.getValue().size() > 1)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));

        Map<String, List<MobEntry>> duplicateNames =
                byName.entrySet().stream()
                        .filter(e -> e.getValue().size() > 1)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ));

        this.duplicates = new DuplicateResult(duplicateIds, duplicateNames);

        return duplicates;
    }

    public @NotNull List<MobEntry> getAllMobs() {
        if (allMobs != null) {
            return allMobs;
        }

        List<MobEntry> mobs = new ArrayList<>();
        Registries.ENTITY_TYPE.forEach(entityType -> {
            String id = Registries.ENTITY_TYPE.getId(entityType).toString();
            String name = id.split(":")[1];
            mobs.add(new MobEntry(id, name, entityType));
        });

        mobs.sort(Comparator.comparing(MobEntry::id));

        this.allMobs = List.copyOf(mobs);

        return allMobs;
    }

    public record MobEntry(String id, String name, EntityType<?> entityType) {
    }

    public record DuplicateResult(
            Map<String, List<MobEntry>> duplicateIds,
            Map<String, List<MobEntry>> duplicateNames
    ) {
        public boolean isEmpty() {
            return duplicateIds.isEmpty() && duplicateNames.isEmpty();
        }
    }
}
