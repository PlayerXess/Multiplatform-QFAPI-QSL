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

package net.fabricmc.fabric.api.transfer.v1.storage.base;

import org.jetbrains.annotations.ApiStatus;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

/**
 * A {@link Storage} that supports extraction, and not insertion.
 *
 * <p><b>Experimental feature</b>, we reserve the right to remove or change it without further notice.
 * The transfer API is a complex addition, and we want to be able to correct possible design mistakes.
 */
@ApiStatus.Experimental
public interface ExtractionOnlyStorage<T> extends Storage<T> {
	@Override
	default boolean supportsInsertion() {
		return false;
	}

	@Override
	default long insert(T resource, long maxAmount, TransactionContext transaction) {
		return 0;
	}
}
