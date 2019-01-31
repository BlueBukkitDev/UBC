package blue.dev.ubc;

public class BlockRecipe {
	private BlockIngredient ingredient;
	private BlockResult result;
	
	/**
	 *Creates a new BlockRecipe, which is a recipe used in creating compressed items from non-compressed items. The BlockIngredient is the ingredient required, and the BlockResult is the item produced. 
	 **/
	public BlockRecipe(BlockIngredient ingredient, BlockResult result) {
		this.ingredient = ingredient;
		this.result = result;
	}
	/**
	 *Returns the BlockIngredient that constitutes half of this BlockRecipe.
	 **/
	public BlockIngredient getIngredient() {
		return ingredient;
	}
	/**
	 *Sets the BlockIngredient that constitutes half of this BlockRecipe. This is only used when changing the ingredient based on preferred settings. 
	 **/
	public void setIngredient(BlockIngredient ingredient) {
		this.ingredient = ingredient;
	}
	/**
	 *Returns the BlockResult that constitutes half of this BlockRecipe.
	 **/
	public BlockResult getResult() {
		return result;
	}
	/**
	 *Sets the BlockResult that constitutes half of this BlockRecipe. This is only used when changing the result based on preferred settings. 
	 **/
	public void setResult(BlockResult result) {
		this.result = result;
	}
}
