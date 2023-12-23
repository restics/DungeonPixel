package pixelizedgaming.dungeonpixel.core.particles;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public abstract class AbstractParticle extends BukkitRunnable implements IParticle{
    int duration;
    Location spawn;
    Vector dir;

    public AbstractParticle(int duration, Location spawn, Vector dir) {
        this.duration = duration;
        this.spawn = spawn;
        this.dir = dir;
    }

    @Override
    public void run(){
        doParticle();
    }
}
