package me.trqhxrd.grapesrpg.game.objects.recipe.stone;

import me.trqhxrd.grapesrpg.api.objects.recipe.GrapesShapedRecipe;
import me.trqhxrd.grapesrpg.game.objects.item.PlantFiber;
import me.trqhxrd.grapesrpg.game.objects.item.stone.StoneSword;
import org.bukkit.Material;

public class StoneSwordRecipe extends GrapesShapedRecipe {
    public StoneSwordRecipe() {
        super(new StoneSword());
        super.setShape("cac", "cac", "cbc");
        super.setIngredient('a', Material.COBBLESTONE);
        super.setIngredient('b',Material.STICK);
        super.addBinding(12,new PlantFiber());
    }
}
