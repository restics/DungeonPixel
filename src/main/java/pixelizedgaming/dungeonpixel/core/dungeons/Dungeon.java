package pixelizedgaming.dungeonpixel.core.dungeons;

import org.bukkit.Location;

public class Dungeon {
    DungeonRoom[] rooms;
    Location centralLocation;
    int sizeX;
    int sizeZ;
    boolean isRunning;

    public DungeonRoom[] getRooms() {
        return rooms;
    }

    public Location getCentralLocation() {
        return centralLocation;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
