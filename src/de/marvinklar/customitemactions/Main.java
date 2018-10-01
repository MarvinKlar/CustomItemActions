package de.marvinklar.customitemactions;

import de.marvinklar.customitemactions.commands.CustomItemActionsCommand;
import de.marvinklar.customitemactions.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static final HashMap<String, CustomItemAction> customItemActions = new HashMap<>();
    private static MessageManager msgManager;
    private static Main instance;

    public static Main getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        getConfig().addDefault("Messages.Prefix", "&7[&6CustomItemActions&7]");
        getConfig().addDefault("Messages.Only_For_Players", "%prefix% &cThis command is only for players.");
        getConfig().addDefault("Messages.No_CustomItemActions", "%prefix% &cThere are no CustomItemActions yet.");
        getConfig().addDefault("Messages.Created_CustomItemAction", "%prefix% &aThe CustomItemAction &8%name% &awas created successfully.");
        getConfig().addDefault("Messages.Deleted_CustomItemAction", "%prefix% &aThe CustomItemAction &8%name% &awas deleted successfully.");
        getConfig().addDefault("Messages.Invalid_Item_In_Hand", "%prefix% &cPlease hold an item in your hand, which you would like to register for this CustomItemAction.");
        getConfig().addDefault("Messages.Use_Permission", "CustomItemActions.use");
        getConfig().addDefault("Messages.Edit_Permission", "CustomItemActions.edit");
        getConfig().addDefault("Messages.No_Permission", "%prefix% &cYou don't have enough permissions to use this command.");
        getConfig().addDefault("Messages.Custom_Item_Action_Does_Not_Exists", "%prefix% &cThe CustomItemAction &8%name% &cdoes not exist.");
        getConfig().addDefault("Messages.Player_Not_Online", "%prefix% &cThe player &8%player% &cis not online.");
        getConfig().addDefault("Messages.Gave_CustomItemAction", "%prefix% &aYou gave the CustomItemAction &8%name% &ato the player &8%player%&a.");
        getConfig().addDefault("Messages.Invalid_ID", "%prefix% &cThe ID &8%id% &cis invalid.");
        getConfig().addDefault("Messages.Custom_Item_Action_Does_Already_Exist", "%prefix% &cThe CustomItemAction &8%name% &cdoes already exist.");
        getConfig().addDefault("Messages.Command_Not_Found", "%prefix% &cThe command with the ID &8%id% &cwas not found.");
        getConfig().addDefault("Messages.Message_Not_Found", "%prefix% &cThe message with the ID &8%id% &cwas not found.");
        getConfig().addDefault("Messages.Broadcast_Not_Found", "%prefix% &cThe broadcast with the ID &8%id% &cwas not found.");
        getConfig().addDefault("Messages.Command_Removed", "%prefix% &aThe command with the ID &8%id% &awas removed.");
        getConfig().addDefault("Messages.Message_Removed", "%prefix% &aThe message with the ID &8%id% &awas removed.");
        getConfig().addDefault("Messages.Broadcast_Removed", "%prefix% &aThe broadcast with the ID &8%id% &awas removed.");
        getConfig().addDefault("Messages.Command_Added", "%prefix% &aThe command &8%command% &awas added successfully.");
        getConfig().addDefault("Messages.Message_Added", "%prefix% &aThe message &8%message% &awas added successfully.");
        getConfig().addDefault("Messages.Broadcast_Added", "%prefix% &aThe broadcast &8%broadcast% &awas added successfully.");
        getConfig().addDefault("Messages.List_CustomItemActions", Arrays.asList(new String[] { "&4%name% &7(&c%item%&7):", "&eCommands:", "&7%commands%", "&eMessages:", "&7%messages%", "&eBroadcasts:", "&7%broadcasts%" }));
        getConfig().addDefault("Messages.Menu", Arrays.asList(new String[] { "&8/cia list &7- &8Lists all CustomItemActions",
                "&8/cia create <name> &7- &8Creates a CustomItemAction with the item in your hand",
                "&8/cia delete <name> &7- &8Deletes a CustomItemAction by name",
                "&8/cia give <name> <player> &7- &8Gives a CustomItemAction to a player",
                "&8/cia <name> add cmd|msg|bc <command|message|broadcast> &7- &8Adds a command, message or broadcast to a CustomItemAction",
                "&eYou can use the placeholder %player% in the commands, messages and broadcasts",
                "&8/cia <name> remove cmd|msg|bc <id> &7- &8Deletes a command, message or broadcast from a CustomItemAction" }));
        saveConfig();

        msgManager = new MessageManager(getConfig());

        if(getConfig().contains("CustomItemActions")) {
            for(String customItemAction : getConfig().getConfigurationSection("CustomItemActions").getKeys(false)) {
                customItemActions.put(customItemAction, new CustomItemAction(customItemAction, getConfig().getItemStack("CustomItemActions." + customItemAction + ".Item"), getConfig().getStringList("CustomItemActions." + customItemAction + ".Commands"), getConfig().getStringList("CustomItemActions." + customItemAction + ".Messages"), getConfig().getStringList("CustomItemActions." + customItemAction + ".Broadcasts")));
            }
        }

        getCommand("customitemactions").setExecutor(new CustomItemActionsCommand());

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static MessageManager getMessageManager() {
        return msgManager;
    }

    public static void createCustomItemAction(CustomItemAction customItemAction) {
        customItemActions.put(customItemAction.name, customItemAction);
        Main.getInstance().getConfig().set("CustomItemActions." + customItemAction.name + ".Item", customItemAction.item);
        Main.getInstance().saveConfig();
    }

    public static void deleteCustomItemAction(String name) {
        customItemActions.remove(name);
        Main.getInstance().getConfig().set("CustomItemActions." + name, null);
        Main.getInstance().saveConfig();
    }

    public static String getArgs(final String[] args, final int num)
    {
        final StringBuilder sb = new StringBuilder();
        for (int i = num; i < args.length; ++i)
        {
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

}
