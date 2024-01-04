package me.tund.utils.data;

import me.tund.main.BuildAttack;
import me.tund.utils.TeamHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class DataHandler {
    public List<Player> joins;
    private BuildAttack plugin;
    private TeamHandler teams;

    public DataHandler(BuildAttack plugin) {
        this.plugin = plugin;
        this.teams = plugin.getTeams();
        this.joins = new ArrayList<>();
    }

    /**
     * Fügt einen Claim in der HashMap hinzu.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als UUID
     */
    public void addClaim(String ID, UUID Player) {
        if (plugin.claimedChunk.containsKey(ID)) return;
        plugin.claimedChunk.put(ID, Player);
    }

    /**
     * Entfernt einen Claim aus der HashMap.
     *
     * @param ID Unique-Chunk-ID als String
     */
    public void removeClaim(String ID) {
        if (!plugin.claimedChunk.containsKey(ID)) return;
        plugin.claimedChunk.remove(ID);
        if (hasTrustedPlayers(ID))
            plugin.trust.remove(ID);
    }

    /**
     * Prüft, ob der Chunk bereits in der HashMap aufgelistet ist.
     *
     * @param ID Unique-Chunk-ID als String
     * @return Ob die Unique-Chunk-ID bereits vorhanden ist
     */
    public boolean isClaimedChunk(String ID) {
        return plugin.claimedChunk.containsKey(ID);
    }

    /**
     * Prüft, ob der angegebene Spieler besitzer des Chunks ist. Returned "false" wenn der Chunk nicht geclaimt ist.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als UUID
     * @return Ob der Spieler besitzer dieses Chunks ist. False, wenn Chunk nicht geclaimt ist.
     */
    public boolean isChunkOwner(String ID, UUID Player) {
        if (isClaimedChunk(ID))
            return getChunkOwner(ID).equals(Player);
        else
            return false;
    }

    /**
     * Gibt den Besitzer eines Chunk-Claims zurück als UUID. Returned "null" wenn der Chunk nicht geclaimt ist.
     *
     * @param ID Unique-Chunk-ID als String
     * @return Besitzer des Claims, null wenn Chunk nicht geclaimt
     */
    public UUID getChunkOwner(String ID) {
        if (plugin.claimedChunk.containsKey(ID))
            return plugin.claimedChunk.get(ID);
        else
            return null;
    }

    /**
     * Fügt einen Claim zu der Liste eines Spielers hinzu. Erstellt einen neuen Eintrag, wenn der Spieler noch keine Liste besitzt.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als UUID
     */
    public void addClaimToList(String ID, UUID Player) {
        List<String> list;
        if (!plugin.claimList.containsKey(Player)) {
            list = new ArrayList<>();
            list.add(ID);
        } else {
            list = plugin.claimList.get(Player);
            list.add(ID);
            plugin.claimList.remove(Player);
        }
        plugin.claimList.put(Player, list);
    }

    /**
     * Entfernt einen Claim aus einer Liste eines Spielers. Löscht den Eintrag, wenn die Liste leer ist.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als UUID
     */
    public void removeClaimFromList(String ID, UUID Player) {
        if (!plugin.claimList.containsKey(Player)) return;
        List<String> list = plugin.claimList.get(Player);

        if (list.contains(ID))
            list.remove(ID);
        else
            return;
        plugin.claimList.remove(Player);
        if (list.size() != 0)
            plugin.claimList.put(Player, list);

    }

    /**
     * Entfernt die gesammte Claim-Liste eines Spielers.
     *
     * @param Player als UUID
     */
    public void removeClaimEntry(UUID Player) {
        if (plugin.claimList.containsKey(Player))
            plugin.claimList.remove(Player);
    }

    /**
     * Überprüft, ob der Spieler eine Liste von Claims hat.
     *
     * @param Player als UUID
     * @return Ob der Spieler Claims besitzt
     */
    public boolean hasClaims(UUID Player) {
        return plugin.claimList.containsKey(Player);
    }

    /**
     * Gibt die Anzahl der Claims, die der Spieler hat, zurück.
     *
     * @param Player als UUID
     * @return Anzahl der Claims, 0 wenn keine Liste vorhanden.
     */
    public int playerClaims(UUID Player) {
        if (!hasClaims(Player)) return 0;
        List<String> list = plugin.claimList.get(Player);
        return list.size();
    }

    /**
     * Gibt eine Liste aller Claims, die der Spieler hat, zurück.
     *
     * @param Player als UUID
     * @return Liste aller Unique-Chunk-IDs des Spielers, leere Liste wenn keine vorhanden.
     */
    public List<String> getPlayerClaims(UUID Player) {
        List<String> list = new ArrayList<>();
        if (!hasClaims(Player)) return list;

        return plugin.claimList.get(Player);
    }

    /**
     * Fügt einen Spieler in der Liste eines Chunks hinzu.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als String
     */
    public void trustPlayer(String ID, String Player) {
        List<String> trusted;
        if (!plugin.trust.containsKey(ID)) {
            trusted = new ArrayList<>();
            trusted.add(Player);
            plugin.trust.put(ID, trusted);
            return;
        }
        trusted = plugin.trust.get(ID);
        trusted.add(Player);
        plugin.trust.remove(ID);
        plugin.trust.put(ID, trusted);
    }

    /**
     * Entfernt einen Spieler aus der Liste eines Chunks und entfernt die Liste, wenn keine Trusts mehr vorhanden sind.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als String
     */
    public void untrustPlayer(String ID, String Player) {
        List<String> trusted;
        if (!plugin.trust.containsKey(ID)) return;

        trusted = plugin.trust.get(ID);
        if (!trusted.contains(Player)) return;

        trusted.remove(Player);
        plugin.trust.remove(ID);
        if (trusted.size() > 0)
            plugin.trust.put(ID, trusted);
    }

    /**
     * Überprüft ob der Spieler in einer Chunk-Liste vorhanden ist.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als String
     * @return Ob der Spieler getrusted ist.
     */
    public boolean isTrusted(String ID, String Player) {
        List<String> trusted;
        if (!plugin.trust.containsKey(ID)) return false;
        trusted = plugin.trust.get(ID);

        return trusted.contains(Player);
    }

    /**
     * @param ID Unique-Chunk-ID als String
     * @return Die Liste mite den getrusteten Spielern, wenn nicht vorhanden null.
     */
    public List<String> getTrustedPlayers(String ID) {
        if (plugin.trust.containsKey(ID))
            return plugin.trust.get(ID);
        else
            return null;
    }

    /**
     * @param ID Unique-Chunk-ID als String
     * @return Ob eine Liste in der HashMap vorhanden ist.
     */
    public boolean hasTrustedPlayers(String ID) {
        return plugin.trust.containsKey(ID);
    }

    /**
     * Fügt einen Trust zu der Liste eines Spielers hinzu.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als String
     */
    public void addTrustToList(String ID, String Player) {
        List<String> chunk;
        if (!plugin.trustList.containsKey(Player)) {
            chunk = new ArrayList<>();
            chunk.add(ID);
            plugin.trustList.put(Player, chunk);
            return;
        }
        chunk = plugin.trustList.get(Player);
        chunk.add(ID);
        plugin.trustList.remove(Player);
        plugin.trustList.put(Player, chunk);
    }

    /**
     * Entfernt einen Trust aus der Liste eines Spielers. Entfernt die Liste aus der HashMap, wenn der Spieler keine Trusts hat.
     *
     * @param ID     Unique-Chunk-ID als String
     * @param Player als String
     */
    public void removeTrustFromList(String ID, String Player) {
        List<String> chunk;
        if (!plugin.trustList.containsKey(Player)) return;

        chunk = plugin.trustList.get(Player);
        if (!chunk.contains(ID)) return;

        chunk.remove(ID);
        plugin.trustList.remove(Player);
        if (chunk.size() != 0)
            plugin.trustList.put(Player, chunk);
    }

    /**
     * Ermöglicht das manuelle Entfernen der Liste an Trusts von einem Spieler.
     *
     * @param Player als String
     */
    public void removeTrustEntry(String Player) {
        if (plugin.trustList.containsKey(Player))
            plugin.trustList.remove(Player);
    }

    /**
     * @param Player als String
     * @return Ob der Spieler eine Liste mit Trusts besitzt.
     */
    public boolean hasTrusts(String Player) {
        return plugin.trustList.containsKey(Player);
    }

    /**
     * @param Player als String
     * @return Gibt eine Liste mit allen Trusts eines Spielers zurück, gibt eine leere Liste, wenn keine Trusts vorhanden sind zurück.
     */
    public List<String> getTrusts(String Player) {
        List<String> list = new ArrayList<>();
        if (!plugin.trustList.containsKey(Player)) return list;

        return plugin.trustList.get(Player);
    }

    /**
     * Registriert einen neuen Clan in der HashMap und ein Team auf den Clan-Namen.
     *
     * @param name   als String
     * @param leader als UUID
     */
    public void addClan(String name, UUID leader) {
        if (plugin.clanLeaders.containsKey(name)) return;
        plugin.clanLeaders.put(name, leader);

        List<UUID> list = new ArrayList<>();
        list.add(leader);
        plugin.clanMembers.put(name, list);
        plugin.privacy.put(name, false);
        createPvpEntry(name);

        teams.createTeam(name);
        teams.addPlayerTeam(Bukkit.getPlayer(leader), name);

    }

    /**
     * Entfernt den Clan aus der HashMap und unregistriert das Team mit dem Clan-Namen.
     *
     * @param name als String
     */
    public void removeClan(String name) {
        if (!plugin.clanLeaders.containsKey(name)) return;
        plugin.clanLeaders.remove(name);

        if (plugin.clanMembers.containsKey(name))
            plugin.clanMembers.remove(name);

        teams.deleteTeam(name);
        plugin.privacy.remove(name);
        removePvpEntry(name);
    }

    /**
     * @param name als String
     * @return Ob der Name ein Clan in der HashMap ist.
     */
    public boolean isExistingClan(String name) {
        return plugin.clanLeaders.containsKey(name);
    }

    /**
     * @param name als String
     * @return Gibt den Leader als UUID zurück, gibt null zurück, wenn der Clan nicht existiert.
     */
    public UUID getClanLeader(String name) {
        if (!plugin.clanLeaders.containsKey(name)) return null;
        return plugin.clanLeaders.get(name);
    }

    /**
     * @param name   als String
     * @param player als UUID
     * @return Ob der angegebene Spieler der Besitzer des Clans ist.
     */
    public boolean isClanLeader(String name, UUID player) {
        if (!isExistingClan(name)) return false;
        return getClanLeader(name).equals(player);
    }

    /**
     * Unregistriert und registriert den Clan mit einem neuen Besitzer.
     *
     * @param name        als String
     * @param replacement als UUID
     */
    public void updateLeader(String name, UUID replacement) {
        if (!isExistingClan(name)) return;
        plugin.clanLeaders.remove(name);
        plugin.clanLeaders.put(name, replacement);
    }

    /**
     * Fügt einen Spieler zu der Liste der Mitgleider eines Clans hinzu.
     *
     * @param name   als String
     * @param player als UUID
     */
    public void addPlayerToClan(String name, UUID player) {
        List<UUID> list;
        if (!isExistingClan(name)) return;

        list = plugin.clanMembers.get(name);
        list.add(player);

        plugin.clanMembers.remove(name);
        plugin.clanMembers.put(name, list);


    }

    /**
     * Entfernt einen Spieler aus der Liste der Mitglieder eines Clans.
     *
     * @param name   als String
     * @param player als UUID
     */
    public void removePlayerFromClan(String name, UUID player) {
        List<UUID> list;
        if (!isExistingClan(name)) return;
        if (!teams.hasTeam(Bukkit.getPlayer(player))) return;
        if (!plugin.clanMembers.get(name).contains(player)) return;

        list = plugin.clanMembers.get(name);
        list.remove(player);
        plugin.clanMembers.remove(name);

        if (Bukkit.getPlayer(player) == null || !Bukkit.getPlayer(player).isOnline()) {

            Player p = (Player) Bukkit.getOfflinePlayer(player);
            teams.remPlayerTeam(p);
        } else {
            teams.remPlayerTeam(Bukkit.getPlayer(player));
        }


        if (list.size() > 0)
            plugin.clanMembers.put(name, list);
        else {
            removeClan(name);
        }
    }

    /**
     * Funktioniert bei /clan rename nicht, wenn der Spieler offline ist, da der Spieler nicht im Team ist!
     * Für Fälle wo das Team sich ändert un der Spieler offline sein könnte, getClanNameByList verwenden!
     *
     * @param player als UUID
     * @return Name des Spieler-Clans
     */
    @Deprecated
    public String getPlayerClan(UUID player) {
        Player p = Bukkit.getPlayer(player);
        if (!teams.hasTeam(p)) return "MissingClan";
        Team team = teams.getTeam(p);

        return team.getName();
    }

    /**
     * Funktioniert bei /clan rename nicht, wenn der Spieler offline ist, da der Spieler nicht im Team ist!
     * Für Fälle wo das Team sich ändert und der Spieler offline sein könnte, isNotInTeam verwenden!
     *
     * @param player als UUID
     * @return Ob der Spieler momentan ein Team hat (→ in einem Clan ist)
     */
    @Deprecated
    public boolean playerHasClan(UUID player) {
        return teams.hasTeam(Bukkit.getPlayer(player));
    }

    /**
     * Prüft jede Liste von Mitgliedern, ob der Spieler vorhanden ist und returned "true" sobald er gefunden wurde.
     *
     * @param player als UUID
     * @return Ob der Spieler einen Clan hat.
     */
    public boolean isInClan(UUID player) {
        boolean ret = false;
        for (Map.Entry<String, List<UUID>> members : plugin.clanMembers.entrySet()) {
            if (ret) {
                return true;
            }
            List<UUID> ids = members.getValue();
            if (ids.contains(player)) {
                ret = true;
            }
        }
        return ret;
    }


    /**
     * Prüft jede Liste von Mitgliedern, ob der Spieler vorhanden ist und returned den Clan-Namen, wenn der Spieler gefunden wurde.
     * Wenn der Spieler kein Clan besitzt, wird "MissingClan" returned.
     *
     * @param player als UUID
     * @return Name des Spieler-Clans
     */
    public String getClanNameByList(UUID player) {
        String ret = "MissingClan";
        boolean done = false;
        for (Map.Entry<String, List<UUID>> members : plugin.clanMembers.entrySet()) {
            if (done) {
                return ret;
            }
            List<UUID> ids = members.getValue();
            if (ids.contains(player)) {
                ret = members.getKey();
                done = true;
            }
        }
        return ret;
    }

    /**
     * Überprüft, ob der Clan in der Hashmap verzeichnet ist. Returned eine leere Liste, wenn der Clan nicht existiert.
     *
     * @param name des Clans als String
     * @return Liste aller Spieler-UUIDs in diesem Clan
     */
    public List<UUID> getClanMembers(String name) {
        List<UUID> list = new ArrayList<>();
        if (!isExistingClan(name)) return list;

        return plugin.clanMembers.get(name);
    }

    /**
     * @param name des Clans als String
     * @return Ob der Clan privat ist oder nicht.
     */
    public boolean clanIsPrivate(String name) {
        if (!plugin.privacy.containsKey(name)) return false;

        return plugin.privacy.get(name);
    }

    /**
     * Wechselt die Clan-Privacy jeweils.
     *
     * @param name des Clans als String
     */
    public void togglePrivacy(String name) {
        if (!plugin.privacy.containsKey(name)) {
            plugin.privacy.put(name, false);
            return;
        }

        plugin.privacy.put(name, !plugin.privacy.get(name));

    }

    /**
     * Fügt eine Berechtigung zum Spieler hinzu, welche mit der standard Methode .getPermissions() ausgelesen kann.
     *
     * @param name des Spielers als String
     * @param patt Name der Berechtigung als String (bsp. join.TUTEL)
     */
    public void addPerm(String name, String patt) {
        PermissionAttachment att = plugin.perms.get(name);
        att.setPermission(patt, true);
        plugin.perms.remove(name);
        plugin.perms.put(name, att);
    }

    /**
     * Entfernt eine Berechtigung eines Spielers, falls vorhanden.
     *
     * @param name des Spielers als String
     * @param patt Name der Berechtigung als String (bsp. join.TUTEL)
     */
    public void remPerm(String name, String patt) {

        PermissionAttachment att = plugin.perms.get(name);
        att.unsetPermission(patt);
        plugin.perms.remove(name);
        plugin.perms.put(name, att);

    }

    /**
     * Fügt einem Spieler eine Berechtigungsliste hinzu und speichert diese in einer HashMap.
     *
     * @param player Spieler, welchem die Berechtigungsliste angeheftet werden soll.
     */
    public void createAtt(Player player) {
        PermissionAttachment att = player.addAttachment(plugin);
        plugin.perms.put(player.getName(), att);
    }

    /**
     * Entfernt eine Berechtigungsliste des angegebenen Spielers.
     *
     * @param player Spieler, welchem die Berechtigungsliste weggenommen werden soll.
     */
    public void removeAtt(Player player) {
        for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            if (plugin.perms.get(player.getName()) != null) {
                if (perm.getAttachment() != null) {

                    plugin.perms.remove(player.getName());
                    player.removeAttachment(perm.getAttachment());
                }
            }
        }
    }

    /**
     * Fügt einen Eintrag in der PVP-Hashmap hinzu
     *
     * @param name Clanname als String
     */
    public void createPvpEntry(String name) {
        if (plugin.pvp.containsKey(name)) return;
        plugin.pvp.put(name, false);
    }

    /**
     * Entfernt einen Eintrag in der PVP-Hashmap
     *
     * @param name Clanname als String
     */
    public void removePvpEntry(String name) {
        if (!plugin.pvp.containsKey(name)) return;
        plugin.pvp.remove(name);
    }

    /**
     * Dreht die Einstellung des PVPs eines Clans um
     *
     * @param name Clanname als String
     */
    public void togglePvp(String name) {
        if (!plugin.pvp.containsKey(name)) return;
        boolean b = plugin.pvp.get(name);
        plugin.pvp.put(name, !b);
    }

    /**
     * @param name Clanname als String
     * @return Ob der Clan PVP an hat. Gibt false zurück wenn der Clan keinen Eintrag hat.
     */
    public boolean hasPvpEnabled(String name) {
        if (!plugin.pvp.containsKey(name)) return false;
        return plugin.pvp.get(name);
    }

}
