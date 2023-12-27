package pixelizedgaming.dungeonpixel.core.dungeons;

import org.bukkit.Color;
import org.bukkit.Material;

//for testing purposes
public enum RoomType {
    CORRIDOR(5,5,5, Material.WHITE_WOOL),
    START(10,10,10, Material.BLACK_WOOL),
    BOSS(30,20,30, Material.RED_WOOL),
    CHEST(15,10,15, Material.PINK_WOOL),
    FIGHT(20,10,20, Material.CYAN_WOOL),
    FIGHT2(15,10,30, Material.GREEN_WOOL),
    FIGHT3(20,10,20, Material.LIME_WOOL),
    FIGHT4(20,10,20, Material.LIGHT_BLUE_WOOL),
    PUZZLE(10,10,30, Material.ORANGE_WOOL),
    SHOP(15,10,15, Material.YELLOW_WOOL);


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
