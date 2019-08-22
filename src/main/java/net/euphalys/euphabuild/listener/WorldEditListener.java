package net.euphalys.euphabuild.listener;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.logging.AbstractLoggingExtent;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.utils.Pair;
import net.euphalys.euphabuild.EuphaBuild;
import net.euphalys.euphabuild.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class WorldEditListener {

    @Subscribe
    public void wrapForLogging(EditSessionEvent event) {
        Actor actor = event.getActor();
        if (actor != null && actor.isPlayer()) {
            event.setExtent(new MyLogger(actor, event.getExtent()));
        }
    }


    private static class MyLogger extends AbstractLoggingExtent {
        private final Actor actor;

        private MyLogger(Actor actor, Extent extent) {
            super(extent);
            this.actor = actor;
        }

        @Override
        protected void onBlockChange(Vector position, BaseBlock newBlock) {
            if (EuphalysApi.getInstance().getPlayer(actor.getName()).hasPermission("euphalys.build.we")) return;
            Player p = Bukkit.getServer().getPlayer(actor.getName());
            BaseBlock oldBlock = getBlock(position);
            Pair<Location, Location> plot = EuphaBuild.getInstance().getPlotMap().get(EuphalysApi.getInstance().getPlayer(p.getUniqueId()).getPlotsId());
            if (plot == null) {
                newBlock.setType(oldBlock.getType());
                return;
            }
            Location location = new Location(p.getWorld(), position.getX(), position.getY(), position.getZ());
            if (!new Cuboid(plot.getKey(), plot.getValue()).hasBlockInside(location.getBlock())) {
                //p.sendMessage("Vous n'avez pas le droit de placer de bloc en dehors de votre zone.");
                newBlock.setType(oldBlock.getType());
            }

        }

    }
}
