package com.gnix.jflatgl.extension;

import java.util.Optional;

/**
 * An EngineExtension allows for additional components to be inserted
 * into the engine, such as the provided GUI extension.
 * <br>
 * An EngineExtension can possess an UpdateExtension and a RenderExtension,
 * which are both functional interface types which will be called once
 * per execution of their respective types (once per tick for UpdateExtension,
 * once per frame for RenderExtension).
 * <br>
 * NOTE: While all EngineExtensions will be called last in the engine tick/render
 * processes, there is no guarantee regarding the order of execution when multiple
 * extensions are present. It is advised not to design extensions to depend on
 * other extensions in terms of execution order.
 */
public abstract class EngineExtension {

	private final Optional<InitExtension> initExtension;
	private final Optional<UpdateExtension> updateExtension;
	private final Optional<RenderExtension> renderExtension;

	public EngineExtension(InitExtension initExtension, UpdateExtension updateExtension, RenderExtension renderExtension) {
		this.initExtension = Optional.of(initExtension);
		this.updateExtension = Optional.of(updateExtension);
		this.renderExtension = Optional.of(renderExtension);
	}

	public void init() { initExtension.ifPresent(InitExtension::init); }

	public void update() {
		updateExtension.ifPresent(UpdateExtension::update);
	}

	public void render() {
		renderExtension.ifPresent(RenderExtension::render);
	}

}
