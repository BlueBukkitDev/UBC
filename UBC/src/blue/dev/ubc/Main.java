package blue.dev.ubc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main plugin;
	private Blockifier b;

	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		if (getConfig().getBoolean("Uncraftable")) {
			Recipes r = new Recipes();
			r.mushroom();
			r.mushroom2();
			r.netherwart();
		}
		new ConfigValues().load();
		b = new Blockifier();
		Bukkit.getConsoleSender().sendMessage("§aUBC has been successfully enabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String Commandlabel, String[] args) {
		if ((sender instanceof Player)) {
			Player p = (Player) sender;
			if ((p.hasPermission("ubc.use")) || (p.isOp())) {
				if (cmd.getName().equalsIgnoreCase("block")) {
					if (args.length == 0) {
						b.added = 0;
						b.removed = 0;
						int added = 0, removed = 0;
						boolean didBlockify = false;
						int res = BlockificationResult.NOT_ENOUGH_ITEMS;
						boolean wasRestrained = false;
						for(int i = 0; i < ConfigValues.recipes.size(); i++) {//Iterates through the recipes and compacts each one through the inventory. 
							if(p.getInventory().containsAtLeast(ConfigValues.recipes.get(i).getIngredient().getItemStack(), 1)){
								res = b.blockify(p, ConfigValues.recipes.get(i).getIngredient().getItemStack(), true);
								if(res == BlockificationResult.SUCCESS) {
									didBlockify = true;
								}else if(res == BlockificationResult.NOT_ENOUGH_ROOM) {
									break;
								}else if(res == BlockificationResult.NO_PERMISSION) {
									wasRestrained = true;
								}
								added += b.added;
								removed += b.removed;
							}
						}
						if(wasRestrained) {
							p.sendMessage("§cCompression of some of your items failed due to lack of permissions.");
						}
						if(res == BlockificationResult.NOT_ENOUGH_ITEMS) {
							if(!didBlockify) {
								p.sendMessage("§cYou don't have enough items to compress!");
								return true;
							}
						}
						if(res == BlockificationResult.NO_COMPRESSABLE_ITEMS) {
							p.sendMessage("§cYou don't have any compressable items!");
						}
						if(didBlockify) {
							if (Main.plugin.getConfig().getBoolean("UseSounds")) {
								p.playSound(p.getLocation(), Sound.valueOf(Main.plugin.getConfig().getString("Sound-Name")), 0.4F, 1.0F);
							}
							if(Main.plugin.getConfig().getBoolean("UseMessages")) {
								String message = Main.plugin.getConfig().getString("ConfirmMessage");
								if(message.contains("%items%")) {
									message = message.replaceAll("%items%", ""+removed);
								}
								if(message.contains("%blocks%")) {
									message = message.replaceAll("%blocks%", ""+added);
								}
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
								
							}
						}
					}
				}
			} else p.sendMessage("§cYou do not have permission to use this command!");
		}
		if (((args.length == 1) && args[0].equalsIgnoreCase("reload")) && (sender.hasPermission("ubc.admin") || sender.isOp())) {
			sender.sendMessage("§aBlock Command config has been reloaded!");
			reloadConfig();
			if(sender instanceof Player) {
				Bukkit.getConsoleSender().sendMessage("§aBlock Command config has been reloaded!");
			}
		}
		return true;
	}
}
