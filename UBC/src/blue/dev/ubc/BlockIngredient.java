package blue.dev.ubc;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockIngredient {
	private Material material;
	private int data, quantity;
	/**
	 *Takes in a Material, an int data, and an int quantity. This is basically a customizable ItemStack used to build a BlockRecipe. 
	 **/
	public BlockIngredient(Material material, int data, int quantity) {
		this.material = material;
		this.data = data;
		this.quantity = quantity;
	}
	
	public ItemStack getItemStack() {
		return new ItemStack(material, quantity, (short)data);
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}
	/**
	 *Returns how many items will be taken when the blockification takes place.
	 **/
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
