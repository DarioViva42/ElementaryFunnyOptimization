package com.efo.engine;

public abstract class AbstractGame {
  public abstract void update(Engine ge, float dt);
  public abstract void render(Engine ge, Renderer r);
}
