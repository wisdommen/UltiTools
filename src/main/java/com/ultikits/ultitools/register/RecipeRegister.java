package com.ultikits.ultitools.register;

import com.ultikits.ultitools.config.ConfigController;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Set;

public class RecipeRegister {
    public static void initRecipe() {
        new BukkitRunnable() {
            @Override
            public void run() {
                YamlConfiguration config = ConfigController.getConfig("recipe");
                Set<String> recipes = config.getKeys(false);
                int count = 0;
                UltiTools.getInstance().getLogger().info(UltiTools.languageUtils.getString("custom_recipe_found_recipes").replace("%s", String.valueOf(recipes.size())));
                for(String name : recipes) {
                    String outputItemName = config.getString(name + ".output" + ".item");
                    int outputItemAmount = config.getInt(name + ".output" + ".amount");
                    List<Character> keys = config.getCharacterList(name + ".keys");
                    List<String> shape = config.getStringList(name + ".shape");
                    if (outputItemName == null || outputItemAmount == 0 || keys.isEmpty() || shape.isEmpty()) {
                        UltiTools.getInstance().getLogger().warning(UltiTools.languageUtils.getString("custom_recipe_format_error").replace("%s", name) + 1);
                        continue;
                    }
                    Material outputMaterial = Material.getMaterial(outputItemName);

                    if (outputMaterial == null) {
                        UltiTools.getInstance().getLogger().warning(UltiTools.languageUtils.getString("custom_recipe_item_not_found").replace("%s", name));
                        continue;
                    }
                    ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(UltiTools.getInstance(), "UltiRecipe_" + name), new ItemStack(outputMaterial));
                    if (shape.size() > 3) {
                        UltiTools.getInstance().getLogger().warning(UltiTools.languageUtils.getString("custom_recipe_row_must_be_three").replace("%s", name));
                        continue;
                    }
                    shapedRecipe.shape(shape.get(0), shape.get(1), shape.get(2));
                    boolean status = true;
                    for (char key : keys) {
                        String inputItemName = config.getString(name + ".ingredient." + key);
                        if (inputItemName == null) {
                            UltiTools.getInstance().getLogger().warning(UltiTools.languageUtils.getString("custom_recipe_format_error").replace("%s", name) + 2);
                            status = false;
                            break;
                        }
                        Material inputMaterial = Material.getMaterial(inputItemName);
                        if (inputMaterial == null) {
                            UltiTools.getInstance().getLogger().warning(UltiTools.languageUtils.getString("custom_recipe_item_not_found").replace("%s", name));
                            status = false;
                            break;
                        }
                        shapedRecipe.setIngredient(key, inputMaterial);
                    }
                    if (status) {
                        Bukkit.addRecipe(shapedRecipe);
                        count ++;
                    }
                }
                UltiTools.getInstance().getLogger().info(UltiTools.languageUtils.getString("custom_recipe_loaded_successfully").replace("%s", String.valueOf(count)));
            }
        }.run();
    }
}
