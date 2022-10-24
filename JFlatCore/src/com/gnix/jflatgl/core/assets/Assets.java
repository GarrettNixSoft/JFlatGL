package com.gnix.jflatgl.core.assets;

import java.util.HashMap;
import java.util.Map;

public class Assets {

	private static Map<Class<?>, Map<String, Object>> assets;

	public Assets() {
		assets = new HashMap<>();
	}

	public void addAsset(String id, Object asset) {

		// fetch the map to store the asset in
		Map<String, Object> assetStore = assets.computeIfAbsent(asset.getClass(), c -> new HashMap<>());

		// check for duplicate IDs
		if (assetStore.containsKey(id)) throw new RuntimeException("Duplicate asset ID: " + id + " (type: " + asset.getClass().getName() + ")");

		// store the asset
		assetStore.put(id, asset);

	}

	public Object getAssetByID(Class<?> type, String id) {

		// get the map it should belong to
		Map<String, Object> assetStore = assets.get(type);

		// if the map does not exist, throw an exception
		if (assetStore == null) throw new RuntimeException("No assets exist with type " + type.getName());

		// retrieve the asset object
		Object asset = assetStore.get(id);

		// if the asset does not exist, throw an exception
		if (asset == null) throw new RuntimeException("No assets exist with id " + id + ", type " + type.getName());

		// otherwise, return the asset
		return assets.get(type).get(id);

	}

}
