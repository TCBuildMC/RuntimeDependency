package xyz.tcbuildmc.common.dependency.runtime.relocate;

import me.lucko.jarrelocator.JarRelocator;
import me.lucko.jarrelocator.Relocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DependencyRelocator {
    private final List<Relocation> rules;

    @Contract(pure = true)
    public DependencyRelocator(List<Relocation> rules) {
        this.rules = rules;
    }

    public DependencyRelocator(@NotNull Map<String, String> ruleMap) {
        List<Relocation> rules = new ArrayList<>();

        for (Map.Entry<String, String> ruleEntry : ruleMap.entrySet()) {
            rules.add(new Relocation(ruleEntry.getKey(), ruleEntry.getValue()));
        }

        this.rules = rules;
    }

    public DependencyRelocator(String from, String to) {
        List<Relocation> rules = new ArrayList<>();

        rules.add(new Relocation(from, to));
        this.rules = rules;
    }

    public void relocate(@NotNull File input, File output) {
        if (!input.getName().endsWith(".jar") || !output.getName().endsWith(".jar")) {
            throw new IllegalArgumentException("Only jar file can be relocated.");
        }

        try {
            System.out.println("Relocating " + input.getName());
            new JarRelocator(input, output, rules).run();
        } catch (IOException e) {
            throw new RuntimeException("Failed to relocate.", e);
        }
    }

    public void relocate(@NotNull Map<File, File> files) {
        for (Map.Entry<File, File> fileEntry : files.entrySet()) {
            this.relocate(fileEntry.getKey(), fileEntry.getValue());
        }
    }
}
