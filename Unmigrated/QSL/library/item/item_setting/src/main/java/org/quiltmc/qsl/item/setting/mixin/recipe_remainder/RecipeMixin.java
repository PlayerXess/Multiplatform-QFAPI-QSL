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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;

import org.quiltmc.qsl.item.setting.api.RecipeRemainderLocation;
import org.quiltmc.qsl.item.setting.api.RecipeRemainderProvider;

@Mixin(Recipe.class)
public interface RecipeMixin {
	@Inject(method = "getRemainder", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
	private <C extends Inventory> void interceptGetRemainingStacks(C inventory, CallbackInfoReturnable<DefaultedList<ItemStack>> cir) {
		cir.setReturnValue(
			RecipeRemainderProvider.getRemainingStacks(inventory, (Recipe<?>) this, RecipeRemainderLocation.CRAFTING, cir.getReturnValue())
		);
	}
}
