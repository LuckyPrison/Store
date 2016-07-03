package com.ulfric.store.manage;

import com.ulfric.store.Store;
import com.ulfric.store.shop.sales.Sale;

import java.util.List;

public class SaleManager extends ListManager<Sale> {

    public SaleManager(Store store)
    {
        super(store);
    }

    public void add(Sale sale, boolean newSale)
    {
        super.add(sale);
        if (newSale)
        {
            store.getManager(ConfigManager.class).newSale(sale);
        }
    }

    public void remove(Sale sale)
    {
        super.remove(sale);
    }

    public List<Sale> getSales()
    {
        return get();
    }

}
