package com.ulfric.store.command;

import com.ulfric.store.Store;
import com.ulfric.store.manage.player.PlayerManager;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.util.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReturn implements CommandExecutor {

    private final Store store;

    public CommandReturn(Store store)
    {
        this.store = store;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Chat.color("&cThis is an in-game command"));
            sender.sendMessage(Chat.color("&cGo away Adam =("));
            return false;
        }
        StorePlayer player = store.getManager(PlayerManager.class).getPlayer((Player) sender);
        if (!player.isWatchingPackage())
        {
            player.sendMessage("chat.not-viewing-package");
            return false;
        }
        player.backToGUI();
        return false;
    }
}
