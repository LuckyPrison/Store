package com.ulfric.store.command;

import com.ulfric.store.Store;
import com.ulfric.store.manage.player.PlayerManager;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.pages.user.UserHomePage;
import com.ulfric.store.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStore implements CommandExecutor {

    private final Store store;

    public CommandStore(Store store)
    {
        this.store = store;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Chat.color("&cPlease run as a player!"));
            return false;
        }
        Player player = (Player) sender;
        StorePlayer storePlayer = store.getManager(PlayerManager.class).getPlayer(player);
        storePlayer.openPage(new UserHomePage(store, storePlayer), true);
        return false;
    }

}
