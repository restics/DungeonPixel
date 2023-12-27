package pixelizedgaming.dungeonpixel.core.dungeons;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

//dungeon factory class, also includes map building methods
public class DungeonGenerator extends BukkitRunnable {
    //add settings here
    Dungeon dungeonInstance;
    Random rand = new Random();
    private Map<Vector, DungeonRoom> locToRoom;
    ArrayList<DungeonRoom> rooms;

    public static void init(){

    }
    public DungeonGenerator(){
        dungeonInstance = new Dungeon();
        locToRoom = new HashMap<>();
        rooms = new ArrayList<>();
    }

    // location = center of perimeter, size = 1/2 side length of perimeter square
    public void generateMap(Location dungeonLocation, int size){
        dungeonInstance.centralLocation = dungeonLocation;
        dungeonInstance.rooms = constructRooms(15 + rand.nextInt(5), size * 2, size * 2).toArray(new DungeonRoom[0]);
    }

    // returns a list of randomly generated rooms within the bounds
    public ArrayList<DungeonRoom>  constructRooms(int count, int sizeX, int sizeZ){
        int roomCount = 10; //scale with floor difficulty?
        Location center = dungeonInstance.centralLocation;
        Location topRight = center.add(sizeX/2, 0, sizeZ/2);
        Location bottomleft = center.subtract(sizeX/2, 0, sizeZ/2);

        //TODO: Insert start in a quadrant
        int randPosX = center.getBlockX() - RoomType.START.x/2 - rand.nextInt(sizeX/2 - RoomType.START.x);
        int randPosZ = center.getBlockZ() - RoomType.START.z/2 - rand.nextInt(sizeZ/2 - RoomType.START.z);
        DungeonRoom startRoom = new DungeonRoom(RoomType.START);
        startRoom.roomCenter = new Location(center.getWorld(), randPosX, center.y(), randPosZ);
        startRoom.placeSchematic();
        rooms.add(startRoom);

        //TODO: Insert boss in quadrant diagonal to start quadrant
        randPosX = center.getBlockX() + RoomType.BOSS.x/2 + rand.nextInt(sizeX/2 - RoomType.BOSS.x);
        randPosZ = center.getBlockZ() + RoomType.BOSS.z/2 + rand.nextInt(sizeZ/2 - RoomType.BOSS.z);
        DungeonRoom bossRoom = new DungeonRoom(RoomType.BOSS);
        bossRoom.roomCenter = new Location(center.getWorld(), randPosX, center.y(), randPosZ);
        bossRoom.placeSchematic();
        rooms.add(bossRoom);

        for(int i = 0; i < count; i++){
            RoomType type = RoomType.values()[rand.nextInt(RoomType.values().length - 3) + 3]; // we don't want to place corridors, start, or boss here
            DungeonRoom room = new DungeonRoom(type);
            int tries = 100; // # of attempts to place a room
            while(isValidRoomPlacement(room) && tries > 0){
                randPosX = bottomleft.getBlockX() + rand.nextInt(sizeX);
                randPosZ = bottomleft.getBlockZ() + rand.nextInt(sizeZ);
                room.roomCenter = new Location(center.getWorld(), randPosX, center.y(), randPosZ);
                tries--;
            }

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
        System.out.println("IM GONNA BUILLLLLDDD!!");
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
            System.out.println("placing along x: iter " + i );
            bottomCorner.add(i,0,0).getBlock().setType(material);
            bottomCorner.add(i,sizeY,0).getBlock().setType(material);
            bottomCorner.add(i,0,sizeZ).getBlock().setType(material);
            bottomCorner.add(i,sizeY,sizeZ).getBlock().setType(material);
        }
        for(int j = 0; j < sizeY; j++){
            System.out.println("placing along y: iter " + j );
            bottomCorner.add(0,j,0).getBlock().setType(material);
            bottomCorner.add(sizeX,j,0).getBlock().setType(material);
            bottomCorner.add(0,j,sizeZ).getBlock().setType(material);
            bottomCorner.add(sizeX,j,sizeZ).getBlock().setType(material);
        }
        for(int k = 0; k < sizeZ; k++){
            System.out.println("placing along z: iter " + k );
            bottomCorner.add(0,0,k).getBlock().setType(material);
            bottomCorner.add(0,sizeY,k).getBlock().setType(material);
            bottomCorner.add(sizeX,0,k).getBlock().setType(material);
            bottomCorner.add(sizeX,sizeY,k).getBlock().setType(material);
        }
    }

    // return true if a collision is found
    public boolean checkRoomCollision(DungeonRoom a, DungeonRoom b){
//        if (a.roomCenter.distanceSquared(b.roomCenter) <= Math.pow(a.sizeX/2.0 + b.sizeX/2.0, 2) + Math.pow(a.sizeZ/2 + b.sizeZ/2, 2)){
//
//        }
        return a.roomCenter.distanceSquared(b.roomCenter) <= Math.pow(a.sizeX/2.0 + b.sizeX/2.0, 2) + Math.pow(a.sizeZ/2 + b.sizeZ/2, 2);
    }



    public boolean isValidRoomPlacement(DungeonRoom room){
        for(DungeonRoom otherRooms: rooms){
            if (checkRoomCollision(room, otherRooms)){
                return false;
            }
        }
        return true;
    }
}
