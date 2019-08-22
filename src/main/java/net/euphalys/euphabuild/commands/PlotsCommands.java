package net.euphalys.euphabuild.commands;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import net.euphalys.core.api.utils.Pair;
import net.euphalys.euphabuild.EuphaBuild;
import net.euphalys.euphabuild.utils.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Dinnerwolph
 */
public class PlotsCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("/plots add <x> <z> <x> <z>");
            commandSender.sendMessage("/plots get");
            commandSender.sendMessage("/plots set <player> <plotId>");
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length < 5) {
                commandSender.sendMessage("/plots add <x> <z> <x> <z>");
            } else {
                Location loc1 = new Location(Bukkit.getWorld("tests2"), Double.parseDouble(args[1]), 0, Double.parseDouble(args[2]));
                Location loc2 = new Location(Bukkit.getWorld("tests2"), Double.parseDouble(args[3]), 255, Double.parseDouble(args[4]));

                EuphaBuild.getInstance().getManager().registerPlots(loc1, loc2);
            }
        } else if (args[0].equalsIgnoreCase("get")) {
            for (Integer integer : EuphaBuild.getInstance().plotMap.keySet()) {
                Pair<Location, Location> plots = EuphaBuild.getInstance().getPlotMap().get(integer);
                if (new Cuboid(plots.getKey(), plots.getValue()).hasPlayerInside((Player) commandSender)) {
                    commandSender.sendMessage("Plots n°" + integer);
                }
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 3) {
                commandSender.sendMessage("/plots set <player> <plotId>");
            } else {
                IEuphalysPlayer player = EuphalysApi.getInstance().getPlayer(args[1]);
                player.setPlotsId(Integer.parseInt(args[2]));
                commandSender.sendMessage("Vous venez d'atribuer le plot n° " + args[2] + " au joueurs " + args[1]);
            }
        }

        return true;
    }
}
