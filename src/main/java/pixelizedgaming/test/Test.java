package pixelizedgaming.test;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pixelizedgaming.test.particles.FunParticle1;

public final class Test extends JavaPlugin {
    private static Test instance;

    public static Test getInstance(){
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
                BukkitTask task = new FunParticle1(this, Integer.parseInt(args[0]), pSender).runTask(this);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }

            return true;
        }
        return true;
    }

}
