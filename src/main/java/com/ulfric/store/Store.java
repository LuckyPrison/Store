package com.ulfric.store;

import com.google.common.collect.Lists;
import com.ulfric.store.command.CommandReturn;
import com.ulfric.store.command.CommandStore;
import com.ulfric.store.factory.CouponFactory;
import com.ulfric.store.factory.SaleFactory;
import com.ulfric.store.factory.StoreFactory;
import com.ulfric.store.locale.Locale;
import com.ulfric.store.manage.*;
import com.ulfric.store.manage.player.PlayerManager;
import com.ulfric.store.manage.player.StorePlayer;
import com.ulfric.store.protocol.Reflection;
import com.ulfric.store.protocol.TinyProtocol;
import io.netty.channel.Channel;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Store extends JavaPlugin {

    private List<Manager> managers;
    private StoreFactory storeFactory;
    private CouponFactory couponFactory;
    private SaleFactory saleFactory;

    @Override
    public void onEnable()
    {
        getDataFolder().mkdirs();
        new File(getDataFolder(), "logs").mkdirs();
        loadFactories();
        loadManagers();
        loadCommands();
        Locale.load(this);
        protocol();
        String foo = "blah " + ChatColor.GREEN;
    }

    @Override
    public void onDisable()
    {
        unloadManagers();
    }

    private void loadFactories()
    {
        this.couponFactory = new CouponFactory(this);
        this.saleFactory = new SaleFactory(this);
    }

    private void loadManagers()
    {
        managers = Lists.newArrayList(
                new StoreManager(this),
                new CategoryManager(this),
                new PackageManager(this),
                new LogManager(this),
                new CommandManager(this),
                new TransactionManager(this),
                new CouponManager(this),
                new SaleManager(this),
                new ConfigManager(this),
                new PlayerManager(this),
                new GUIManager(this)
        );

        managers.forEach(Manager::onEnable);
    }

    private void loadCommands()
    {
        getCommand("storereturn").setExecutor(new CommandReturn(this));
        getCommand("store").setExecutor(new CommandStore(this));
    }

    private void protocol()
    {
        Reflection.FieldAccessor<IChatBaseComponent> chatMessage = Reflection.getField("{nms}.PacketPlayOutChat", IChatBaseComponent.class, 0);
        new TinyProtocol(this)
        {

            @Override
            public Object onPacketOutAsync(Player receiver, Channel channel, Object packet)
            {
                if (chatMessage.hasField(packet))
                {
                    PlayerManager manager = getManager(PlayerManager.class);
                    if (manager == null)
                    {
                        return super.onPacketOutAsync(receiver, channel, packet);
                    }
                    StorePlayer player = manager.getPlayer(receiver);
                    if (player == null)
                    {
                        return super.onPacketOutAsync(receiver, channel, packet);
                    }
                    if (player.isWatchingPackage())
                    {
                        return null;
                    }
                }
                return super.onPacketOutAsync(receiver, channel, packet);
            }

        };
    }

    private void unloadManagers()
    {
        Lists.reverse(managers).stream().forEach(Manager::onDisable);
    }

    public <T extends Manager> T getManager(Class<T> type)
    {
        for (Manager manager : managers)
        {
            if (manager.getClass().equals(type))
            {
                return (T) manager;
            }
        }
        try
        {
            getLogger().severe(String.format("Process attempted to retrieve Manager of type %s but it was not registered!", type));
            getLogger().severe("Dumping Thread stack!");
            Thread.dumpStack();
            Manager manager = type.getDeclaredConstructor(Store.class).newInstance();
            managers.add(manager);
            return (T) manager;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {}

        return null;
    }

    public StoreFactory getStoreFactory()
    {
        return storeFactory;
    }

    public void setStoreFactory(StoreFactory storeFactory)
    {
        this.storeFactory = storeFactory;
    }

    public CouponFactory getCouponFactory()
    {
        return couponFactory;
    }

    public SaleFactory getSaleFactory()
    {
        return saleFactory;
    }
}
