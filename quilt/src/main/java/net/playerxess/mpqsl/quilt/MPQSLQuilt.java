package net.playerxess.mpqsl.quilt;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

import net.playerxess.mpqsl.fabriclike.MPQSLFabricLike;

public final class MPQSLQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        // Run the Fabric-like setup.
        MPQSLFabricLike.init();
    }
}
