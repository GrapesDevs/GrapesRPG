package me.trqhxrd.grapesrpg.api.objects.recipe;

import me.trqhxrd.grapesrpg.Grapes;
import me.trqhxrd.grapesrpg.api.attribute.Serializable;
import me.trqhxrd.grapesrpg.api.objects.item.GrapesItem;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a shaped Recipe.
 * Shaped means, that correctness of the recipe depends on the shape of the items in the crafting grid.
 *
 * @author Trqhxrd
 */
public class GrapesShapedRecipe extends GrapesRecipe implements Serializable<GrapesShapedRecipe> {

    /**
     * This array stores the shape of the recipe.
     * Characters can be used twice.
     * The array should have a length of 9.
     * The actual ingredient for every character is noted in {@link GrapesShapedRecipe#ingredients}.
     */
    private char[] shape;

    /**
     * A map of all ingredients and their character in the {@link GrapesShapedRecipe#shape}.
     */
    private Map<Character, GrapesRecipeChoice> ingredients;

    /**
     * Creates a new Recipe for the item result.
     *
     * @param result The item for which you want to get a recipe.
     */
    public GrapesShapedRecipe(ItemStack result) {
        super(result);
        this.ingredients = new HashMap<>();
    }

    /**
     * This method sets a certain character in the set to be a certain type of item.
     *
     * @param key         The character for which you want to set the valid ingredients.
     * @param ingredients All things you can put in the slot to make the crafting recipe valid.
     * @return The Object from which it was executed. This makes it possible to chain commands.
     */
    public GrapesShapedRecipe setIngredient(char key, GrapesItem... ingredients) {
        this.ingredients.put(key, new GrapesRecipeChoice(ingredients));
        return this;
    }

    /**
     * This method sets a certain character in the set to be a certain type of item.
     *
     * @param key         The character for which you want to set the valid ingredients.
     * @param ingredients All things you can put in the slot to make the crafting recipe valid.
     * @return The Object from which it was executed. This makes it possible to chain commands.
     */
    public GrapesShapedRecipe setIngredient(char key, Material... ingredients) {
        this.ingredients.put(key, new GrapesRecipeChoice(ingredients));
        return this;
    }

    /**
     * This method sets a certain character in the set to be a certain type of item.
     *
     * @param key         The character for which you want to set the valid ingredients.
     * @param ingredients All things you can put in the slot to make the crafting recipe valid.
     * @return The Object from which it was executed. This makes it possible to chain commands.
     */
    public GrapesShapedRecipe setIngredient(char key, ItemStack... ingredients) {
        this.ingredients.put(key, new GrapesRecipeChoice(ingredients));
        return this;
    }

    /**
     * This method returns an array of characters, which represents the recipes shape.
     *
     * @return The shape of the recipe.
     */
    public char[] getShape() {
        return shape;
    }

    /**
     * This method allows you to set the shape for the recipe.
     * The first three characters are the top row of the crafting recipe.
     * The second three characters are the middle row of the crafting recipe.
     * The third three characters are the bottom row of the crafting recipe.
     *
     * @param shape The chars, which you want to set as a shape.
     * @return The Object from which it was executed. This makes it possible to chain commands.
     */
    public GrapesShapedRecipe setShape(char... shape) {
        this.shape = ArrayUtils.subarray(shape, 0, 9);
        return this;
    }

    /**
     * This method allows you to set the shape for the recipe.
     * The first string are the top row of the crafting recipe.
     * The second string are the middle row of the crafting recipe.
     * The third string are the bottom row of the crafting recipe.
     *
     * @param shape The lines, which you want to set as a shape.
     * @return The Object from which it was executed. This makes it possible to chain commands.
     */
    public GrapesShapedRecipe setShape(String... shape) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 3; i++) if (shape[0] != null) s.append(shape[i], 0, Math.min(3, shape[i].length() - 1));
        return this.setShape(s.toString().toCharArray());
    }

    /**
     * This method returns a map of keys and ingredients.
     * The keys are the chars from the shape and the GrapesRecipeChoice contains all valid ingredients, which you can use.
     *
     * @return A Map of {@link Character}s and {@link GrapesRecipeChoice}s.
     */
    public Map<Character, GrapesRecipeChoice> getIngredients() {
        return ingredients;
    }

    /**
     * This method completely overwrites the Map of ingredients.
     *
     * @param ingredients The new map of ingredients, which you want to set.
     * @return The Object from which it was executed. This makes it possible to chain commands.
     */
    public GrapesShapedRecipe setIngredients(Map<Character, GrapesRecipeChoice> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    /**
     * This method checks, if the matrix, which is given in the parameters, is valid for the recipe.
     * Most commonly you get the matrix from <code>((CraftingInventory) inv).getMatrix()</code>
     *
     * @param matrix An array of items with the length of 9. Index 0 is the top left corner. Index 1 is the middle of the top row and so on.
     * @return A Boolean. If it's true, the matrix is valid.
     */
    @Override
    public boolean check(ItemStack[] matrix) {
        boolean valid = true;
        GrapesRecipeChoice[] choices = this.getChoices();

        for (int i = 0; i < choices.length; i++) {
            if ((choices[i] != null || matrix[i] != null) && (choices[i] == null || matrix[i] == null)) {
                valid = false;
                break;
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            ItemStack item = matrix[i];
            GrapesRecipeChoice choice = choices[i];
            if ((choice == null && item != null) || (choice != null && item == null)) {
                valid = false;
                continue;
            }
            if (choice == null) continue;
            if (!choice.check(item)) valid = false;
        }

        return valid;
    }

    /**
     * This method returns an Array of {@link GrapesRecipeChoice}s.
     * You can use this method, if you want to check the recipe manually.
     * Index 0 is the top left slot, index 1 is the top middle slot and so on.
     *
     * @return An array of {@link GrapesRecipeChoice}s.
     */
    public GrapesRecipeChoice[] getChoices() {
        GrapesRecipeChoice[] out = new GrapesRecipeChoice[shape.length];
        for (int i = 0; i < shape.length; i++)
            try {
                out[i] = ingredients.get(shape[i]);
            } catch (Exception e) {
                out[i] = null;
            }
        return out;
    }

    /**
     * This method serializes an Object (t) into a String.
     *
     * @param grapesShapedRecipe The Object, which you want to serialize.
     * @return The String containing all data about the object.
     */
    @Override
    public String serialize(GrapesShapedRecipe grapesShapedRecipe) {
        return Grapes.GSON.toJson(grapesShapedRecipe);
    }

    /**
     * This method serializes the Object, from which it will be executed.
     *
     * @return The serialized object.
     */
    @Override
    public String serialize() {
        return this.serialize(this);
    }

    /**
     * This method is able to create an object from a serialized String.
     *
     * @param s The String you want to deserialize.
     * @return The Object.
     */
    @Override
    public GrapesShapedRecipe deserialize(String s) {
        return Grapes.GSON.fromJson(s, GrapesShapedRecipe.class);
    }
}
