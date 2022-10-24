![GitHub Logo](/res/icon/repo_image_new.png)
# JFlatGL
A library implementing a basic 2D game engine in Java, built using LWJGL 3. I use this library for my personal projects.

# Structure
JFlatGL is separated into modules. The core functionality is provided in JFlatCore. The additional modules are optional ones which provide additional features.
For example, the module JFlatGUI provides an implementation for a basic GUI system using the JFlatCore library, and JFlatTile offers a set of classes for rendering tiles
to create tile-based games.

# Features
 - 2D rendering images and simple geometry (rect, circle)
 - Automatic layering system; simply set the layer of each render element and the renderer does the rest.
 - Engine extensions allow developers to add logic stages to the underlying engine methods
 - Render extensions allow developers to add custom renderable elements and asset types
 - Support for rendering a splash screen while loading assets
 - Audio system for playing many sound effects simultaneously
 - Many aspects of the underlying engine are configurable by modifying the config.json file.
