package net.playerxess.mpqsl.fabric;

import net.fabricmc.api.ModInitializer;

import net.playerxess.mpqsl.fabriclike.MPQSLFabricLike;

public final class MPQSLFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run the Fabric-like setup.
        MPQSLFabricLike.init();
    }
}
