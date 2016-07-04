package com.ulfric.store.manage.player;

import com.ulfric.store.Store;
import com.ulfric.store.manage.Manager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager extends Manager {

    private Map<UUID, StorePlayer> players = new HashMap<>();

    public PlayerManager(Store store)
    {
        super(store);
    }

    @Override
    public void onEnable()
    {
        store.getServer().getOnlinePlayers().forEach(this::register);
    }

    @Override
    public void onDisable()
    {
        store.getServer().getOnlinePlayers().forEach(this::deregister);
    }

    public StorePlayer register(Player player)
    {
        return players.put(player.getUniqueId(), new StorePlayer(store, player));
    }

    public void deregister(Player player)
    {
        players.remove(player.getUniqueId());
    }

    public StorePlayer getPlayer(Player player)
    {
        StorePlayer storePlayer = players.get(player.getUniqueId());
        if (storePlayer == null)
        {
            storePlayer = register(player);
        }
        return storePlayer;
    }

}
