package de.marvinklar.customitemactions.commands;

import de.marvinklar.customitemactions.CustomItemAction;
import de.marvinklar.customitemactions.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomItemActionsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] arguments) {
        if(commandSender.hasPermission(Main.getMessageManager().getMessage("Edit_Permission"))) {
            if (arguments.length > 3) {
                if (arguments[1].equalsIgnoreCase("add") || arguments[1].equalsIgnoreCase("hinzufügen")) {
                    String name = arguments[0];
                    if(customItemActionExists(name, commandSender)) {
                        String stringToAdd = Main.getArgs(arguments, 3);

                        switch(arguments[2]) {
                            case "msg": case "msgs": case "message": case "messages":
                                Main.customItemActions.get(name).messages.add(stringToAdd);
                                Main.getInstance().getConfig().set("CustomItemActions." + name + ".Messages", getCustomItemAction(name).messages);
                                Main.getInstance().saveConfig();
                                commandSender.sendMessage(Main.getMessageManager().getMessage("Message_Added", "message", stringToAdd));
                                return true;
                            case "bc": case "bcs": case "broadcast": case "broadcasts":
                                Main.customItemActions.get(name).broadcasts.add(stringToAdd);
                                Main.getInstance().getConfig().set("CustomItemActions." + name + ".Broadcasts", getCustomItemAction(name).broadcasts);
                                Main.getInstance().saveConfig();
                                commandSender.sendMessage(Main.getMessageManager().getMessage("Broadcast_Added", "broadcast", stringToAdd));
                                return true;
                            case "cmd": case "cmds": case "command": case "commands":
                                stringToAdd = (stringToAdd.startsWith("/") ? stringToAdd : "/" + stringToAdd);
                                Main.customItemActions.get(name).commands.add(stringToAdd);
                                Main.getInstance().getConfig().set("CustomItemActions." + name + ".Commands", getCustomItemAction(name).commands);
                                Main.getInstance().saveConfig();
                                commandSender.sendMessage(Main.getMessageManager().getMessage("Command_Added", "command", stringToAdd));
                                return true;
                        }
                    }

                } else if(arguments[1].equalsIgnoreCase("remove") || arguments[1].equalsIgnoreCase("entfernen")) {
                    String name = arguments[0];
                    if(customItemActionExists(name, commandSender)) {
                        Integer id = 0;
                        try {
                            id = Integer.parseInt(arguments[3]);
                            if(id <= 0) {
                                throw new Exception();
                            }
                        } catch(Exception e) {
                            commandSender.sendMessage(Main.getMessageManager().getMessage("Invalid_ID", "id", arguments[3]));
                        }

                        Integer idToRemove = id - 1;
                        switch(arguments[2]) {
                            case "msg": case "msgs": case "message": case "messages":
                                if(getCustomItemAction(name).messages.size() >= id) {
                                    CustomItemAction customItemAction = Main.customItemActions.get(name);
                                    Main.customItemActions.remove(customItemAction.name);
                                    String stringToRemove = customItemAction.messages.get(idToRemove);
                                    customItemAction.messages.remove(stringToRemove);
                                    Main.customItemActions.put(customItemAction.name, customItemAction);
                                    Main.getInstance().getConfig().set("CustomItemActions." + name + ".Messages", customItemAction.messages);
                                    Main.getInstance().saveConfig();
                                    commandSender.sendMessage(Main.getMessageManager().getMessage("Message_Removed", "id", id));
                                } else {
                                    commandSender.sendMessage(Main.getMessageManager().getMessage("Message_Not_Found", "id", id));
                                }
                                break;
                            case "bc": case "bcs": case "broadcast": case "broadcasts":
                                if(getCustomItemAction(name).broadcasts.size() >= id) {
                                    CustomItemAction customItemAction = Main.customItemActions.get(name);
                                    Main.customItemActions.remove(customItemAction.name);
                                    String stringToRemove = customItemAction.broadcasts.get(idToRemove);
                                    customItemAction.broadcasts.remove(stringToRemove);
                                    Main.customItemActions.put(customItemAction.name, customItemAction);
                                    Main.getInstance().getConfig().set("CustomItemActions." + name + ".Broadcasts", customItemAction.broadcasts);
                                    Main.getInstance().saveConfig();
                                    commandSender.sendMessage(Main.getMessageManager().getMessage("Broadcast_Removed", "id", id));
                                } else {
                                    commandSender.sendMessage(Main.getMessageManager().getMessage("Broadcast_Not_Found", "id", id));
                                }
                                break;
                            case "cmd": case "cmds":case "command": case "commands":
                                if(getCustomItemAction(name).commands.size() >= id) {
                                    CustomItemAction customItemAction = Main.customItemActions.get(name);
                                    Main.customItemActions.remove(customItemAction.name);
                                    String stringToRemove = customItemAction.commands.get(idToRemove);
                                    customItemAction.commands.remove(stringToRemove);
                                    Main.customItemActions.put(customItemAction.name, customItemAction);
                                    Main.getInstance().getConfig().set("CustomItemActions." + name + ".Commands", customItemAction.commands);
                                    Main.getInstance().saveConfig();
                                    commandSender.sendMessage(Main.getMessageManager().getMessage("Command_Removed", "id", id));
                                } else {
                                    commandSender.sendMessage(Main.getMessageManager().getMessage("Command_Not_Found", "id", id));
                                }
                                break;
                        }
                    }
                    return true;
                }

            } else if (arguments.length > 2) {
                if (arguments[0].equalsIgnoreCase("give") || arguments[0].equalsIgnoreCase("geben")) {

                    String name = arguments[1];
                    if(customItemActionExists(name, commandSender)) {
                        Player target = Bukkit.getPlayer(arguments[2]);
                        if(target == null) {
                            commandSender.sendMessage(Main.getMessageManager().getMessage("Player_Not_Online", "player", name));
                        } else {
                            commandSender.sendMessage(Main.getMessageManager().getMessage("Gave_CustomItemAction", "name", name, "player", target.getName()));
                            target.getInventory().addItem(getCustomItemAction(name).item);
                        }
                    }
                    return true;
                }
            } else if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("create") || arguments[0].equalsIgnoreCase("erstellen")) {
                    if (commandSender instanceof Player) {
                        Player player = (Player) commandSender;
                        if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            commandSender.sendMessage(Main.getMessageManager().getMessage("Invalid_Item_In_Hand"));
                        } else {
                            if (getCustomItemAction(arguments[1]) == null) {
                                Main.createCustomItemAction(new CustomItemAction(arguments[1], player.getInventory().getItemInMainHand()));
                                commandSender.sendMessage(Main.getMessageManager().getMessage("Created_CustomItemAction", "name", arguments[1]));
                            } else {
                                commandSender.sendMessage(Main.getMessageManager().getMessage("Custom_Item_Action_Does_Already_Exist", "name", arguments[1]));
                            }
                        }
                    } else {
                        commandSender.sendMessage(Main.getMessageManager().getMessage("Only_For_Players"));
                    }
                    return true;
                }  else if (arguments[0].equalsIgnoreCase("delete") || arguments[0].equalsIgnoreCase("löschen")) {
                    if (customItemActionExists(arguments[1], commandSender)) {
                        Main.deleteCustomItemAction(arguments[1]);
                        commandSender.sendMessage(Main.getMessageManager().getMessage("Deleted_CustomItemAction", "name", arguments[1]));
                    }
                    return true;
                }

            } else if (arguments.length > 0 && (arguments[0].equalsIgnoreCase("list") || arguments[0].equalsIgnoreCase("liste"))) {
                if (Main.customItemActions.isEmpty()) {
                    commandSender.sendMessage(Main.getMessageManager().getMessage("No_CustomItemActions"));
                } else {
                    for(CustomItemAction customItemAction : Main.customItemActions.values()) {
                        for(String message : Main.getMessageManager().getList("List_CustomItemActions")) {
                            commandSender.sendMessage(Main.getMessageManager().replacePlaceholdersToParameters(message, "name", customItemAction.name, "item", customItemAction.item.getType().name(), "commands", String.join("\n", customItemAction.commands), "messages", String.join("\n", customItemAction.messages), "broadcasts", String.join("\n", customItemAction.broadcasts)));
                        }
                    }
                }
                return true;


            }

            for(String message : Main.getMessageManager().getList("Menu")) {
                commandSender.sendMessage(message);
            }
        } else {
            commandSender.sendMessage(Main.getMessageManager().getMessage("No_Permission"));
        }
        return true;
    }

    private CustomItemAction getCustomItemAction(String name) {
        for(CustomItemAction customItemAction : Main.customItemActions.values()) {
            if(customItemAction.name.equalsIgnoreCase(name)) {
                return customItemAction;
            }
        }
        return null;
    }

    private boolean customItemActionExists(String name, CommandSender commandSender) {
        if(getCustomItemAction(name) == null) {
            commandSender.sendMessage(Main.getMessageManager().getMessage("Custom_Item_Action_Does_Not_Exists", "name", name));
            return false;
        }
        return true;
    }

}
