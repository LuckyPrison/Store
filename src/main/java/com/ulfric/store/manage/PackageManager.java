package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.shop.Package;

import java.util.UUID;

public class PackageManager extends ListManager<Package> {

    public PackageManager(Store store)
    {
        super(store);
    }

    public Package getPackage(int id)
    {
        return elements.stream().filter(pack -> pack.getId() == id).findFirst().orElse(null);
    }

    public void execute(Package item, UUID uuid)
    {
        item.getCommands().forEach(command ->
                store.getManager(CommandManager.class).addToQueue(uuid, command, true));
    }

}
