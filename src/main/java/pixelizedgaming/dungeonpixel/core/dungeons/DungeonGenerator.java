package pixelizedgaming.dungeonpixel.core.dungeons;

import com.destroystokyo.paper.ParticleBuilder;
import com.google.common.graph.EndpointPair;
import com.google.common.graph.MutableGraph;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import pixelizedgaming.dungeonpixel.DungeonPixel;
import pixelizedgaming.dungeonpixel.core.particles.BeamParticle;
import pixelizedgaming.dungeonpixel.util.math.GraphUtils;
import pixelizedgaming.dungeonpixel.util.math.Triangle;

import java.util.*;

//dungeon factory class, also includes map building methods
public class DungeonGenerator extends BukkitRunnable {
    //add settings here
    Dungeon dungeonInstance;
    Random rand = new Random();
    private Map<Vector, DungeonRoom> locToRoom;
    ArrayList<DungeonRoom> rooms;

    public DungeonGenerator(){
        dungeonInstance = new Dungeon();
        locToRoom = new HashMap<>();
        rooms = new ArrayList<>();
    }

    public Dungeon generateDungeon(){
        return dungeonInstance;
    }
    // location = center of perimeter, size = side length of perimeter square
    public void generateMap(Location dungeonLocation, int size){
        dungeonInstance.centralLocation = dungeonLocation;
        constructRooms(size/5 + rand.nextInt(size/10), size, size);
        dungeonInstance.rooms = rooms.toArray(new DungeonRoom[0]);
    }

    // returns a list of randomly generated rooms within the bounds
    // TODO: snap the rooms to a grid based on the size of the corridors (so we can avoid clipping problems)
    public void  constructRooms(int count, int sizeX, int sizeZ){

        Location center = dungeonInstance.centralLocation;
        Location topRight = center.clone().add(sizeX/2.0, 0, sizeZ/2.0);
        Location bottomleft = center.clone().subtract(sizeX/2.0, 0, sizeZ/2.0);

        // Insert start in a quadrant
        int boundX = RoomType.CORRIDOR.x * ((sizeX / 2) / RoomType.CORRIDOR.x) - RoomType.BOSS.x;
        int boundZ = RoomType.CORRIDOR.z * ((sizeZ / 2) / RoomType.CORRIDOR.z) - RoomType.BOSS.z;

        int randPosX = topRight.getBlockX() - RoomType.START.x/2 - rand.nextInt(boundX);
        int randPosZ = topRight.getBlockZ() - RoomType.START.z/2 - rand.nextInt(boundZ);
        DungeonRoom startRoom = new DungeonRoom(RoomType.START, new Location(center.getWorld(), randPosX, center.y(), randPosZ));
        startRoom.placeSchematic();
        rooms.add(startRoom);

        // Insert boss in quadrant diagonal to start quadrant
        randPosX = bottomleft.getBlockX() + RoomType.BOSS.x/3 + rand.nextInt(boundX);
        randPosZ = bottomleft.getBlockZ() + RoomType.BOSS.z/3 + rand.nextInt(boundZ);
        DungeonRoom bossRoom = new DungeonRoom(RoomType.BOSS, new Location(center.getWorld(), randPosX, center.y(), randPosZ));
        bossRoom.placeSchematic();
        rooms.add(bossRoom);

        // place other rooms
        for(int i = 0; i < count; i++){
            RoomType type = RoomType.values()[rand.nextInt(RoomType.values().length - 3) + 3]; // we don't want to place corridors, start, or boss here
            DungeonRoom room = new DungeonRoom(type, new Location(center.getWorld(), 0,0,0));
            int tries = 20; // # of attempts to place a room
            do{
                boundX = RoomType.CORRIDOR.x * ((sizeX) / RoomType.CORRIDOR.x)  - RoomType.BOSS.x;
                boundZ = RoomType.CORRIDOR.z * ((sizeZ) / RoomType.CORRIDOR.z)  - RoomType.BOSS.z;
                randPosX = bottomleft.getBlockX() + room.sizeX/2 + rand.nextInt(sizeX - room.sizeX);
                randPosZ = bottomleft.getBlockZ() + room.sizeZ/2 + rand.nextInt(sizeZ - room.sizeZ);
                room.roomCenter = new Location(center.getWorld(), randPosX, center.y(), randPosZ);
                tries--;
            }while ((!isValidRoomPlacement(room) && tries > 0));
            if (tries > 0){

                rooms.add(room);
            }
        }

        constructHallways();
        // place after hallways have been placed
        for(DungeonRoom room: rooms){
            room.placeSchematic();
        }
    }

    // we did the triangulation thing specifically so i can make good hallways
    public void constructHallways(){
        List<Vector> vectorsList= new ArrayList<>();
        for(DungeonRoom room: rooms){
            vectorsList.add(room.roomCenter.toVector());
            locToRoom.put(room.roomCenter.toVector(), room);
        }
        MutableGraph<Vector> fullGraph = GraphUtils.trianglesToLocGraph(GraphUtils.graphFromPoints(vectorsList));
        MutableGraph<Vector> mst = GraphUtils.findMST(fullGraph);
        MutableGraph<Vector> finishedGraph = GraphUtils.addLoops(fullGraph, mst);
        World world = dungeonInstance.centralLocation.getWorld();
        for(EndpointPair<Vector> edge: finishedGraph.edges()){
            System.out.println("Graph edge drawn from " + edge.nodeU() + " to " + edge.nodeV());
            new BeamParticle(100, edge.nodeU().toLocation(world), edge.nodeV().toLocation(world),
                    new ParticleBuilder(Particle.VILLAGER_HAPPY).allPlayers().count(1).extra(0.1).offset(0,0,0).force(true),
                    null
            ).runTaskTimer(DungeonPixel.getInstance(), 0, 5);
        }

    }

    public void onFinish(){

    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

    }

    public static void buildFrame(Location center, int sizeX, int sizeY, int sizeZ, Material material){

        Location bottomCorner = center.clone().subtract(sizeX/2.0, 0, sizeZ/2.0); //
        for(int i = 0; i <= sizeX; i++){
            for(int j = 0; j <= sizeY; j++){
                for(int k = 0; k <= sizeZ; k++){
                    Location currLocation = bottomCorner.clone().add(i,j,k);
                    currLocation.getBlock().setType(Material.AIR);
                }
            }
        }
        for(int i = 0; i <= sizeX; i++){

            bottomCorner.clone().add(i,0,0).getBlock().setType(material);
            bottomCorner.clone().add(i,sizeY,0).getBlock().setType(material);
            bottomCorner.clone().add(i,0,sizeZ).getBlock().setType(material);
            bottomCorner.clone().add(i,sizeY,sizeZ).getBlock().setType(material);
        }
        for(int j = 0; j <= sizeY; j++){
            bottomCorner.clone().add(0,j,0).getBlock().setType(material);
            bottomCorner.clone().add(sizeX,j,0).getBlock().setType(material);
            bottomCorner.clone().add(0,j,sizeZ).getBlock().setType(material);
            bottomCorner.clone().add(sizeX,j,sizeZ).getBlock().setType(material);
        }
        for(int k = 0; k <= sizeZ; k++){
            bottomCorner.clone().add(0,0,k).getBlock().setType(material);
            bottomCorner.clone().add(0,sizeY,k).getBlock().setType(material);
            bottomCorner.clone().add(sizeX,0,k).getBlock().setType(material);
            bottomCorner.clone().add(sizeX,sizeY,k).getBlock().setType(material);
        }
        bottomCorner.getBlock().setType(Material.GOLD_BLOCK);
        center.getBlock().setType(Material.DIAMOND_BLOCK);
    }

    // return true if a collision is found
    public boolean checkRoomCollision(DungeonRoom a, DungeonRoom b){
        //System.out.println("Checking Room Collision! Distance from room " + a + " and room " + b + " is " + a.roomCenter.distanceSquared(b.roomCenter)  );
        //System.out.println("Pythagoras check : " + Math.pow(a.sizeX/2.0 + b.sizeX/2.0, 2) + Math.pow(a.sizeZ/2.0 + b.sizeZ/2.0, 2) + "\n");
        if (a.roomCenter.distanceSquared(b.roomCenter) <= Math.pow(a.sizeX/2.0 + b.sizeX/2.0, 2) + Math.pow(a.sizeZ/2.0 + b.sizeZ/2.0, 2)){
            return true;//((a.roomCenter.getBlockX() > b.bottomLeft.getBlockX() && a.roomCenter.getBlockX() < b.topRight.getBlockX()) && (a.roomCenter.getBlockZ() > b.bottomLeft.getBlockZ() && a.roomCenter.getBlockZ() < b.topRight.getBlockZ()) );
        }


        return false;
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
