package pixelizedgaming.dungeonpixel.util;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;

public class RayCastResult {
    Location endLocation; //defaults to 200 blocks from firing position
    HashSet<Location> passedBlocks;
    HashSet<LivingEntity> passedEntities; // maybe change this later

    // intentionally package private
    RayCastResult(){
        passedBlocks = new HashSet<>();
        passedEntities = new HashSet<>();
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public HashSet<Location> getPassedBlocks() {
        return passedBlocks;
    }

    public HashSet<LivingEntity> getPassedEntities() {
        return passedEntities;
    }
}
