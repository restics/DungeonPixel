package pixelizedgaming.test.particles;

import org.bukkit.Location;

public class BeamParticle extends AbstractParticle{
    @Override
    public void doParticle() {
        Location l = player.getEyeLocation();

    }
}
