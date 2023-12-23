package pixelizedgaming.dungeonpixel.core.dungeons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//dungeon factory class, also includes map building methods
public class DungeonGenerator extends BukkitRunnable {
    //add settings here
    Dungeon dungeonInstance;
    private Map<Vector, DungeonRoom> registeredRooms;

    public static void init(){

    }
    public DungeonGenerator(){
        registeredRooms = new HashMap<>();
    }

    // location = center of perimeter, size = 1/2 side length of perimeter square
    public void generateMap(Location dungeonLocation, int size){

    }

    // returns a list of randomly generated rooms within the bounds
    public DungeonRoom[] constructRooms(int count, int sizeX, int sizeZ){
        Location center = dungeonInstance.centralLocation;
        Location topRight = center.add(sizeX/2, 0, sizeZ/2);
        Location bottomleft = center.subtract(sizeX/2, 0, sizeZ/2);

        DungeonRoom[] rooms = new DungeonRoom[count];
        Random rand = new Random();
        for(DungeonRoom room : rooms){
            RoomType type = RoomType.values()[rand.nextInt(RoomType.values().length - 1) + 1]; // we don't want to place corridors here
            room = new DungeonRoom(type);
            room.placeSchematic();
        }
        return rooms;
    }

    public void onFinish(){

    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

    }

    public static void buildFrame(Location center, int sizeX, int sizeY, int sizeZ, Material material){
        Location bottomCorner = center.subtract(sizeX/2, sizeY/2, sizeZ/2);
        for(int i = 0; i < sizeX; i++){
            for(int j = 0; j < sizeY; j++){
                for(int k = 0; k < sizeZ; k++){
                    Location currLocation = bottomCorner.add(i,j,k);
                    currLocation.getBlock().setType(Material.AIR);
                }
            }
        }
        for(int i = 0; i < sizeX; i++){
            bottomCorner.add(i,0,0).getBlock().setType(material);
            bottomCorner.add(i,sizeY,0).getBlock().setType(material);
            bottomCorner.add(i,0,sizeZ).getBlock().setType(material);
            bottomCorner.add(i,sizeY,sizeZ).getBlock().setType(material);
        }
        for(int j = 0; j < sizeY; j++){
            bottomCorner.add(0,j,0).getBlock().setType(material);
            bottomCorner.add(sizeX,j,0).getBlock().setType(material);
            bottomCorner.add(0,j,sizeZ).getBlock().setType(material);
            bottomCorner.add(sizeX,j,sizeZ).getBlock().setType(material);
        }
        for(int k = 0; k < sizeZ; k++){
            bottomCorner.add(0,0,k).getBlock().setType(material);
            bottomCorner.add(0,sizeY,k).getBlock().setType(material);
            bottomCorner.add(sizeX,0,k).getBlock().setType(material);
            bottomCorner.add(sizeX,sizeY,k).getBlock().setType(material);
        }
    }
}
