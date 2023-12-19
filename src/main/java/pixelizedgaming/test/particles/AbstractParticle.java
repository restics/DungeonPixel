package pixelizedgaming.test.particles;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pixelizedgaming.test.Test;

public abstract class AbstractParticle extends BukkitRunnable implements IParticle{
    int duration;
    Location spawn;
    Vector dir;
    Test plugin;
    @Override
    public void run(){
        doParticle();
    }
}
