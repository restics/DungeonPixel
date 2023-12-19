package pixelizedgaming.test.utils;

import org.bukkit.Location;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public class RayCast extends BukkitRunnable {
    private Location startLocation;
    private double length;
    private double step; // # of steps per block traveled
    private boolean isHoming;
    private RayStepFI rs;
    private Consumer<RayCastResult> onFinish;

    //returns location of block hit
    public RayCastResult castUntilHitBlock(){
        RayCastResult result = new RayCastResult();
        Vector uLookVector = startLocation.getDirection().normalize().divide(new Vector(step,step,step));
        World world = startLocation.getWorld();
        System.out.println(uLookVector);
        for(int i = 0; (i < length * step); i++){
            startLocation.add(uLookVector);
            rs.rayStep(startLocation, i / step);
            world.getNearbyEntities(startLocation, 0.1, 0.1, 0.1).forEach((entity)->
            {
                result.passedEntities.add((LivingEntity) entity); // might not work
            });

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

    @Override
    public void run() {
        onFinish.accept(castUntilHitBlock());
    }

    public static class Builder{
        private Location startLocation;
        private double length;
        private int step;
        private boolean isHoming;
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
        }
    }
    protected RayCast(Builder builder){
        startLocation = builder.startLocation;
        length = builder.length;
        step = builder.step;
        rs = builder.rs;
        onFinish = builder.onFinish;
        isHoming = builder.isHoming;
     }
}
