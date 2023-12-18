package pixelizedgaming.test.particles;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pixelizedgaming.test.Test;

public abstract class AbstractParticle extends BukkitRunnable implements IParticle{
    int duration;
    Player player;
    Test plugin;
    @Override
    public void run(){
        doParticle();
    }
}
