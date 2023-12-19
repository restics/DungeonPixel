package pixelizedgaming.test.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import pixelizedgaming.test.Test;
import java.util.Objects;

public class FunParticle1 extends AbstractParticle{

    Player p;
    public FunParticle1(Test plugin, int millisDuration, Player p) {
        this.plugin = plugin;
        duration = millisDuration;
        dir = p.getEyeLocation().getDirection();
        spawn = Objects.requireNonNull(p.getWorld().rayTraceBlocks(p.getEyeLocation(), dir, 100)).getHitPosition().toLocation(p.getWorld());
        this.p = p;
    }

    @Override
    public void doParticle() {
        while(duration >= 1000){
            try{
                Thread.sleep(1000);
                new ParticleBuilder(Particle.TOTEM)
                        .location(spawn)
                        .count(10)
                        .allPlayers()
                        .spawn();
                p.sendMessage("Countdown... " + duration/1000);

                duration-= 1000;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        try{
            Thread.sleep(duration % 1000);
        }catch(Exception e){
            e.printStackTrace();
        }

        p.sendMessage("Heheheha");
    }

}
