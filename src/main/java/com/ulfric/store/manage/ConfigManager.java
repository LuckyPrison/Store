package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.config.ConfigFile;
import com.ulfric.store.execute.CommandType;
import com.ulfric.store.execute.StoreCommand;
import com.ulfric.store.factory.StoreFactory;
import com.ulfric.store.shop.Category;
import com.ulfric.store.shop.Package;
import com.ulfric.store.shop.Transaction;
import com.ulfric.store.shop.sales.Coupon;
import com.ulfric.store.shop.sales.Sale;

import java.util.UUID;

public class ConfigManager extends Manager {

    private ConfigFile storeConfig;
    private ConfigFile transactionConfig;
    private ConfigFile commandConfig;
    private ConfigFile saleConfig;
    private ConfigFile couponConfig;

    public ConfigManager(Store store)
    {
        super(store);
        load();
    }

    private void load()
    {
        loadConfigs();
        loadStore();
        loadTransactions();
        loadCommands();
        loadCoupons();
        loadSales();
    }

    @Override
    public void onDisable()
    {
        saveCommands(false);
        saveTransactions(false);
        saveStore(false);
        saveSales(false);
        saveCoupons(false);
    }

    private void loadConfigs()
    {
        storeConfig = new ConfigFile(store, "store");
        transactionConfig = new ConfigFile(store, "transactions");
        commandConfig = new ConfigFile(store, "commandqueue");
        saleConfig = new ConfigFile(store, "sales");
        couponConfig = new ConfigFile(store, "coupons");
    }

    private void loadStore()
    {
        loadIds();
        loadCategories();
        loadPackages();
    }

    private void loadIds()
    {
        store.setStoreFactory(new StoreFactory(store, storeConfig.getConfig().getInt("current-id", 0)));
    }

    public void incrementId(int id)
    {
        storeConfig.getConfig().set("current-id", id);
        saveStore(true);
    }

    private void loadCategories()
    {
        StoreManager storeManager = store.getManager(StoreManager.class);
        CategoryManager categoryManager = store.getManager(CategoryManager.class);
        storeConfig.getParts("categories").forEach(part ->
        {
            int id = Integer.parseInt(part.getKey());
            Category category = Category.deserialize(store, storeConfig.getConfig(), id);
            storeManager.addItem(category);
            categoryManager.add(category);
        });
    }

    public void saveCategory(Category category)
    {
        CategoryManager categoryManager = store.getManager(CategoryManager.class);
        if (!categoryManager.get().contains(category))
        {
            categoryManager.add(category);
        }
        Category.serialize(store, category, storeConfig.getConfig());
        saveStore(true);
    }

    private void loadPackages()
    {
        StoreManager storeManager = store.getManager(StoreManager.class);
        PackageManager packageManager = store.getManager(PackageManager.class);
        storeConfig.getParts("packages").forEach(part ->
        {
            int id = Integer.parseInt(part.getKey());
            Package pack = Package.deserialize(store, storeConfig.getConfig(), id);
            storeManager.addItem(pack);
            packageManager.add(pack);
        });
    }

    public void savePackage(Package pack)
    {
        PackageManager packageManager = store.getManager(PackageManager.class);
        if (!packageManager.get().contains(pack))
        {
            packageManager.add(pack);
        }
        Package.serialize(store, pack, storeConfig.getConfig());
        saveStore(true);
    }

    private void loadTransactions()
    {
        TransactionManager transactionManager = store.getManager(TransactionManager.class);
        transactionConfig.getParts("transactions").forEach(part ->
        {
            Transaction transaction = Transaction.deserialize(
                    store,
                    transactionConfig.getConfig(),
                    part.getSection(),
                    UUID.fromString(part.getKey())
            );
            transactionManager.newTransaction(transaction);
        });
    }

    private void loadCommands()
    {
        CommandManager commandManager = store.getManager(CommandManager.class);
        commandConfig.getParts("queue").forEach(part ->
        {
            commandManager.addToQueue(
                    UUID.fromString(part.getString("uuid")),
                    new StoreCommand(
                            store,
                            part.getString("command"),
                            CommandType.valueOf(part.getString("type"))
                    )
            );
        });
    }

    private void loadCoupons()
    {
        CouponManager couponManager = store.getManager(CouponManager.class);
        couponConfig.getParts("coupons").forEach(part ->
        {
            Coupon coupon = Coupon.deserialize(store, couponConfig.getConfig(), part.getKey());
            couponManager.add(coupon, false);
        });
    }

    public void newCoupon(Coupon coupon)
    {
        Coupon.serialize(store, coupon, couponConfig.getConfig());
        saveCoupons(true);
    }

    private void loadSales()
    {
        SaleManager saleManager = store.getManager(SaleManager.class);
        saleConfig.getParts("sales").forEach(part ->
        {
            Sale sale = Sale.deserialize(store, saleConfig.getConfig(), part.getKey());
            saleManager.add(sale, false);
        });
    }

    public void newSale(Sale sale)
    {
        Sale.serialize(store, sale, saleConfig.getConfig());
        saveSales(true);
    }

    public void newCommand(UUID uuid, StoreCommand storeCommand)
    {
        UUID key = storeCommand.getCommandUUID();
        commandConfig.getConfig().set("queue." + key + ".uuid", uuid);
        commandConfig.getConfig().set("queue." + key + ".command", storeCommand.getCommand());
        commandConfig.getConfig().set("queue." + key + ".type", storeCommand.getType());
        saveCommands(true);
    }

    public void finishedCommand(StoreCommand storeCommand)
    {
        commandConfig.getConfig().set("queue." + storeCommand.getCommandUUID(), null);
        saveCommands(true);
    }

    public void saveTransaction(Transaction transaction)
    {
        Transaction.serialize(transaction, transactionConfig.getConfig(), "transactions.");
        saveTransactions(true);
    }

    public void saveCommands(boolean async)
    {
        commandConfig.save(async);
    }

    public void saveStore(boolean async)
    {
        storeConfig.save(async);
    }

    public void saveTransactions(boolean async)
    {
        transactionConfig.save(async);
    }

    public void saveSales(boolean async)
    {
        saleConfig.save(async);
    }

    public void saveCoupons(boolean async)
    {
        couponConfig.save(async);
    }

}
