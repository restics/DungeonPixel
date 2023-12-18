package pixelizedgaming.test;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pixelizedgaming.test.utils.RayCast;

public class TestListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        RayCast rc = RayCast.builder()
                .startLocation(p.getEyeLocation())
                .
        
    }
}
