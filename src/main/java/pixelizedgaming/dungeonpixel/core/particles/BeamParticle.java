package pixelizedgaming.dungeonpixel.core.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import pixelizedgaming.dungeonpixel.util.RayCast;

public class BeamParticle extends AbstractParticle{
    int len;

    ParticleBuilder spawnOnStep;
    ParticleBuilder spawnOnFinish;


    //2 particlebuilders don't need to be fully built
    public BeamParticle(int duration, Location from, Location to, ParticleBuilder spawnOnStep, ParticleBuilder spawnOnFinish) {
        super(duration, from, to.subtract(from).getDirection());
        spawn.setDirection(dir);
        this.spawnOnStep = spawnOnStep;
        this.spawnOnFinish = spawnOnFinish;
    }

    @Override
    public void doParticle() {
        RayCast rc = RayCast.builder()
                .startLocation(spawn)
                .rs((loc, step)->{
                    spawnOnStep.location(loc);
                    spawnOnStep.spawn();

                })
                .onFinish((rayCastResult)->{
                    spawnOnFinish.location(rayCastResult.getEndLocation());
                    spawnOnFinish.spawn();
                })
                .build();
        rc.castUntilHitBlock();
    }
}
