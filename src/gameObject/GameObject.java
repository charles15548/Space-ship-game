package gameObject;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2d;

public abstract class GameObject {

	protected BufferedImage texture;
	protected Vector2d position;
	
	public GameObject(Vector2d position, BufferedImage texture)
	{
		this.position=position;
		this.texture=texture;
	}
	public abstract void update();
	public abstract void draw(Graphics g);
	public Vector2d getPosition() {
		return position;
	}
	public void setPosition(Vector2d posicion) {
		this.position = posicion;
	}
	
	
}
