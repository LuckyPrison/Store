package com.ulfric.store.shop;

public interface StoreAppliable {

    default boolean appliableTo(StoreAppliable appliable)
    {
        if (this.equals(appliable))
        {
            return true;
        }
        if (this instanceof Package && appliable instanceof Category)
        {
            if (((Package) this).getParent().equals(appliable))
            {
                return true;
            }
        }
        return false;
    }

    int getId();

    void save();

}
