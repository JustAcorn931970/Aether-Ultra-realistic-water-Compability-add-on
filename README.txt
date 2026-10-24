AETHERIUM + ULTRA REALISTIC WATER SAFE RECOVERY BUILD
Minecraft 26.3 / Fabric / Java 25
Version 0.3.0

WHY THIS BUILD EXISTS
The previous 0.2.0 proxy approach replaced Ultra Realistic Water's intentionally empty
FluidRenderHandler with Fabric's default fluid tessellator. That could make water look like
ordinary Minecraft water. The Mixin also ran during client initialization and could fail if
its injection point did not match the exact runtime bytecode.

WHAT CHANGED
- Removed the renderer Mixin and its Mixin configuration completely.
- The add-on no longer changes URW's custom fluid rendering handler.
- The client entrypoint only logs which mods are present and warns that full shader integration
  is not implemented in this safe recovery build.
- GitHub Actions verifies the mod metadata and uploaded JAR without requiring a Mixin config.

IMPORTANT LIMITATION
This build is a crash-safe rollback of the previous workaround; it is NOT a completed BSL integration.
Ultra Realistic Water 1.2.3 generates and draws its own custom Vulkan surface using its own Vulkan
command buffers. Aetherium converts standard shader-pack programs into its own Vulkan render pipeline.
Simply changing a FluidRenderHandler cannot submit URW's custom surface mesh to BSL's gbuffers_water
pass, and it necessarily tends to show ordinary fluid geometry instead.

If URW's realistic surface is still invisible when Aetherium is enabled after removing the old
0.2.0 bridge JAR, a true fix needs a renderer-stage integration with Aetherium (or a change from
one of the upstream mod authors). This package deliberately avoids pretending that the hard part
has been implemented.

INSTALL / RECOVERY
1. Exit Minecraft.
2. Remove the OLD aetherium-urw-bridge-26.3-0.2.0.jar from the mods folder.
3. Build this source project through GitHub Actions with Java 25 / Gradle 9.8.2.
4. If you want to test this safe build, put the generated 0.3.0 JAR in mods/ alongside Aetherium,
   Sodium, Fabric API, and Ultra Realistic Water 1.2.3.
5. If the game still crashes, remove the bridge JAR. Save latest.log and the crash report.

BUILD
The GitHub workflow builds the project, validates fabric.mod.json and uploads the resulting JAR.
A full Gradle build and in-game test have not been run in this authoring environment.
