package gameObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Assets;
import math.Vector2d;
import state.Game_state;

public class Meteor extends MovingObject {
	private Size size;

	public Meteor(Vector2d position, Vector2d velocity, double maxVel, BufferedImage texture, Game_state gameState, Size size) {
		super(position, velocity, maxVel, texture, gameState);
		// TODO Auto-generated constructor stub
		this.size= size;
		this.velocity = velocity.scale(maxVel);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		position = position.add(velocity);
		
		if(position.getX() > Constants.WIDTH)
			position.setX(-width);
		if(position.getY() > Constants.HEIGHT)
			position.setY(-height);
		
		if(position.getX() < -width)
			position.setX(Constants.WIDTH);
		if(position.getY() < -height)
			position.setY(Constants.HEIGHT);
		
		angle += Constants.DELTAANGLE/2;
	}
	@Override
	public void Destroy() {
		gameState.divideMeteor(this);
		gameState.addScore(Constants.METEOR_SCORE);
		super.Destroy();
	}
	
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D)g;
        at= AffineTransform.getTranslateInstance(position.getX(), position.getY());
		
		at.rotate(angle, width/2, height/2);
		
		g2d.drawImage(texture, at,  null);
	}

	public Size getSize() {
		return size;
	}

	
}
