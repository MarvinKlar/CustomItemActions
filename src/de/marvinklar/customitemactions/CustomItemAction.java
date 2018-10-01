package de.marvinklar.customitemactions;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomItemAction {

    public String name;
    public ItemStack item;

    public List<String> commands = new ArrayList<>();
    public List<String> messages = new ArrayList<>();
    public List<String> broadcasts = new ArrayList<>();

    public CustomItemAction(String name, ItemStack item, List<String> commands, List<String> messages, List<String> broadcasts) {
        this.name = name;
        this.item = item;
        this.commands = commands;
        this.messages = messages;
        this.broadcasts = broadcasts;
    }

    public CustomItemAction(String name, ItemStack item) {
        this.name = name;
        this.item = item;
    }

}