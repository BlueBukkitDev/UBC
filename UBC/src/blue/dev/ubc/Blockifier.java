package blue.dev.ubc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Blockifier {
	
	public int removed, added;
	
	public byte blockify(Player p, ItemStack stack, boolean all) {
		if(isEnabled(stack)) {
			if(Main.plugin.getConfig().getBoolean("UsePermissions")) {
				if(!p.hasPermission("ubc.use."+stack.getType().toString())) {
					return BlockificationResult.NO_PERMISSION;
				}
			}
			BlockRecipe recipe = getRecipe(stack);
			int slots = emptySlots(p);
			int qOwned = amountOfItem(p, stack);//How many items are in the inventory
			int qReq = recipe.getIngredient().getQuantity();//How many items are needed per iteration
			int qDebted = 0;
			int slotsByResults = (int) (((double)qOwned/(double)qReq)/(double)recipe.getResult().getItemStack().getMaxStackSize()) + 
				(int) Math.ceil(((((double)qOwned/(double)qReq)%(double)recipe.getResult().getItemStack().getMaxStackSize())/(double)recipe.getResult().getItemStack().getMaxStackSize()));
			int slotsByRemains = (int) Math.ceil((double)((double)qOwned%(double)qReq)/(double)recipe.getIngredient().getItemStack().getMaxStackSize());
			int totalSlotsTaken = slotsByResults+slotsByRemains;
			
			slots += (qOwned/recipe.getIngredient().getItemStack().getMaxStackSize());
			slots -= totalSlotsTaken;
			
			while(qOwned >= qReq) {
				qOwned -= qReq;
				qDebted += recipe.getResult().getQuantity();
			}
			qOwned = amountOfItem(p, stack);////////////////////Why a second instance? //Because we used it to iterate. Replace with math and no more re-instanciation. 
			//This handles the certainty that the player will have room in his inventory to use his /block
			int req = 0;
			if(qDebted*recipe.getIngredient().getQuantity() < qOwned) {//Has leftovers, needs one empty slot
				req++;
				int stacks = qDebted/recipe.getResult().getItemStack().getMaxStackSize();
				req += stacks;
			}
			int usableReq = req;
			
			int finalSlots = slots;
			if(slots < req) {
				Bukkit.getScheduler().runTaskLater(Main.plugin, new Runnable() {
					@Override
					public void run() {
						TextComponent tc = new TextComponent("Not enough room in your inventory to compress your "+stack.getType().toString().toLowerCase()+" items!");
						tc.setColor(ChatColor.RED);//Using setChatColor prevents color from resetting per line. Useful stuff. 
						tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Empty Slots: §a"+finalSlots+"\n§7Required Slots: §c"+usableReq).create()));
						p.spigot().sendMessage(tc);
					}
				}, 1);
				return BlockificationResult.NOT_ENOUGH_ROOM;
			}
			////////////////////////////////////////////////////////////////////////////////////////////////
			if(qDebted >= 1) {
				removed = removeFromInventory(p, stack, qDebted*recipe.getIngredient().getQuantity());
				added = addToInventory(p, recipe, qDebted*recipe.getResult().getQuantity());
				if(!all) {//Keeps from chiming every single time. This is only used if it is going to specifically do one stack or specific stacks... I think I could get rid of it tbh. 
					if (Main.plugin.getConfig().getBoolean("UseSounds")) {
						p.playSound(p.getLocation(), Sound.valueOf(Main.plugin.getConfig().getString("Sound-Name")), 0.4F, 1.0F);
						if(Main.plugin.getConfig().getBoolean("UseMessages")) {
							for(String each:Main.plugin.getConfig().getStringList("Messages")) {
								if(each.contains("%items%")) {
									each.replaceAll("%items%", ""+removed);
								}
								if(each.contains("%blocks%")) {
									each.replaceAll("%blocks%", ""+added);
								}
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', each));
							}
						}
					}
				}
				return BlockificationResult.SUCCESS;
			}else return BlockificationResult.NOT_ENOUGH_ITEMS;
		}
		return BlockificationResult.NO_COMPRESSABLE_ITEMS;
	}

	public boolean isEnabled(ItemStack stack) {
		for(BlockRecipe each:ConfigValues.recipes) {
			Material m = each.getIngredient().getMaterial();
			if(m == stack.getType()) {
				return true;
			}
		}
		Bukkit.getConsoleSender().sendMessage("§cERROR: §7"+stack.getType().toString());
		return false;
	}
	
	public BlockRecipe getRecipe(ItemStack stack) {
		for(BlockRecipe each:ConfigValues.recipes) {
			Material m = each.getIngredient().getMaterial();
			if(m == stack.getType()) {
				return each;
			}
		}
		return null;
	}

	public int amountOfItem(Player p, ItemStack stack) {
		int total = 0;
		for (int i = 0; i < 36; i++) {
			if ((p.getInventory().getItem(i) != null) && (p.getInventory().getItem(i).getType() == stack.getType())
					&& (p.getInventory().getItem(i).getDurability() == stack.getDurability())) {
				total += p.getInventory().getItem(i).getAmount();
			}
		}
		return total;
	}
	
	public int addToInventory(Player p, BlockRecipe recipe, int amount) {
		int total = 0;
		int qDebted = amount;
		while (qDebted >= recipe.getResult().getItemStack().getMaxStackSize()) {
			qDebted -= recipe.getResult().getItemStack().getMaxStackSize();
			p.getInventory().addItem(new ItemStack(recipe.getResult().getMaterial(), recipe.getResult().getItemStack().getMaxStackSize(), (short)recipe.getResult().getData()));//Adding?
			total += recipe.getResult().getItemStack().getMaxStackSize();
		}
		int toAdd = 0;
		while(qDebted > 0) {//Second while loop because we are adding full stacks first, now we add the partial stack. 
			qDebted--;
			toAdd++;
			total++;
		}
		p.getInventory().addItem(new ItemStack(recipe.getResult().getItemStack().getType(), toAdd*recipe.getResult().getItemStack().getAmount()));
		return total;
	}

	public int removeFromInventory(Player p, ItemStack stack, int amount) {
		int total = 0;
		for (int i = 0; i < 36; i++) {
			ItemStack current = p.getInventory().getItem(i);
			if ((current != null) && (current.isSimilar(stack))) {
				if (amount >= current.getAmount()) {
					amount -= current.getAmount();
					p.getInventory().removeItem(current);
					total +=  current.getAmount();
				} else {
					current.setAmount(current.getAmount() - amount);//This happens when you run out, right?
					total += amount;
					break;
				}
			}
		}
		return total;//Should remove full amount
	}
	
	public int emptySlots(Player p) {
		int count = 0;
		for(int i = 0; i < 36; i++) {
			if(p.getInventory().getItem(i) == null) {
				count++;
				continue;
			}else if(p.getInventory().getItem(i).getType() == Material.AIR) {
				count++;
				continue;
			}
		}
		return count;
	}
}