package dev.acorn.aetheriumurw;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Safe-mode initializer for the Aetherium / Ultra Realistic Water bridge.
 *
 * This release intentionally installs no renderer mixins. The previous proxy
 * approach replaced Ultra Realistic Water's custom invisible fluid handler with
 * vanilla fluid tessellation, which could produce ordinary-looking fluids and
 * was not a true integration with Aetherium's shader passes.
 */
public final class BridgeSafetyClient implements ClientModInitializer {
    private static final System.Logger LOGGER = System.getLogger("Aetherium-URW-Bridge");

    @Override
    public void onInitializeClient() {
        FabricLoader loader = FabricLoader.getInstance();
        boolean aetherium = loader.isModLoaded("aetherium");
        boolean realisticWater = loader.isModLoaded("realistic-water");

        LOGGER.log(System.Logger.Level.INFO,
                "Safe recovery build loaded. No fluid renderer mixins are installed.");

        if (aetherium && realisticWater) {
            LOGGER.log(System.Logger.Level.WARNING,
                    "Aetherium and Ultra Realistic Water are detected. This safe build preserves URW's "
                    + "own renderer and deliberately does not add vanilla fluid proxy geometry. It prevents "
                    + "the previous proxy behavior, but it is not yet a complete BSL/Aetherium render-pipeline integration.");
        } else {
            LOGGER.log(System.Logger.Level.INFO,
                    "Detected mods: aetherium=" + aetherium + ", realistic-water=" + realisticWater);
        }
    }
}
