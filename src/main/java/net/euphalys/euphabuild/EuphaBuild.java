package net.euphalys.euphabuild;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.utils.Pair;
import net.euphalys.euphabuild.commands.PlotsCommands;
import net.euphalys.euphabuild.data.PlotsManager;
import net.euphalys.euphabuild.listener.ListenerManager;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */
public class EuphaBuild extends JavaPlugin {

    private static EuphaBuild instance;
    public final Map<Integer, Pair<Location, Location>> plotMap = new ConcurrentHashMap<>();
    private PlotsManager manager;

    @Override
    public void onEnable() {
        instance = this;
        new ListenerManager();
        this.manager = new PlotsManager(EuphalysApi.getInstance());
        this.manager.getAllPlots();
        this.getCommand("plots").setExecutor(new PlotsCommands());
        this.getCommand("plots").setPermission("euphalys.cmd.plots");
    }

    public static EuphaBuild getInstance() {
        return instance;
    }

    public Map<Integer, Pair<Location, Location>> getPlotMap() {
        return plotMap;
    }

    public PlotsManager getManager() {
        return manager;
    }
}
