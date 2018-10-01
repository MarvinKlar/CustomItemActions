package de.marvinklar.customitemactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageManager {
    private final HashMap<String, List<String>> hmpLists = new HashMap<>();
    private final HashMap<String, String> hmpMessages = new HashMap<>();
    private final String strPrefix;

    public MessageManager(final Configuration filConfig) {
		for (final String strKey : filConfig.getConfigurationSection("Messages").getKeys(false)) {
			if (filConfig.getStringList("Messages." + strKey).isEmpty()) {
				addMessage(strKey, filConfig.getString("Messages." + strKey));
			} else {
				addList(strKey, filConfig.getStringList("Messages." + strKey));
			}
		}

		this.strPrefix = ChatColor.translateAlternateColorCodes('&', getMessage("Prefix"));
    }

    public String getMessage(String strKey, final Object... objParameters) {
		strKey = strKey.toLowerCase();
		if (!hmpMessages.containsKey(strKey)) {
			throw new NullPointerException("The message '" + strKey + "' does not exist in the configuration.");
		}
		return replacePlaceholdersToParameters(hmpMessages.get(strKey), objParameters);
    }

    public ArrayList<String> getList(String strKey, final Object... objParameters) {
		strKey = strKey.toLowerCase();
		if (!hmpLists.containsKey(strKey)) {
			throw new NullPointerException("The list '" + strKey + "' does not exist in the configuration.");
		}
		final ArrayList<String> listToReturn = new ArrayList<>();
		for (final String strMessage : hmpLists.get(strKey)) {
			listToReturn.add(replacePlaceholdersToParameters(strMessage, objParameters));
		}
		return listToReturn;
    }

    public void addMessage(final String strKey, final String strMessage) {
		hmpMessages.put(strKey.toLowerCase(), ChatColor.translateAlternateColorCodes('&', strMessage));
    }

    public void addList(final String strKey, final List<String> lstMessages) {
		final List<String> lstConvertedMessages = new ArrayList<>();
		for (final String strMessage : lstMessages) {
			lstConvertedMessages.add(ChatColor.translateAlternateColorCodes('&', strMessage));
		}
		hmpLists.put(strKey.toLowerCase(), lstConvertedMessages);
    }

    public String replacePlaceholdersToParameters(String strMessage, final Object... objParameters) {
		for (int i = 0; i < objParameters.length - 1; i = i + 2) {
			if (objParameters[i] != null) {
			strMessage = strMessage.replace("%" + objParameters[i] + "%",
				objParameters[i + 1] == null ? "" : objParameters[i + 1].toString());
			}
		}
		return strMessage.replace("%prefix%", getPrefix());
    }

    public String getPrefix() {
	return strPrefix == null ? "" : strPrefix;
    }
}
