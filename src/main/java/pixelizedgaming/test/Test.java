package pixelizedgaming.test;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pixelizedgaming.test.particles.FunParticle1;

public final class Test extends JavaPlugin {

    @Override
    public void onEnable() {
        // load API and store it for later use
        getLogger().info("Hiya! its a me marrios!");
        getServer().getPluginManager().registerEvents(new TestListener(), this);
        // or get it from running plugin api instance
        // check if everything is fine with it

        // if (ParticelNativePlugin.isValid()) {
        //     particleApi = ParticleNativePlugin.getAPI();
        // }
        // else {
        //     getLogger().log(Level.SEVERE, "Error occurred while loading dependency.");
        //     this.setEnabled(false);
        //     return;
        // }
    }

    // example usage
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("gaming")){

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be player to use this command!");
                return true;
            }

            Player pSender = (Player) sender;
            if (args.length != 1){
                sender.sendMessage("Usage: /gaming <duration>");
                return true;
            }

            Location loc = pSender.getLocation();

//            // particle spawning
//            particleApi.LIST_1_8.FLAME             // select particle from list
//                    .packet(true, loc)             // create particle packet
//                    .sendInRadiusTo(pSender, 30D);  // send it to player if in 30 block radius
            try {
                BukkitTask task = new FunParticle1(this, Integer.parseInt(args[0]), pSender).runTaskLater(this, 20);
            }catch(NumberFormatException e){
                e.printStackTrace();
            }

            return true;
        }
        return true;
    }

}
