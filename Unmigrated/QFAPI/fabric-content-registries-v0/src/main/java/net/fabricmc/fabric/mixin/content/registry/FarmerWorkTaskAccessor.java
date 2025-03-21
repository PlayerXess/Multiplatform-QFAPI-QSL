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

package net.fabricmc.fabric.mixin.content.registry;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.ai.brain.task.FarmerWorkTask;
import net.minecraft.item.Item;

@Mixin(FarmerWorkTask.class)
public interface FarmerWorkTaskAccessor {
	@Mutable
	@Accessor("COMPOSTABLES")
	static void fabric_setCompostables(List<Item> items) {
		throw new AssertionError("Untransformed @Accessor");
	}

	@Accessor("COMPOSTABLES")
	static List<Item> fabric_getCompostable() {
		throw new AssertionError("Untransformed @Accessor");
	}
}
