package pixelizedgaming.dungeonpixel;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import pixelizedgaming.dungeonpixel.core.dungeons.DungeonGenerator;
import pixelizedgaming.dungeonpixel.core.particles.FunParticle1;

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
                return true;
            }
            Player pSender = (Player) sender;

            try {
                BukkitTask task = new FunParticle1(Integer.parseInt(args[0]), pSender).runTaskAsynchronously(this);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }
            RayTraceResult result = pSender.rayTraceBlocks(200);
            assert result != null;
            DungeonGenerator.buildFrame(Objects.requireNonNull(result.getHitBlock()).getLocation(), 5, 6, 7, Material.BLUE_WOOL);
            return true;
        }
        else if (command.getName().equalsIgnoreCase("balls")){
            sender.sendMessage("wawaawsdasdwa");
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be player to use this command!");
                return true;
            }
            Player pSender = (Player) sender;

            RayTraceResult result = pSender.rayTraceBlocks(200);
            assert result != null;
            DungeonGenerator.buildFrame(Objects.requireNonNull(result.getHitBlock()).getLocation(), 5, 6, 7, Material.BLUE_WOOL);

            return true;
        }
        return true;
    }

}
