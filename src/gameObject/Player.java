package gameObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Assets;
import input.KeyBoard;

import math.Vector2d;
import state.Game_state;


public class Player extends MovingObject {

	
	private Vector2d heading;
	private Vector2d acceleration;
    private boolean Accelerating = false;
    private Chronometer fireRate;
   
    private boolean spawning, visible;
    
    private Chronometer spawnTime, flickerTime;
	
	public Player(Vector2d position, Vector2d velocity,double maxVel, BufferedImage texture, Game_state gameState) {
		super(position, velocity,maxVel, texture, gameState);
		this.gameState = gameState;
		heading= new Vector2d(0,1);
		acceleration = new Vector2d();
		fireRate = new Chronometer();
		spawnTime = new Chronometer();
		flickerTime = new Chronometer(); 
		}

	

	@Override
	public void update() {

		if(!spawnTime.isRunning()) {
			spawning = false;
			visible = true;
		}
		
		if(spawning) {
			
			if(!flickerTime.isRunning()) {
				
				flickerTime.run(Constants.FLICKER_TIME);
				visible = !visible;
			}
		}
		
		if(KeyBoard.SHOOT && !fireRate.isRunning() && !spawning) {
			gameState.getMovingObjects().add(0,new Laser(
					getCenter().add(heading.scale(width)),
					heading,
					10,
					angle,
					Assets.blueLaser,
					gameState
					));
			fireRate.run(Constants.FIRERATE);
		}
		
		if(KeyBoard.RIGHT)
			angle += Constants.DELTAANGLE;
		if(KeyBoard.LEFT)
			angle -= Constants.DELTAANGLE;
		if(KeyBoard.UP)
		{
			acceleration=heading.scale(Constants.ACC);
			Accelerating = true;
		}else
		{
			if(velocity.getMagnitude()!=0)
				acceleration = (velocity.scale(-1).normalize()).scale(Constants.ACC/2);
			Accelerating = false;
		}
		velocity=velocity.add(acceleration);
		
		velocity = velocity.limit(maxVel);
		heading=heading.setDireccion(angle-Math.PI/2);
		
		position= position.add(velocity);
		
		
		
		if(position.getX() > Constants.WIDTH)
			position.setX(-width);
		if(position.getY() > Constants.HEIGHT)
			position.setY(-height);
		
		if(position.getX() < -width)
			position.setX(Constants.WIDTH);
		if(position.getY() < -height)
			position.setY(Constants.HEIGHT);
			
			fireRate.update();
			spawnTime.update();
			flickerTime.update();
			collidesWith();
		
	}
	
	@Override
	public void Destroy() {
		spawning = true;
		spawnTime.run(Constants.SPAWNING_TIME);
		resetValues();
		gameState.subtracLife();
	}
	
	private void resetValues() {
		angle =0;
		velocity = new Vector2d();
	    position = new Vector2d(Constants.WIDTH/2 - Assets.player.getWidth()/2,
	    		Constants.HEIGHT/2 - Assets.player.getHeight()/2);	
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		if(!visible)
			return;
		
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform at1 = AffineTransform.getTranslateInstance(position.getX() + width/2 + 5,
				position.getY()+ height/2 + 10);
		AffineTransform at2 = AffineTransform.getTranslateInstance(position.getX()  + 5,
				position.getY()+ height/2 + 10);
		
		at1.rotate(angle, -5,-10);
		at2.rotate(angle, width/2 -5,-10);
		
		if(Accelerating)
		{
			g2d.drawImage(Assets.speed, at1, null);
			g2d.drawImage(Assets.speed, at2, null);
		}
		
		at= AffineTransform.getTranslateInstance(position.getX(), position.getY());
		
		at.rotate(angle, width/2, height/2);
		
		g2d.drawImage(texture, at,  null);
	}
	
	public boolean isSpawning() {
		return spawning;
	}

	

}
