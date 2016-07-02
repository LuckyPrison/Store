package com.ulfric.store.shop;

import com.google.common.collect.Lists;
import com.ulfric.store.execute.StoreCommand;

import java.util.List;

public class Package implements StoreAppliable {

    private String title;
    private String description;
    private Category parent;

    private Double price;


    private List<StoreCommand> commands = Lists.newArrayList();

    public Package(String title, String description, Category parent, Double price)
    {
        this.title = title;
        this.description = description;
        this.parent = parent;
        this.price = price;
    }

    public Package withCommand(StoreCommand command)
    {
        commands.add(command);
        return this;
    }

    public Package withoutCommand(StoreCommand command)
    {
        commands.remove(command);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Category getParent()
    {
        return parent;
    }

    public Double getPrice() {
        return price;
    }

    public List<StoreCommand> getCommands() {
        return commands;
    }
}
