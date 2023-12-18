package pixelizedgaming.test.utils;

import org.bukkit.Location;

import org.bukkit.util.Vector;

public class RayCast {
    private Location startLocation;
    private Vector dir;
    private double length;
    private int step;
    private RayStepFI rs;



    public Location castUntilHitBlock(){
        Vector uLookVector = dir.normalize().divide(new Vector(step,step,step));
        for(int i = 0; (i < length * step); i++){
            startLocation.add(uLookVector);
            if (startLocation.getBlock().getType().isSolid()){
                rs.rayStep();
                return startLocation;
            }
        }
        return null;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private Location startLocation;
        private Vector dir;
        private double length;
        private int step;
        private RayStepFI rs;

        public Builder startLocation(Location sl){
            this.startLocation = sl;
            return this;
        }

        public Builder dir(Vector dir){
            this.dir = dir;
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

        public RayCast build(){
            return new RayCast(this);
        }

        protected Builder(){
            step = 6;
            length = 100;
        }
    }
    protected RayCast(Builder builder){
        startLocation = builder.startLocation;
        dir = builder.dir;
        length = builder.length;
        step = builder.step;
        rs = builder.rs;
     }
}
