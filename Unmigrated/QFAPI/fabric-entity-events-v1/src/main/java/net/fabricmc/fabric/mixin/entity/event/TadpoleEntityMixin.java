/*
 * Copyright 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.mixin.entity.event;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.TadpoleEntity;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

@Mixin(TadpoleEntity.class)
abstract class TadpoleEntityMixin extends FishEntity {
	TadpoleEntityMixin(EntityType<? extends FishEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyArg(
			method = "growUp",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V")
	)
	private Entity afterGrowingUpToFrog(Entity converted) {
		ServerLivingEntityEvents.MOB_CONVERSION.invoker().onConversion(this, (MobEntity) converted, false);
		return converted;
	}
}
