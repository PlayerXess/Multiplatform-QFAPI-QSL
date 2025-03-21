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

package net.fabricmc.fabric.api.client.rendereregistry.v1;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

/**
 * Helper class for registering BlockEntityRenderers.
 *
 * @deprecated This module has been moved into fabric-rendering-v1. Use {@link net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry} instead
 */
@Deprecated
public interface BlockEntityRendererRegistry {
	BlockEntityRendererRegistry INSTANCE = new BlockEntityRendererRegistry() {
		@Override
		public <E extends BlockEntity> void register(BlockEntityType<E> blockEntityType, BlockEntityRendererFactory<? super E> blockEntityRendererFactory) {
			net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry.register(blockEntityType, blockEntityRendererFactory);
		}
	};

	/**
	 * Register a BlockEntityRenderer for a BlockEntityType. Can be called clientside before the world is rendered.
	 *
	 * @param blockEntityType the {@link BlockEntityType} to register a renderer for
	 * @param blockEntityRendererFactory a {@link BlockEntityRendererFactory} that creates a {@link BlockEntityRenderer}, called
	 *                            when {@link BlockEntityRenderDispatcher} is initialized or immediately if the dispatcher
	 *                            class is already loaded
	 * @param <E> the {@link BlockEntity}
	 */
	<E extends BlockEntity> void register(BlockEntityType<E> blockEntityType, BlockEntityRendererFactory<? super E> blockEntityRendererFactory);
}
