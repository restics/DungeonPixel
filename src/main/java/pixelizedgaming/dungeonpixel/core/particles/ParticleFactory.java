package pixelizedgaming.dungeonpixel.core.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;

public class ParticleFactory {
    private ParticleBuilder builder;
    public ParticleFactory (ParticleBuilder builder){
        this.builder = builder;
    }
    public void spawnAt(Location loc){
        builder.location(loc);
        builder.spawn();
    }

}
