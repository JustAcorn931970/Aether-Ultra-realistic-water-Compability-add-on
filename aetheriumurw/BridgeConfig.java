package dev.acorn.aetheriumurw;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;

/**
 * Routes Ultra Realistic Water's invisible fluid registrations through Minecraft's
 * normal fluid tessellation while Aetherium is installed. That standard geometry is
 * visible to Aetherium's converted shader-pack fluid pass (for example BSL's water pass).
 * The simulation and URW's own custom Vulkan surface renderer are not replaced here.
 */
public final class BridgeConfig {
    private static final System.Logger LOGGER = System.getLogger("Aetherium-URW-Bridge");
    private static final Path FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("aetherium-urw-bridge.properties");
    private static String cachedMode;
    private static boolean logged;

    private BridgeConfig() {}

    public static FluidRenderHandler selectHandler(FluidRenderHandler original) {
        if (!shouldUseShaderProxy()) {
            return original;
        }

        if (!logged) {
            logged = true;
            LOGGER.log(System.Logger.Level.INFO,
                    "Aetherium shader proxy is active: URW fluids will also submit standard fluid geometry "
                    + "to Aetherium's converted shader-pack pipeline. URW's custom Vulkan surface remains enabled; "
                    + "visual overlap can vary by shader pack and has not been runtime-tested.");
        }
        return StandardFluidProxy.INSTANCE;
    }

    private static boolean shouldUseShaderProxy() {
        return switch (mode()) {
            case "on", "true", "yes", "1" -> true;
            case "off", "false", "no", "0" -> false;
            default -> FabricLoader.getInstance().isModLoaded("aetherium");
        };
    }

    private static String mode() {
        if (cachedMode != null) {
            return cachedMode;
        }

        Properties properties = new Properties();
        if (Files.exists(FILE)) {
            try (Reader reader = Files.newBufferedReader(FILE)) {
                properties.load(reader);
            } catch (IOException e) {
                LOGGER.log(System.Logger.Level.WARNING,
                        "Couldn't read " + FILE + "; using auto mode.", e);
            }
        } else {
            properties.setProperty("mode", "auto");
            try {
                Files.createDirectories(FILE.getParent());
                try (Writer writer = Files.newBufferedWriter(FILE)) {
                    properties.store(writer,
                            "Aetherium + Ultra Realistic Water shader bridge; modes: auto, on, off");
                }
            } catch (IOException e) {
                LOGGER.log(System.Logger.Level.WARNING, "Couldn't create " + FILE, e);
            }
        }

        String value = properties.getProperty("mode", "auto").trim().toLowerCase(Locale.ROOT);
        cachedMode = switch (value) {
            case "on", "true", "yes", "1" -> "on";
            case "off", "false", "no", "0" -> "off";
            default -> "auto";
        };
        return cachedMode;
    }

    /**
     * An empty handler uses FluidRenderHandler's default renderFluid implementation,
     * which delegates to the vanilla/Fabric fluid tessellator. URW's original handler
     * explicitly overrides renderFluid with an empty method; this one does not.
     */
    private enum StandardFluidProxy implements FluidRenderHandler {
        INSTANCE
    }
}
