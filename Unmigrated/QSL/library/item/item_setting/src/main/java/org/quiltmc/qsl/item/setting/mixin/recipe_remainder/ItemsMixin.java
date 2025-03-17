/*
 * Copyright 2024 The Quilt Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.item.setting.mixin.recipe_remainder;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import org.quiltmc.qsl.item.setting.api.RecipeRemainderLocation;

@Mixin(Items.class)
public class ItemsMixin {
	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/Item$Settings;recipeRemainder(Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item$Settings;"
		),
		slice = @Slice(
			from = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;BEETROOT_SOUP:Lnet/minecraft/item/Item;", opcode = Opcodes.PUTSTATIC),
			to = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;DRAGON_BREATH:Lnet/minecraft/item/Item;", opcode = Opcodes.PUTSTATIC)
		)
	)
	private static Item.Settings changeDragonBreathRecipeRemainder(Item.Settings instance, Item recipeRemainder) {
		// See: https://github.com/FabricMC/fabric/issues/2873
		//      https://bugs.mojang.com/browse/MC-259583
		return new QuiltItemSettings()
			.recipeRemainder((_original, _recipe) -> recipeRemainder.getDefaultStack())
			.recipeRemainder((original, recipe) -> original.getCount() >= 2 ? recipeRemainder.getDefaultStack() : ItemStack.EMPTY, RecipeRemainderLocation.POTION_ADDITION);
	}
}
