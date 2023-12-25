package gameObject;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import math.Vector2d;
import state.Game_state;

public abstract class MovingObject extends GameObject {

	protected Vector2d velocity;
	protected AffineTransform at;
	protected double angle;
	protected double maxVel;
	protected int width;
	protected int height;
	protected Game_state gameState;
	
	public MovingObject(Vector2d position, Vector2d velocity,double maxVel, BufferedImage texture, Game_state gameState) {
		super(position, texture);
		
		this.velocity = velocity;
		this.maxVel=maxVel;
		this.gameState = gameState;
		width = texture.getWidth();
		height = texture.getHeight();
		angle = 0;
	}
	
	protected void collidesWith() {
		ArrayList<MovingObject> movingObjects = gameState.getMovingObjects();
		
		for (int i = 0; i < movingObjects.size(); i++) {
			
			MovingObject m = movingObjects.get(i);
			
			if(m.equals(this))
				continue;
			double distance = m.getCenter().subtrac(getCenter()).getMagnitude();
			
			if(distance < m.width/2 + width/2 && movingObjects.contains(this)) {
				objectCollision(m, this);
			}
			
		}
	}
   
	private void objectCollision(MovingObject a, MovingObject b) {
		
		if(a instanceof Player && ((Player)a).isSpawning()) {
			return;
		}
		if(b instanceof Player && ((Player)b).isSpawning()) {
			return;
		}
		
		if(!( a instanceof Meteor && b instanceof Meteor)) {
			gameState.playExplosion(getCenter());
			a.Destroy();
			b.Destroy();
		}
	}
	
	 protected void Destroy() {
		 gameState.getMovingObjects().remove(this);
	 }
	
	protected Vector2d getCenter() {
		return new Vector2d(position.getX()+ width/2, position.getY()+height/2);
	}
	

	
	
}
