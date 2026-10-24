package dev.acorn.aetheriumurw.mixin;

import dev.acorn.aetheriumurw.BridgeConfig;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Aetherium-specific bridge for the invisible fluid handler in Ultra Realistic Water 1.2.3.
 * The custom URW renderer draws its own Vulkan mesh. This mixin adds a standard fluid proxy
 * so Aetherium's already-converted shader pack (such as BSL) can render a water-pass proxy.
 */
@Pseudo
@Mixin(targets = "me.skaffy.client.RealisticWaterClient", remap = false)
public abstract class RealisticWaterClientMixin {
    private static final String REGISTER =
            "Lnet/fabricmc/fabric/api/client/render/fluid/v1/FluidRenderingRegistry;register"
            + "(Lnet/minecraft/world/level/material/Fluid;"
            + "Lnet/minecraft/world/level/material/Fluid;"
            + "Lnet/minecraft/client/renderer/block/FluidModel$Unbaked;"
            + "Lnet/fabricmc/fabric/api/client/render/fluid/v1/FluidRenderHandler;)V";

    @ModifyArg(
            method = "registerInvisibleFluid",
            at = @At(value = "INVOKE", target = REGISTER, ordinal = 0),
            index = 3,
            require = 1
    )
    private static FluidRenderHandler aetheriumUrwBridge$water(FluidRenderHandler original) {
        return BridgeConfig.selectHandler(original);
    }

    @ModifyArg(
            method = "registerInvisibleFluid",
            at = @At(value = "INVOKE", target = REGISTER, ordinal = 1),
            index = 3,
            require = 1
    )
    private static FluidRenderHandler aetheriumUrwBridge$lava(FluidRenderHandler original) {
        return BridgeConfig.selectHandler(original);
    }

    @ModifyArg(
            method = "registerInvisibleFluid",
            at = @At(value = "INVOKE", target = REGISTER, ordinal = 2),
            index = 3,
            require = 1
    )
    private static FluidRenderHandler aetheriumUrwBridge$slime(FluidRenderHandler original) {
        return BridgeConfig.selectHandler(original);
    }
}
