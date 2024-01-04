package pixelizedgaming.dungeonpixel.core.particles;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import pixelizedgaming.dungeonpixel.util.RayCast;

public class BeamParticle extends AbstractParticle{
    int len;

    ParticleBuilder spawnOnStep;
    ParticleBuilder spawnOnFinish;
    RayCast rc;

    //2 particlebuilders don't need to be fully built
    public BeamParticle(int duration, Location from, Location to, ParticleBuilder spawnOnStep, ParticleBuilder spawnOnFinish) {
        super(duration, from, to.clone().subtract(from).toVector());
        spawn.setDirection(to.clone().subtract(from).toVector());
        System.out.println(spawn);
        this.spawnOnStep = spawnOnStep;
        this.spawnOnFinish = spawnOnFinish;
        rc = RayCast.builder()
                .startLocation(spawn)
                .step(3)
                .rs((loc, step)->{
                    spawnOnStep.location(loc);
                    spawnOnStep.spawn();

                })
                .length(dir.length())
                .onFinish((rayCastResult)->{
                    if (spawnOnFinish != null) {
                        spawnOnFinish.location(rayCastResult.getEndLocation());
                        spawnOnFinish.spawn();
                    }
                })
                .build();
    }

    @Override
    public void doParticle() {
        rc.castUntilHitEntity();
    }
}
