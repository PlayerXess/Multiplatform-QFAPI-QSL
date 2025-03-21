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

package net.fabricmc.fabric.impl.client.container;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.ContainerScreenFactory;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.impl.container.ContainerProviderImpl;

public class ScreenProviderRegistryImpl implements ScreenProviderRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScreenProviderRegistryImpl.class);

	private static final Map<Identifier, ContainerFactory<HandledScreen>> FACTORIES = new HashMap<>();

	@Override
	public void registerFactory(Identifier identifier, ContainerFactory<HandledScreen> factory) {
		if (FACTORIES.containsKey(identifier)) {
			throw new RuntimeException("A factory has already been registered as " + identifier + "!");
		}

		FACTORIES.put(identifier, factory);
	}

	@Override
	public <C extends ScreenHandler> void registerFactory(Identifier identifier, ContainerScreenFactory<C> containerScreenFactory) {
		registerFactory(identifier, (syncId, identifier1, player, buf) -> {
			C container = ((ContainerProviderImpl) ContainerProviderRegistry.INSTANCE).createContainer(syncId, identifier1, player, buf);

			if (container == null) {
				LOGGER.error("Could not open container for {} - a null object was created!", identifier1.toString());
				return null;
			}

			return containerScreenFactory.create(container);
		});
	}

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(ContainerProviderImpl.OPEN_CONTAINER, (client, handler, buf, responseSender) -> {
			Identifier identifier = buf.readIdentifier();
			int syncId = buf.readUnsignedByte();

			// Retain the buf since we must open the screen handler with it's extra modded data on the client thread
			buf.retain();

			client.execute(() -> {
				try {
					ContainerFactory<HandledScreen> factory = FACTORIES.get(identifier);

					if (factory == null) {
						LOGGER.error("No GUI factory found for {}!", identifier.toString());
						return;
					}

					ClientPlayerEntity player = client.player;
					HandledScreen<?> gui = factory.create(syncId, identifier, player, buf);

					player.currentScreenHandler = gui.getScreenHandler();
					client.setScreen(gui);
				} finally {
					buf.release();
				}
			});
		});
	}
}
