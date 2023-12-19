package pixelizedgaming.test;

import com.destroystokyo.paper.ParticleBuilder;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;
import pixelizedgaming.test.utils.RayCast;

import java.util.Objects;

public class TestListener implements Listener {
    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e){
        if (!Objects.requireNonNull(e.getItem()).getType().equals(Material.STICK)) return;

        Player p = e.getPlayer();
        Audience players =  p;
        players.sendMessage(() -> Component
                .text("Magic Missile!")
                .color(TextColor.color(245, 129, 66)));

        //players.playSound(Sound.sound(Key.key("entity.blaze.shoot"), Sound.Source.PLAYER, 1f, 1f));

        RayCast.Builder builder = RayCast.builder();
        RayCast rc = builder.startLocation(p.getEyeLocation())
                .rs((loc, i) -> {

            new ParticleBuilder(Particle.END_ROD)
                    .location(loc)
                    .extra(0)
                    .count(2)
                    .offset(0,0,0)
                    .allPlayers()
                    .spawn();

        }).onFinish((result) -> {
            Location loc = result.getEndLocation();
            result.getPassedEntities().forEach((ent)->{
                ent.damage(10);
            });
            players.playSound(Sound
                            .sound()
                            .type(Key.key("entity.firework_rocket.large_blast_far"))
                            .source(Sound.Source.BLOCK)
                            .volume(5f)
                            .build()
            ,loc.x(),loc.y(),loc.z());

            new ParticleBuilder(Particle.EXPLOSION_LARGE)
                    .location(loc)
                    .count(20)
                    .offset(3, 3, 3)
                    .allPlayers()
                    .spawn();
        }).build();

        BukkitTask task = rc.runTask(Test.getInstance());

    }
}
