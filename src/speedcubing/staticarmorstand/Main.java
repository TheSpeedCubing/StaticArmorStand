package speedcubing.staticarmorstand;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {
    List<EntityArmorStand> s = new ArrayList<>();

    public void onEnable() {
        Bukkit.getPluginCommand("staticarmorstand").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        add();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        PlayerConnection connection = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection;
        for (EntityArmorStand a : s) {
            connection.sendPacket(new PacketPlayOutSpawnEntityLiving(a));
        }
    }

    void add() {
        for (String path : getConfig().getConfigurationSection("").getKeys(false)) {
            WorldServer world = ((CraftWorld) Bukkit.getWorld("world")).getHandle();
            EntityArmorStand dg = new EntityArmorStand(world);
            dg.setCustomNameVisible(true);
            dg.setGravity(true);
            dg.setInvisible(true);
            dg.n(true);
            dg.setCustomName(ChatColor.translateAlternateColorCodes('&', path));
            dg.setLocation(getConfig().getDouble(path + ".x") + 0.5, getConfig().getDouble(path + ".y"), getConfig().getDouble(path + ".z") + 0.5, 0, 0);
            s.add(dg);
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender || sender.hasPermission("staticarmorstand.use")) {
            s.clear();
            reloadConfig();
            add();
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
