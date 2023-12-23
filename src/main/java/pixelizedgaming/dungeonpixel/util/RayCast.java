package pixelizedgaming.dungeonpixel.util;

import org.bukkit.Location;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public class RayCast extends BukkitRunnable {
    private Location startLocation;
    private double length;
    private double step; // # of steps per block traveled
    private double width; // width of raycast line
    private boolean isHoming;
    private double homingWidth;
    private RayStepFI rs;
    private Consumer<RayCastResult> onFinish;

    //returns location of block hit
    //homing turns the vector to target entities
    public RayCastResult castUntilHitBlock(){
        RayCastResult result = new RayCastResult();
        Vector uLookVector = startLocation.getDirection().normalize().divide(new Vector(step,step,step));
        World world = startLocation.getWorld();

        LivingEntity homingTarget = null;

        for(int i = 0; (i < length * step); i++){
            startLocation.add(uLookVector);
            rs.rayStep(startLocation, i / step);

            for(Entity ent : world.getNearbyEntities(startLocation, width, width, width)){

                result.passedEntities.add((LivingEntity) ent);
                homingTarget = null; //warnings system is retarded this only works on contact
            }

            if (isHoming){
                if (homingTarget == null) {
                    double shortestDist = 100000;
                    for(Entity e : world.getNearbyEntities(startLocation, homingWidth, homingWidth, homingWidth)){
                        if (!(e instanceof LivingEntity)) continue;
                        if (e instanceof Player) continue; // as if we need pvp right hahahahaha
                        if (result.getPassedEntities().contains((LivingEntity) e)) continue;

                        Vector endVector = e.getLocation().toVector();
                        Vector newDirection = endVector.subtract(startLocation.toVector());
                        if (endVector.distance(newDirection) < shortestDist) {
                            homingTarget = (LivingEntity) e;
                            shortestDist = endVector.distance(newDirection);
                        }
                    }
                    if (homingTarget != null) {
                        Vector endVector = homingTarget.getEyeLocation().toVector();
                        Vector newDirection = endVector.subtract(startLocation.toVector());
                        uLookVector = newDirection.normalize().divide(new Vector(step, step, step));
                    }
                }

            }

            if (startLocation.getBlock().getType().isSolid()){
                // ends here anyways
                result.endLocation = startLocation;
                return result;
            }
        }
        result.endLocation = startLocation;
        return result;
    }


    public RayCastResult castUntilHitEntity(){
        RayCastResult result = new RayCastResult();
        Vector uLookVector = startLocation.getDirection().normalize().divide(new Vector(step,step,step));
        World world = startLocation.getWorld();

        LivingEntity homingTarget = null;

        for(int i = 0; (i < length * step); i++){
            startLocation.add(uLookVector);
            rs.rayStep(startLocation, i / step);

            for(Entity ent : world.getNearbyEntities(startLocation, width, width, width)){

                result.endLocation = ent.getLocation();
                homingTarget = null;
            }

            if (isHoming){
                if (homingTarget == null) {
                    double shortestDist = 100000;
                    for(Entity e : world.getNearbyEntities(startLocation, homingWidth, homingWidth, homingWidth)){
                        if (!(e instanceof LivingEntity)) continue;
                        if (e instanceof Player) continue; // as if we need pvp right hahahahaha
                        if (result.getPassedEntities().contains((LivingEntity) e)) continue;

                        Vector endVector = e.getLocation().toVector();
                        Vector newDirection = endVector.subtract(startLocation.toVector());
                        if (endVector.distance(newDirection) < shortestDist) {
                            homingTarget = (LivingEntity) e;
                            shortestDist = endVector.distance(newDirection);
                        }
                    }
                    if (homingTarget != null) {
                        Vector endVector = homingTarget.getEyeLocation().toVector();
                        Vector newDirection = endVector.subtract(startLocation.toVector());
                        uLookVector = newDirection.normalize().divide(new Vector(step, step, step));
                    }
                }

            }

            if (startLocation.getBlock().getType().isSolid()){
                // ends here anyways
                result.endLocation = startLocation;
                return result;
            }
        }
        result.endLocation = startLocation;
        return result;
    }


    public static Builder builder(){
        return new Builder();
    }

    //TODO: Implement futures so no need for onFinish call
    @Override
    public void run() {
        onFinish.accept(castUntilHitBlock());
    }

    public static class Builder{
        private Location startLocation;

        private double length;
        private int step;
        private double width; // width of raycast line
        private boolean isHoming;
        private double homingWidth;
        private RayStepFI rs;
        private Consumer<RayCastResult> onFinish;


        public Builder startLocation(Location sl){
            this.startLocation = sl;
            return this;
        }

        public Builder isHoming(boolean b){
            this.isHoming = b;
            return this;
        }

        public Builder length(double len){
            this.length = len;
            return this;
        }

        public Builder step(double len){
            this.length = len;
            return this;
        }

        public Builder radius(double width){
            this.width = width;
            return this;
        }

        public Builder homingWidth(double radius){
            this.homingWidth = radius;
            return this;
        }

        public Builder rs(RayStepFI rs){
            this.rs = rs;
            return this;
        }

        public Builder onFinish(Consumer<RayCastResult>  rs){
            this.onFinish = rs;
            return this;
        }

        public RayCast build(){
            return new RayCast(this);
        }

        protected Builder(){
            step = 6; // adjust maybe
            length = 100;
            width = 0.5;
            homingWidth = 10;
        }
    }
    protected RayCast(Builder builder){
        startLocation = builder.startLocation;
        length = builder.length;
        step = builder.step;
        rs = builder.rs;
        onFinish = builder.onFinish;
        isHoming = builder.isHoming;
        homingWidth = builder.homingWidth;
        width = builder.width;
     }
}
