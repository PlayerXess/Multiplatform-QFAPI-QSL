package net.playerxess.mpqsl.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.playerxess.mpqsl.MPQSL;

@Mod(MPQSL.MOD_ID)
public final class MPQSLForge {
    public MPQSLForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(MPQSL.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        MPQSL.init();
    }
}
