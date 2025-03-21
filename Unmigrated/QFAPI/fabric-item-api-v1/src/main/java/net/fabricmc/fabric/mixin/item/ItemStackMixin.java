/*
 * Copyright 2016, 2017, 2018, 2019 FabricMC
 * Copyright 2022 The Quilt Project
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

package net.fabricmc.fabric.mixin.item;

import java.util.function.Consumer;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements FabricItemStack {
	@Shadow public abstract Item getItem();

	@Unique
	private Consumer<LivingEntity> fabric_breakCallback;

	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("HEAD"))
	private void saveDamager(int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, CallbackInfo ci) {
		this.fabric_breakCallback = breakCallback;
	}

	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At("RETURN"))
	private <T extends LivingEntity> void clearDamage(int amount, T entity, Consumer<T> breakCallback, CallbackInfo ci) {
		this.fabric_breakCallback = null;
	}

	@Redirect(
			method = "getAttributeModifiers",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/Item;getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;"
			)
	)
	public Multimap<EntityAttribute, EntityAttributeModifier> hookGetAttributeModifiers(Item item, EquipmentSlot slot) {
		ItemStack stack = (ItemStack) (Object) this;
		//we need to ensure it is modifiable for the callback, use linked map to preserve ordering
		Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers = LinkedHashMultimap.create(item.getAttributeModifiers(stack, slot));
		ModifyItemAttributeModifiersCallback.EVENT.invoker().modifyAttributeModifiers(stack, slot, attributeModifiers);
		return attributeModifiers;
	}

	@Redirect(
			method = "isSuitableFor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/Item;isSuitableFor(Lnet/minecraft/block/BlockState;)Z"
			)
	)
	public boolean hookIsSuitableFor(Item item, BlockState state) {
		return item.isSuitableFor((ItemStack) (Object) this, state);
	}
}
