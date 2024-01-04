package pixelizedgaming.dungeonpixel.core.dungeons;

import org.bukkit.Location;
import org.bukkit.Material;

public class DungeonRoom {

    int sizeX;
    int sizeY;
    int sizeZ;
    Location roomCenter;
    // schematic stuff
    Location[] spawnLocations;
    Location topRight;
    Location bottomLeft;

    //TODO: remove when implementing schematics
    Material buildMaterial;

    public DungeonRoom(int sizeX, int sizeY, int sizeZ, Location roomCenter) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.roomCenter = roomCenter;
        topRight = roomCenter.add(sizeX/2, 0, sizeZ/2);
        bottomLeft = roomCenter.subtract(sizeX/2, 0, sizeZ/2);
    }

    public DungeonRoom(RoomType roomType, Location roomCenter) {
        this(roomType.x, roomType.y, roomType.z, roomCenter);
        buildMaterial = roomType.mat;

    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public void placeSchematic(){
        DungeonGenerator.buildFrame(roomCenter,sizeX,sizeY,sizeZ,buildMaterial);
    }

    public void spawnMobs(){

    }
    @Override
    public String toString(){
        return roomCenter + "\n material: " + buildMaterial + "\n sizeX: " + sizeX + "\nsizeZ: " + sizeZ ;
    }
}
