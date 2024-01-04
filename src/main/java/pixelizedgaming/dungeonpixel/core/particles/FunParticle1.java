package pixelizedgaming.dungeonpixel.core.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import java.util.Objects;

public class FunParticle1 extends AbstractParticle{

    Player p;
    public FunParticle1(int millisDuration, Player p) {
        super(millisDuration, Objects.requireNonNull(p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100)).getHitPosition().toLocation(p.getWorld()),p.getEyeLocation().getDirection());
    }

    @Override
    public void doParticle() {
        if(duration >= 1000){
            try{
                new ParticleBuilder(Particle.TOTEM)
                        .location(spawn)
                        .count(10)
                        .extra(30)
                        .offset(5,5,5)
                        .allPlayers()
                        .spawn();

                p.sendMessage("Countdown... " + duration/1000);

                duration-= 1000;
            }catch(Exception e){
                e.printStackTrace();
            }
        }else {
            p.sendMessage("Heheheha");
        }
    }

}
