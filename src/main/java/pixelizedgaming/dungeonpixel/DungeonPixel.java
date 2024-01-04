package pixelizedgaming.dungeonpixel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import pixelizedgaming.dungeonpixel.core.dungeons.Dungeon;
import pixelizedgaming.dungeonpixel.core.dungeons.DungeonGenerator;
import pixelizedgaming.dungeonpixel.core.particles.FunParticle1;
import pixelizedgaming.dungeonpixel.util.RayCast;
import pixelizedgaming.dungeonpixel.util.RayCastResult;

import java.util.Objects;

public final class DungeonPixel extends JavaPlugin {
    private static DungeonPixel instance;

    public static DungeonPixel getInstance(){
        if (instance == null){
            throw (new IllegalStateException("Attempting to access singleton before initialization"));
        }
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Hiya! its a me marrios!");
        getServer().getPluginManager().registerEvents(new TestListener(), this);
    }

    // example usage
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("gaming")){

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be player to use this command!");
                return true;
            }

            if (args.length != 1){
                sender.sendMessage("Usage: /gaming <duration>");
                return false;
            }
            Player pSender = (Player) sender;

            try {
                BukkitTask task = new FunParticle1(Integer.parseInt(args[0]), pSender).runTaskTimer(this, 0, 20);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("generatemap")){
            sender.sendMessage("wawaawsdasdwa");
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be player to use this command!");
                return true;
            }
            Player pSender = (Player) sender;

            RayCast rc = RayCast.builder()
                    .length(200)
                    .startLocation(pSender.getEyeLocation())
                    .build();

            RayCastResult result = rc.castUntilHitBlock();

            long beforeTime = System.currentTimeMillis();
            int generationSize = 150;
            DungeonGenerator.buildFrame(result.getEndLocation(), generationSize, 100, generationSize, Material.GRAY_WOOL);
            DungeonGenerator generator = new DungeonGenerator();
            generator.generateMap(result.getEndLocation(), generationSize);
            Dungeon dungeon = generator.generateDungeon();
            long afterTime = System.currentTimeMillis();
            long elapsed = afterTime - beforeTime;
            pSender.sendMessage("Dungeon of size " + generationSize + " generated | Room count: " + dungeon.getRooms().length + " | Time Elapsed: " + elapsed + "ms");
            return true;
        }
        return true;
    }

}
