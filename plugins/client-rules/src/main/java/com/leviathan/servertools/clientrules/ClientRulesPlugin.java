package com.leviathan.servertools.clientrules;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sends Leviathan Client rules: which client modules and HUD widgets to turn off,
 * by world. The client only ever turns things off, never saves the rules, and
 * clears them when the player leaves the server.
 *
 * Players are only messaged once their client registers {@code leviathan:rules};
 * nothing is sent to anyone else, and nothing is read from players besides the
 * protocol version on {@code leviathan:hello}.
 */
public final class ClientRulesPlugin extends JavaPlugin implements Listener, PluginMessageListener {
    public static final String RULES = "leviathan:rules";
    public static final String HELLO = "leviathan:hello";

    private volatile RuleSet defaults = RuleSet.NONE;
    private volatile Map<String, RuleSet> byWorld = new HashMap<>();
    /** Players whose client takes rules; cleaned on quit, so it can't grow without bound. */
    private final Set<UUID> clients = ConcurrentHashMap.newKeySet();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        load();
        getServer().getMessenger().registerOutgoingPluginChannel(this, RULES);
        getServer().getMessenger().registerIncomingPluginChannel(this, HELLO, this);
        getServer().getPluginManager().registerEvents(this, this);
        // After a reload, players already online keep working.
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getListeningPluginChannels().contains(RULES)) {
                clients.add(p.getUniqueId());
                send(p);
            }
        }
    }

    @Override
    public void onDisable() {
        // Lift the rules for everyone still here, then stop.
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (clients.contains(p.getUniqueId())) p.sendPluginMessage(this, RULES, RuleSet.NONE.payload());
        }
        clients.clear();
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

    private void load() {
        reloadConfig();
        List<String> unknown = new ArrayList<>();
        defaults = RuleSet.of(getConfig().getList("default"), unknown);
        Map<String, RuleSet> worlds = new HashMap<>();
        ConfigurationSection section = getConfig().getConfigurationSection("worlds");
        if (section != null) {
            for (String world : section.getKeys(false)) {
                worlds.put(world, RuleSet.of(section.getList(world), unknown));
            }
        }
        byWorld = worlds;
        for (String name : unknown) {
            getLogger().warning("Unknown Leviathan Client rule '" + name + "' ignored. See plugins/client-rules/README.md for the names.");
        }
    }

    /** A world's own rules replace the default ones; they don't add to them. */
    RuleSet rulesFor(World world) {
        RuleSet own = world == null ? null : byWorld.get(world.getName());
        return own != null ? own : defaults;
    }

    private void send(Player player) {
        if (!player.isOnline() || !clients.contains(player.getUniqueId())) return;
        player.sendPluginMessage(this, RULES, rulesFor(player.getWorld()).payload());
    }

    @EventHandler
    public void onRegister(PlayerRegisterChannelEvent event) {
        if (!RULES.equals(event.getChannel())) return;
        clients.add(event.getPlayer().getUniqueId());
        send(event.getPlayer());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!HELLO.equals(channel)) return;
        int protocol = RuleSet.readHello(message);
        if (protocol < 1) return;
        clients.add(player.getUniqueId());
        send(player);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        send(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        clients.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String sub = args.length == 0 ? "show" : args[0].toLowerCase();
        switch (sub) {
            case "reload" -> {
                load();
                for (Player p : Bukkit.getOnlinePlayers()) send(p);
                sender.sendMessage("Leviathan Client rules reloaded and sent to " + clients.size() + " player(s).");
            }
            case "show" -> {
                sender.sendMessage("Default: " + describe(defaults));
                for (Map.Entry<String, RuleSet> e : byWorld.entrySet()) {
                    sender.sendMessage("World " + e.getKey() + ": " + describe(e.getValue()));
                }
                sender.sendMessage(clients.size() + " player(s) online with the Leviathan Client.");
            }
            default -> {
                sender.sendMessage("Usage: /" + label + " [show|reload]");
                return true;
            }
        }
        return true;
    }

    private static String describe(RuleSet rules) {
        return rules.isEmpty() ? "nothing turned off" : String.join(", ", rules.names());
    }
}
