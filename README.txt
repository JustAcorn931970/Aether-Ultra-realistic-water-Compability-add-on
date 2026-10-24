AETHERIUM + ULTRA REALISTIC WATER SHADER BRIDGE
Minecraft 26.3 / Fabric / Java 25

WHAT THIS ADD-ON DOES
Aetherium converts standard shader-pack programs (including BSL's gbuffers_water program)
for its Vulkan renderer. Ultra Realistic Water 1.2.3 registers its realistic water/lava/slime
fluids with a handler whose renderFluid method intentionally does nothing, because URW normally
draws those surfaces using its own custom Vulkan renderer.

This add-on targets Aetherium specifically. It replaces URW's invisible handler with an empty
FluidRenderHandler implementation that inherits Fabric's default renderFluid method. That
creates standard fluid proxy geometry which Aetherium's shader pipeline can process. URW's own
custom simulation and Vulkan surface renderer are left enabled.

IMPORTANT LIMITATION
This is a proxy/G-buffer compatibility bridge, not a native API integration of URW's custom
particle-meshed water surface into the BSL shader program. Aetherium does not currently expose a
public third-party API for inserting URW's custom Vulkan surface mesh into arbitrary shader-pack
passes. The visible result can vary by pack and may show proxy/custom-surface overlap. BSL is not
included in this project; install the BSL shader ZIP separately in Aetherium's shaderpacks folder.

REQUIREMENTS
- Minecraft Java 26.3
- Fabric Loader 0.19.5+
- Fabric API 0.161.0+26.3 or compatible 26.3 build
- Sodium 0.9.2+ (required by Aetherium)
- Aetherium 0.1.1+ for Minecraft 26.3
- Ultra Realistic Water 1.2.3 for Minecraft 26.3
- Vulkan-capable GPU and driver

INSTALLING THE BUILT JAR
1. Build the source through GitHub Actions, or run Gradle with JDK 25 and Gradle 9.8.2.
2. Put aetherium-urw-bridge-26.3-0.2.0.jar in the client's mods folder.
3. Install Aetherium, Sodium, Fabric API, and Ultra Realistic Water as usual.
4. Put BSL's shader ZIP (downloaded separately) in .minecraft/shaderpacks or use Aetherium's shader-pack screen.
5. Launch on Vulkan and select BSL through Aetherium.
6. Check config/aetherium-urw-bridge.properties if you want to disable/force the proxy.

CONFIG
mode=auto : use the proxy when Aetherium is installed (default)
mode=on   : force the proxy
mode=off  : preserve URW's original invisible fluid handler
Restart after editing this file.

BUILD
GitHub Actions uses Ubuntu 24.04, Temurin JDK 25 and Gradle 9.8.2. The build workflow verifies
that the produced JAR contains fabric.mod.json and the mixin configuration, then uploads it as
an Actions artifact. This package has not yet been compiled or tested in-game in its authoring
environment; validate the GitHub build and inspect the water in-game before publishing it.
