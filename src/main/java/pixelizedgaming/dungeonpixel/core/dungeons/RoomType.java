package pixelizedgaming.dungeonpixel.core.dungeons;

import org.bukkit.Color;
import org.bukkit.Material;

//for testing purposes
public enum RoomType {
    CORRIDOR(7,5,7, Material.WHITE_WOOL),
    CHEST(14,10,14, Material.GREEN_WOOL),
    FIGHT(20,10,20, Material.CYAN_WOOL),
    FIGHT2(15,10,30, Material.CYAN_WOOL),
    BOSS(30,20,30, Material.CYAN_WOOL),
    PUZZLE(10,10,30, Material.ORANGE_WOOL),
    SHOP(14,10,14, Material.YELLOW_WOOL);


    public final int x;
    public final int y;
    public final int z;
    public final Material mat;
    RoomType(int x, int y, int z, Material mat) {
        this.x = x;
        this.y = y;
        this.z = x;
        this.mat = mat;
    }
}
