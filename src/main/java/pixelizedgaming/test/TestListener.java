package pixelizedgaming.test;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.RayTraceResult;
import pixelizedgaming.test.utils.RayCast;

public class TestListener implements Listener {
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e){
        Player p = e.getPlayer();

        RayTraceResult result = e.getPlayer().getWorld().rayTraceEntities(p.getEyeLocation(), p.getEyeLocation().getDirection(), 200);
        assert result != null;
        RayCast rc = RayCast.builder()
                .startLocation(p.getEyeLocation())
                .rs((loc) ->{
                    new ParticleBuilder(Particle.END_ROD).location(loc).count(10).offset(5,5,5).receivers(p);
                })
                .build();
        rc.castUntilHitBlock();
    }
}
