package me.tund.commands;

import me.tund.main.BuildAttack;
import me.tund.utils.data.DataHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class claim implements CommandExecutor {
    private final BuildAttack plugin;
    private DataHandler handler;

    public claim(BuildAttack plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("claims") || cmd.getName().equalsIgnoreCase("claim")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            handler = plugin.getHandler();
            if (claimIsOff()) {
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Command ist deaktiviert!");
                return true;
            }
            String argument;
            if (args.length == 0) {
                return claims(p); // /claim
            } else {
                argument = args[0];
            }

            if (argument.equalsIgnoreCase("list") || argument.equalsIgnoreCase("show")) {

                return listClaims(p.getUniqueId(), args, handler);

            } else {
                p.sendMessage(ChatColor.GREEN + "[Claim-Help]" + "\n" +
                        ChatColor.STRIKETHROUGH + "                                                     ");
                p.sendMessage(ChatColor.GREEN + "/claim: Claimt den Chunk in dem du stehst.");
                p.sendMessage(ChatColor.GREEN + "/claims list: Listet all deine Claims auf.");
                p.sendMessage(ChatColor.GREEN + "/unclaim: Unclaimt den Chunk in dem du stehst.");
                p.sendMessage(ChatColor.GREEN + "/unlcaim all: Unclaimt alle deine Claims.");
                p.sendMessage(ChatColor.GREEN + "/trust (Spieler): Trustet einen Spieler in dem Chunk.");
                p.sendMessage(ChatColor.GREEN + "/untrust list (Spieler): Zählt auf, wo du den Spieler getrustet hast.");
                p.sendMessage(ChatColor.GREEN + "/untrust (Spieler): Enttrustet einen Spieler in dem Chunk.");
                p.sendMessage(ChatColor.GREEN + "/untrust all (Spieler): Enttrustet einen Spieler in allen Chunks.");
                p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                                     ");
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("unclaim")) {
            if (!(sender instanceof Player)) return true;
            handler = plugin.getHandler();
            Player p = (Player) sender;
            if (claimIsOff()) {
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Command ist deaktiviert!");
                return true;
            }
            String argument;
            String argument2;
            if (args.length == 0) {
                return unClaim(p);
            } else if (args.length == 1) {
                argument = args[0];
                if (argument.equalsIgnoreCase("all")) {
                    return unClaimAll(p);
                } else {
                    p.sendMessage(ChatColor.RED + "[Error] Sorry, aber das ist kein valider Command!");
                    return true;
                }

            } else if (args.length == 2) {
                argument = args[0];
                argument2 = args[1];
                if (!argument.equalsIgnoreCase("all")) {
                    p.sendMessage(ChatColor.RED + "[Error] Sorry, aber das ist kein valider Command!");
                    return true;
                }
                if (argument2.equalsIgnoreCase("this")) {
                    Chunk chunk = p.getLocation().getChunk();
                    int x = chunk.getX() << 4;
                    int z = chunk.getZ() << 4;
                    String chunkID = "Claim/" + x + "/" + z + "/" + p.getWorld().getName();
                    if (p.isOp())
                        return unClaimAll(handler.getChunkOwner(chunkID), p);
                    else
                        p.sendMessage(ChatColor.RED + "[Error] Sorry, aber nur ein Admin kann das machen!");
                }
                Player player = Bukkit.getPlayer(argument2);
                if (player == null) {
                    p.sendMessage(ChatColor.RED + "[Error] Sorry, der Spieler muss online sein!");
                }
                if (p.isOp())
                    return unClaimAll(player.getUniqueId(), p);
                else
                    p.sendMessage(ChatColor.RED + "[Error] Sorry, aber nur ein Admin kann das machen!");
            }

        } else if (cmd.getName().equalsIgnoreCase("trust") || cmd.getName().equalsIgnoreCase("trusts")) {
            Player p = (Player) sender;
            if (claimIsOff()) {
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Command ist deaktiviert!");
                return true;
            }
            handler = plugin.getHandler();
            String argument;
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "[Error] Bitte gib ein, was du machen willst! (/trust help für Hilfe)");
                return true;
            } else {
                argument = args[0];

            }
            if (argument.equalsIgnoreCase("list") || argument.equalsIgnoreCase("listAll")) {
                return listTrusts(p.getUniqueId(), args, handler);
            } else {
                return trust(p, args, handler);
            }


        } else if (cmd.getName().equalsIgnoreCase("untrust")) {
            Player p = (Player) sender;
            if (claimIsOff()) {
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Command ist deaktiviert!");
                return true;
            }
            handler = plugin.getHandler();
            String argument;
            if (args.length == 1) {
                argument = args[0];
                if (argument.equalsIgnoreCase("all")) {
                    return unTrustAll(p, args, handler);
                }
                return untrust(p, args, handler);
            }
        }
        return true;
    }


    public boolean claims(Player p) {
        handler = plugin.getHandler();
        Chunk chunk = p.getLocation().getChunk();
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;
        String chunkID = "Claim/" + x + "/" + z + "/" + p.getWorld().getName();
        if (handler.isClaimedChunk(chunkID)) {
            Player owner = Bukkit.getPlayer(handler.getChunkOwner(chunkID));
            if (owner != null && owner.isOnline())
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Chunk ist schon von " + owner.getName() + " geclaimed!");
            else
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Chunk ist schon geclaimed!");
            return true;
        }
        int all = handler.getPlayerClaims(p.getUniqueId()).size();
        String purse = "Player." + p.getUniqueId();
        String bank = "Bank." + p.getUniqueId();
        String history = "History." + p.getUniqueId();
        float multi = (float) plugin.settings.getConfig().getDouble("MoneyMultiplier");
        if (all > 9) {
            float price = Math.round(((2000 + 1000 * (all / 1.2F)) - ((2000 + 1000 * (all / 1.2F)) * (1 / all))) * (1 / multi));
            float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
            float purseAmount = (float) plugin.economy.getConfig().getDouble(purse);

            if (bankAmount + purseAmount < price) {
                p.sendMessage(ChatColor.RED + "[Economy] Sorry, aber du hast nicht genug Geld!");
                p.sendMessage(ChatColor.RED + "[Economy] Der Preis beträgt: " + price + "$");
                return true;
            } else {
                if (bankAmount - price < 0) {
                    float newPrice = -1 * (bankAmount - price);
                    plugin.economy.getConfig().set(bank, 0.00F);
                    plugin.economy.getConfig().set(purse, purseAmount - newPrice);
                    plugin.economy.getConfig().set(history, "-" + bankAmount + "$ für eine Claimzahlung.");
                    plugin.economy.saveConfig();
                } else {
                    plugin.economy.getConfig().set(bank, bankAmount - price);
                    plugin.economy.getConfig().set(history, "-" + price + "$ für eine Claimzahlung.");
                    plugin.economy.saveConfig();
                }
            }
        }
        handler.addClaim(chunkID, p.getUniqueId());
        handler.addClaimToList(chunkID, p.getUniqueId());
        p.sendMessage(ChatColor.GREEN + "[Claims] Du hast diesen Chunk nun geclaimt!");

        return true;
    }

    public boolean unClaim(Player p) {
        handler = plugin.getHandler();
        Chunk chunk = p.getLocation().getChunk();
        int x = chunk.getX() << 4;
        int z = chunk.getZ() << 4;
        String chunkID = "Claim/" + x + "/" + z + "/" + p.getWorld().getName();
        if (!handler.isClaimedChunk(chunkID)) {
            p.sendMessage(ChatColor.RED + "[Error] Dieser Chunk ist nicht geclaimt!");
            return true;
        }
        if (!handler.isChunkOwner(chunkID, p.getUniqueId()) && !p.isOp()) {
            p.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst der Besitzer des Chunks sein!");
            return true;
        }
        if (handler.hasTrustedPlayers(chunkID)) {
            if (handler.getTrustedPlayers(chunkID).size() > 0) {
                List<String> list = handler.getTrustedPlayers(chunkID);
                for (int i = 0; i < list.size(); i++) {
                    String b = list.get(i);
                    handler.removeTrustFromList(chunkID, b);
                    handler.untrustPlayer(chunkID, b);
                }
            }
        }

        int all = handler.getPlayerClaims(p.getUniqueId()).size();
        String bank = "Bank." + p.getUniqueId();
        String history = "History." + p.getUniqueId();
        float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
        float multi = (float) plugin.settings.getConfig().getDouble("MoneyMultiplier");
        if (all > 9) {
            plugin.economy.getConfig().set(bank, bankAmount + 2000.00F * multi);
            plugin.economy.getConfig().set(history, 2000.00F * multi + "$ Rückerstattung");
            plugin.economy.saveConfig();
            p.sendMessage(ChatColor.GREEN + "[Economy] Dir wurden" + 2000.00F + multi + "$ rückerstattet!");
        }
        handler.removeClaim(chunkID);
        handler.removeClaimFromList(chunkID, handler.getChunkOwner(chunkID));


        p.sendMessage(ChatColor.GREEN + "[Claims] Du hast diesen Chunk nun entclaimt!");

        return true;
    }

    public boolean unClaimAll(Player p) {
        handler = plugin.getHandler();
        if (!handler.hasClaims(p.getUniqueId())) {
            if (p.isOnline())
                p.sendMessage(ChatColor.RED + "[Error] Du besitzt keine Claims, die man löschen könnte!");

            return true;
        }
        List<String> claims = handler.getPlayerClaims(p.getUniqueId());
        if (claims.size() > 9) {
            int amount = claims.size() - 9;
            String bank = "Bank." + p.getUniqueId();
            String history = "History." + p.getUniqueId();
            float bankAmount = (float) plugin.economy.getConfig().getDouble(bank);
            float multi = (float) plugin.settings.getConfig().getDouble("MoneyMultiplier");
            plugin.economy.getConfig().set(bank, bankAmount + 2000.00F * amount * multi);
            plugin.economy.getConfig().set(history, 2000.00F * amount * multi + "$ Rückerstattung");
            plugin.economy.saveConfig();
            p.sendMessage(ChatColor.GREEN + "[Economy] Dir wurden " + 2000 * amount * multi + "$ rückerstattet!");
        }

        for (String ID : claims) {
            if (handler.hasTrustedPlayers(ID)) {
                List<String> list = handler.getTrustedPlayers(ID);
                for (int i = 0; i < list.size(); i++) {
                    String b = list.get(i);
                    handler.removeTrustFromList(ID, b);
                }
            }
            handler.removeClaim(ID);
        }
        handler.removeClaimEntry(p.getUniqueId());


        if (p.isOnline())
            p.sendMessage(ChatColor.GREEN + "[Claims] Alle deine Claims wurden entfernt!");

        return true;
    }

    public boolean unClaimAll(UUID p, Player sender) {
        Player player = Bukkit.getPlayer(p);
        if (!handler.hasClaims(p)) {
            if (player != null && player.isOnline())
                sender.sendMessage(ChatColor.RED + "[Error] Der Spieler besitzt keine Claims, die man löschen könnte!");

            return true;
        }
        List<String> claims = handler.getPlayerClaims(p);
        for (String ID : claims) {
            if (handler.hasTrustedPlayers(ID)) {
                for (String b : handler.getTrustedPlayers(ID)) {
                    handler.removeTrustFromList(ID, b);
                }
            }
            handler.removeClaim(ID);
        }
        handler.removeClaimEntry(p);

        if (player != null && player.isOnline())
            player.sendMessage(ChatColor.GREEN + "[Claims] Alle deine Claims wurden entfernt!");
        sender.sendMessage(ChatColor.GOLD + "[Admins] Alle claims dieses Spielers wurden gelöscht!");
        return true;
    }

    public boolean trust(Player sender, String[] args, DataHandler handler) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst den Namen des Spielers angeben!");
            return true;
        }

        Chunk chunk = sender.getLocation().getChunk();          // ChunkID
        int X = chunk.getX() << 4;
        int Z = chunk.getZ() << 4;
        String ID = "Claim/" + X + "/" + Z + "/" + sender.getWorld().getName();

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null || !player.isOnline()) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler muss sich auf dem Server befinden!");
            return true;
        }
        if (!handler.isClaimedChunk(ID)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, dieser Chunk ist nicht geclaimt!");
            return true;
        }
        if (!handler.isChunkOwner(ID, sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst der Besitzer des Chunks sein!");
            return true;
        }
        if (handler.isTrusted(ID, player.getName())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler ist hier bereits getrusted!");
            return true;
        }
        if (player.getName() == sender.getName()) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du kannst dich nicht selber trusten!");
            return true;
        }

        handler.trustPlayer(ID, player.getName());
        handler.addTrustToList(ID, player.getName());
        String[] coords;
        coords = ID.split("/");
        String x = coords[1];
        String z = coords[2];
        int xx = Integer.parseInt(x) + 8;
        int zz = Integer.parseInt(z) + 8;
        String WorldName = coords[3];
        player.sendMessage(ChatColor.GREEN + "[Claims] Der Spieler " + sender.getName() + " hat dich bei X: " + xx + " Z: " +
                zz + " in Welt: " + WorldName + " getrusted!");
        sender.sendMessage(ChatColor.GREEN + "[Claims] Du hast den Spieler " + player.getName() + " bei X: " + xx + " Z: " +
                zz + " in Welt: " + WorldName + " getrusted!");
        return true;
    }

    public boolean untrust(Player sender, String[] args, DataHandler handler) {
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst einen Spieler angeben!");
            return true;
        }
        if (!handler.hasClaims(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast keine Claims!");
        }
        Chunk chunk = sender.getLocation().getChunk();          // ChunkID
        int X = chunk.getX() << 4;
        int Z = chunk.getZ() << 4;
        String ID = "Claim/" + X + "/" + Z + "/" + sender.getWorld().getName();

        String player = args[0];

        if (!handler.isClaimedChunk(ID)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Chunk ist nicht geclaimt!");
            return true;
        }
        if (!handler.getChunkOwner(ID).equals(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber dieser Chunk gehört dir nicht!");
            return true;
        }
        if (!handler.hasTrustedPlayers(ID)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber niemand ist auf dem Chunk getrusted!");
            return true;
        }
        if (!handler.isTrusted(ID, player)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler ist hier nicht getrusted!");
            return true;
        }
        handler.removeTrustFromList(ID, player);
        handler.untrustPlayer(ID, player);

        sender.sendMessage(ChatColor.GREEN + "[Claims] Der Spieler " + player + " wurde enttrusted!");
        Player target = Bukkit.getPlayer(player);
        if (target != null && target.isOnline()) {
            target.sendMessage(ChatColor.DARK_RED + "[Claims] Du wurdest von " + sender.getName() + " " +
                    "bei X:" + X + " Z:" + Z + " in der Welt " + sender.getWorld().getName() + " enttrusted!");
        }
        return true;
    }

    public boolean unTrustAll(Player sender, String[] args, DataHandler handler) {
        List<String> trusts;
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du musst einen Spieler angeben!");
            return true;
        }
        String p = args[1];
        if (!handler.hasTrusts(p)) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber der Spieler hat entweder keine Trusts oder existiert nicht!");
            return true;
        }
        if (!handler.hasClaims(sender.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast keine Claims!");
            return true;
        }
        if (!p.equals(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "[Error] Sorry, aber du kannst dich nicht selber enttrusten!");
            return true;
        }

        trusts = handler.getTrusts(p);
        for (String ID : trusts) {
            if (handler.getChunkOwner(ID).equals(sender.getUniqueId())) {
                handler.removeTrustFromList(ID, p);
                handler.untrustPlayer(ID, p);
            }
        }
        sender.sendMessage(ChatColor.GREEN + "[Claims] Du hast den Spieler " + p + " auf allen Grundstücken enttrusted!");
        Player target = Bukkit.getPlayer(p);
        if (target != null && target.isOnline()) {
            target.sendMessage(ChatColor.DARK_RED + "[Claims] Der Spieler " + sender.getName() + " hat dich überall enttrusted!");
        }

        return true;
    }

    public boolean listClaims(UUID sender, String[] args, DataHandler handler) {
        List<String> claims;
        Player p = Bukkit.getPlayer(sender);
        Player target;

        if (args.length == 1) {   //No target
            claims = handler.getPlayerClaims(sender);
            if (claims.size() == 0 || claims == null) {
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber du hast keine Claims!");
                return true;
            }
            p.sendMessage(ChatColor.GREEN + "[Claims] Hier ist eine Liste all deiner Claims: ");
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");
            for (String chunkID : claims) {
                String[] coords;
                coords = chunkID.split("/");
                String x = coords[1];
                String z = coords[2];

                int xx = Integer.parseInt(x) + 8;
                int zz = Integer.parseInt(z) + 8;
                String WorldName = coords[3];
                p.sendMessage(ChatColor.GREEN + "Claim bei X: " + xx + " Z: " + zz + " in Welt: " + WorldName);
            }
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");
        } else {  //target in args[1]
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "[Error] Der Spieler muss sich auf dem Server befinden!");
                return true;
            }

            claims = handler.getPlayerClaims(target.getUniqueId());
            if (claims.size() == 0 || claims == null) {
                p.sendMessage(ChatColor.RED + "[Error] Der Spieler hat noch keine Claims!");
                return true;
            }
            p.sendMessage(ChatColor.GREEN + "[Claims] Hier ist eine Liste all Claims von " + target.getName() + ": ");
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");
            for (String chunkID : claims) {
                String[] coords;
                coords = chunkID.split("/");
                String x = coords[1];
                String z = coords[2];
                int xx = Integer.parseInt(x) + 8;
                int zz = Integer.parseInt(z) + 8;
                String WorldName = coords[3];
                p.sendMessage(ChatColor.GREEN + "Claim bei X: " + xx + " Z: " + zz + " in Welt: " + WorldName);
            }
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");
        }
        return true;
    }

    public boolean listTrusts(UUID sender, String[] args, DataHandler handler) {
        Player target;
        Player p = Bukkit.getPlayer(sender);
        List<String> trusts;

        if (args.length == 1) { // /trust list
            trusts = handler.getTrusts(p.getName());
            if (trusts.size() == 0 || trusts == null) {
                p.sendMessage(ChatColor.RED + "[Error] Sorry, aber du bist nirgendwo getrusted :(");
                return true;
            }
            p.sendMessage(ChatColor.GREEN + "[Claims] Hier ist eine Liste aller deiner Trusts: ");
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");
            String[] coords;
            String x;
            String z;

            String WorldName;
            for (String a : trusts) {
                coords = a.split("/");
                x = coords[1];
                z = coords[2];
                int xx = Integer.parseInt(x) + 8;
                int zz = Integer.parseInt(z) + 8;
                WorldName = coords[3];
                p.sendMessage(ChatColor.GREEN + "[Claims] Getrusted bei X: " + xx + " Z: " + zz + " in Welt: " + WorldName);

            }
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");

        } else if (args.length > 1) { // /trust list <Player>
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                p.sendMessage(ChatColor.RED + "[Error] Der Spieler muss sich auf dem Server befinden!");
                return true;
            }
            trusts = handler.getTrusts(target.getName());

            p.sendMessage(ChatColor.GREEN + "[Claims] Hier ist eine Liste aller Trusts von " + target.getName() + " auf deinen Claims: ");
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");
            String[] coords;
            String x;
            String z;
            String WorldName;
            for (String a : trusts) {
                if (handler.getChunkOwner(a).equals(sender)) {
                    coords = a.split("/");
                    x = coords[1];
                    z = coords[2];
                    int xx = Integer.parseInt(x) + 8;
                    int zz = Integer.parseInt(z) + 8;
                    WorldName = coords[3];
                    p.sendMessage(ChatColor.GREEN + "[Claims] Getrusted bei X: " + xx + " Z: " + zz + " in Welt: " + WorldName);
                }
            }
            p.sendMessage(ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "                                    ");
        }
        return true;
    }

    public boolean claimIsOff() {
        return plugin.settings.getConfig().get("Mode").equals("Off");
    }
}
