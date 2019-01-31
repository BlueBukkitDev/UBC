package blue.dev.ubc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Recipes
{
  public void mushroom()
  {
    ItemStack result = new ItemStack(Material.RED_MUSHROOM, 4);
    
    @SuppressWarnings("deprecation")
	ShapedRecipe recipe = new ShapedRecipe(result);
    
    recipe.shape(new String[] {"*"});
    recipe.setIngredient('*', Material.RED_MUSHROOM_BLOCK);
    Bukkit.getServer().addRecipe(recipe);
  }
  
  public void mushroom2()
  {
    ItemStack result = new ItemStack(Material.BROWN_MUSHROOM, 4);
    
    @SuppressWarnings("deprecation")
	ShapedRecipe recipe = new ShapedRecipe(result);
    
    recipe.shape(new String[] {"*"});
    recipe.setIngredient('*', Material.BROWN_MUSHROOM_BLOCK);
    Bukkit.getServer().addRecipe(recipe);
  }
  
  public void netherwart()
  {
    ItemStack result = new ItemStack(Material.NETHER_WART, 9);
    
    @SuppressWarnings("deprecation")
	ShapedRecipe recipe = new ShapedRecipe(result);
    
    recipe.shape(new String[] {"*"});
    recipe.setIngredient('*', Material.NETHER_WART_BLOCK);
    Bukkit.getServer().addRecipe(recipe);
  }
}
