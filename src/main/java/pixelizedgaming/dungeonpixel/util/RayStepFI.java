package pixelizedgaming.dungeonpixel.util;

import org.bukkit.Location;

// executes arbitrary function at every ray step.
@FunctionalInterface
public interface RayStepFI { void rayStep(Location location, double step);}