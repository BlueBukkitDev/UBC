package blue.dev.ubc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class ConfigValues {
	public static List<BlockRecipe> recipes = new ArrayList<BlockRecipe>();
	
	public void load() {
		for(String each:Main.plugin.getConfig().getStringList("Enabled-Items")) {
			String[] halves = each.split("=");
			String[] recipe = halves[0].split("-");
			String[] result = halves[1].split("-");
			
			if(Material.getMaterial(recipe[0]) != null) {
				if(Material.getMaterial(result[0]) != null) {
					BlockIngredient bi = new BlockIngredient(Material.getMaterial(recipe[0]), Integer.parseInt(recipe[1]), Integer.parseInt(recipe[2]));
					BlockResult br = new BlockResult(Material.getMaterial(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2]));
					BlockRecipe brec = new BlockRecipe(bi, br);
					
					recipes.add(brec);
				}else Bukkit.getConsoleSender().sendMessage("§cERROR: §7Material \""+result[0]+"\" is not a valid material!");
			}else Bukkit.getConsoleSender().sendMessage("§cERROR: §7Material \""+recipe[0]+"\" is not a valid material!");
		}
	}
}
