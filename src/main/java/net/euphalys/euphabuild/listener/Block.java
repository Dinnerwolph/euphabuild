package net.euphalys.euphabuild.listener;

import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.utils.Pair;
import net.euphalys.euphabuild.EuphaBuild;
import net.euphalys.euphabuild.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dinnerwolph
 */
public class Block implements Listener {

    private final Map<UUID, Long> placeMap = new ConcurrentHashMap<>();
    private final Map<UUID, Long> breakMap = new ConcurrentHashMap<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(EuphalysApi.getInstance().getPlayer(event.getPlayer().getUniqueId()).hasPermission("euphalys.build.place")) return;
        UUID uuid = event.getPlayer().getUniqueId();
        Pair<Location, Location> plot = EuphaBuild.getInstance().getPlotMap().get(EuphalysApi.getInstance().getPlayer(uuid).getPlotsId());
        if (plot == null) {
            event.setCancelled(true);
            if (!placeMap.containsKey(uuid)) {
                placeMap.put(uuid, System.currentTimeMillis());
                event.getPlayer().sendMessage("Vous n'avez pas le droit de placer de bloc en dehors de votre zone.");
            } else {
                long time = placeMap.get(uuid);
                if (time + 10000 < System.currentTimeMillis()) {
                    placeMap.put(uuid, System.currentTimeMillis());
                    event.getPlayer().sendMessage("Vous n'avez pas le droit de placer de bloc en dehors de votre zone.");
                }
            }
            return;
        }
        if (!new Cuboid(plot.getKey(), plot.getValue()).hasBlockInside(event.getBlock())) {
            event.setCancelled(true);
            if (!placeMap.containsKey(uuid)) {
                placeMap.put(uuid, System.currentTimeMillis());
                event.getPlayer().sendMessage("Vous n'avez pas le droit de placer de bloc en dehors de votre zone.");
            } else {
                long time = placeMap.get(uuid);
                if (time + 10000 < System.currentTimeMillis()) {
                    placeMap.put(uuid, System.currentTimeMillis());
                    event.getPlayer().sendMessage("Vous n'avez pas le droit de placer de bloc en dehors de votre zone.");
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(EuphalysApi.getInstance().getPlayer(event.getPlayer().getUniqueId()).hasPermission("euphalys.build.break")) return;
        UUID uuid = event.getPlayer().getUniqueId();
        Pair<Location, Location> plot = EuphaBuild.getInstance().getPlotMap().get(EuphalysApi.getInstance().getPlayer(uuid).getPlotsId());

        if(plot == null) {
            event.setCancelled(true);
            if (!breakMap.containsKey(uuid)) {
                breakMap.put(uuid, System.currentTimeMillis());
                event.getPlayer().sendMessage("Vous n'avez pas le droit de casser de bloc en dehors de votre zone.");
            } else {
                long time = breakMap.get(uuid);
                if (time + 10000 < System.currentTimeMillis()) {
                    breakMap.put(uuid, System.currentTimeMillis());
                    event.getPlayer().sendMessage("Vous n'avez pas le droit de casser de bloc en dehors de votre zone.");
                }
            }
            return;
        }

        if (!new Cuboid(plot.getKey(), plot.getValue()).hasBlockInside(event.getBlock())) {
            event.setCancelled(true);
            if (!breakMap.containsKey(uuid)) {
                breakMap.put(uuid, System.currentTimeMillis());
                event.getPlayer().sendMessage("Vous n'avez pas le droit de casser de bloc en dehors de votre zone.");
            } else {
                long time = breakMap.get(uuid);
                if (time + 10000 < System.currentTimeMillis()) {
                    breakMap.put(uuid, System.currentTimeMillis());
                    event.getPlayer().sendMessage("Vous n'avez pas le droit de casser de bloc en dehors de votre zone.");
                }
            }
        }
    }

}
