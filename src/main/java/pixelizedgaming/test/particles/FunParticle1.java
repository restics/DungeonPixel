package pixelizedgaming.test.particles;

import org.bukkit.entity.Player;
import pixelizedgaming.test.Test;

public class FunParticle1 extends AbstractParticle{

    public FunParticle1(Test plugin, int millisDuration, Player p) {
        this.plugin = plugin;
        duration = millisDuration;
        player = p;
    }

    @Override
    public void doParticle() {

    }

    @Override
    public void run() {
        while(duration >= 1000){
            try{
                Thread.sleep(1000);

                player.sendMessage("Countdown... " + duration/1000);
                duration-= 1000;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        try{
            Thread.sleep(duration % 1000);
        }catch(Exception e){
            e.printStackTrace();
        }

        player.sendMessage("Heheheha");
        super.run();
    }
}
