package gameObject;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import graphics.Assets;
import math.Vector2d;
import state.Game_state;

public class Ufo  extends MovingObject{

	private ArrayList<Vector2d> path;
	
	private Vector2d currentNode;
	
	private int index;
	
	private boolean following;
	
	private Chronometer fireRate;
	
	public Ufo(Vector2d position, Vector2d velocity, double maxVel, BufferedImage texture,
		ArrayList<Vector2d> path,	Game_state gameState) {
		super(position, velocity, maxVel, texture, gameState);
		// TODO Auto-generated constructor stub
		this.path = path;
		index =0;
		following = true;
		fireRate = new Chronometer();
		fireRate.run(Constants.UFO_FIRE_RATE);
	}
	
	private Vector2d pathFollowing() {
		currentNode = path.get(index);
		
		double distanceToNode = currentNode.subtrac(getCenter()).getMagnitude();
		
		if(distanceToNode < Constants.NODE_RADIUS) {
			index ++;
			if(index >= path.size()) {
				following = false;
			}
			
		}
		return seekForce(currentNode);
	}
	
	private Vector2d seekForce(Vector2d target) {
		Vector2d  desiredVelocity = target.subtrac(getCenter());
		desiredVelocity = desiredVelocity.normalize().scale(maxVel);
		return desiredVelocity.subtrac(velocity);
	}

	@Override
	public void update() {
		Vector2d pathFollowing;
		
		if(following)
			pathFollowing = pathFollowing();
		else
			pathFollowing = new Vector2d();
		
		pathFollowing = pathFollowing.scale(1/Constants.UFO_MASS);
		
		velocity = velocity.add(pathFollowing);
		velocity = velocity.limit(maxVel);
		position = position.add(velocity);
		
				if(position.getX() <0 || position.getX() > Constants.WIDTH ||
						position.getY() < 0 || position.getY() > Constants.HEIGHT) {
					Destroy();
				}
			
			
			//SHOOT
			
			if(!fireRate.isRunning()) {
				Vector2d toPlayer = gameState.getPlayer().getCenter().subtrac(getCenter());
				
				toPlayer = toPlayer.normalize();
				
				double currentAngle = toPlayer.getAngle();
				
				currentAngle += Math.random()*Constants.UFO_ANGLE_RANGE - Constants.UFO_ANGLE_RANGE /2;
				
				
				
				
				if(toPlayer.getX() <0)
					currentAngle = -currentAngle + Math.PI;
				
				toPlayer = toPlayer.setDireccion(currentAngle);
				
				Laser laser = new Laser(
						getCenter().add(toPlayer.scale(width)),
						toPlayer,
						Constants.LASER_VEL,
						currentAngle + Math.PI/2,
						Assets.redLaser,
						gameState
						);
				gameState.getMovingObjects().add(0,laser);
				
				fireRate.run(Constants.UFO_FIRE_RATE);		
				
				
			}
			
			
			angle  += 0.05;
			
			collidesWith();
			fireRate.update();		
		
	}
	@Override
	public void Destroy() {
		gameState.addScore(Constants.UFO_SCORE);
		super.Destroy();
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		
		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		
		at.rotate(angle, width/2, height/2);
		
		g2d.drawImage(texture, at,null);
		
		
	}

}
