package com.shfloop.BlockSwitcher;

import dev.crmodders.cosmicquilt.api.entrypoint.ModInitializer;
import org.quiltmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockSwitcherQuilt implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Block Switcher QUILT");

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Block Switcher QUILT Initialized!");
	}
}
