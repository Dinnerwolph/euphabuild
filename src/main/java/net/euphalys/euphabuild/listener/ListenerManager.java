package net.euphalys.euphabuild.listener;

import com.sk89q.worldedit.WorldEdit;
import net.euphalys.euphabuild.EuphaBuild;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * @author Dinnerwolph
 */
public class ListenerManager {

    public ListenerManager() {
        init(EuphaBuild.getInstance());
    }

    private void init(Plugin plugin) {
        PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new Block(), plugin);
        WorldEdit.getInstance().getEventBus().register(new WorldEditListener());
    }
}
