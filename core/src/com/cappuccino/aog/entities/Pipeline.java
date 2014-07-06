package com.cappuccino.aog.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cappuccino.aog.Scene;

public class Pipeline extends Entity{

	public static enum Piece{
		HORIZONTAL, VERTICAL, ANGLE_UP_LEFT, ANGLE_UP_RIGHT, ANGLE_DOWN_LEFT, ANGLE_DOWN_RIGHT;
	}
	
	public static enum Direction{
		LEFT_TO_RIGHT, RIGHT_TO_LEFT;
	}
	
	
	private Entity path[];
	
	public Pipeline( World world, float x, float y, Direction dir, Piece[] route) {
		super("Pipe_H", world);
		init(world, BodyType.StaticBody);
	
		
		path = new Entity[route.length];

		Vector2 pos = new Vector2(x, y);
		
		for(int i=0; i<route.length; i++){
			switch (route[i]) {
				case HORIZONTAL:
					path[i] = new Entity("Pipe_H", world);
					path[i].init(world, BodyType.StaticBody);
					initFixture(path[i]);
					
					path[i].setCenter(pos.cpy());
					
					
					if(dir == Direction.RIGHT_TO_LEFT){
						pos.sub(path[i].getWidth(), 0);
						path[i].setAngle(180*MathUtils.degRad);
					}else{
						pos.add(path[i].getWidth(), 0);
					}
					
					break;
					
				case VERTICAL:
					path[i] = new Entity("Pipe_H", world);
					path[i].init(world, BodyType.StaticBody);
					initFixture(path[i]);
					
					path[i].setCenter(pos.cpy());
					
					if(route[i-1] == Piece.ANGLE_DOWN_LEFT || route[i-1] == Piece.ANGLE_DOWN_RIGHT){
						path[i].setAngle(90*MathUtils.degRad);
						pos.add(0, path[i].getWidth());
					}else{
						path[i].setAngle(-90*MathUtils.degRad);
						pos.sub(0, path[i].getWidth());
					}
					break;	
					
					case ANGLE_DOWN_LEFT:
						path[i] = new Entity("Pipe_A", world);
						path[i].init(world, BodyType.StaticBody);
						initFixture(path[i]);
						
						pos.add(0, -path[i].getOrigin().y+10);
						
						path[i].setPosition(pos.cpy());
						
						pos.add(path[i].getOrigin().x+10, path[i].getHeight());
						
						break;
						
					case ANGLE_UP_RIGHT:
						path[i] = new Entity("Pipe_A", world);
						path[i].init(world, BodyType.StaticBody);
						initFixture(path[i]);
						
						path[i].setAngle(180*MathUtils.degRad);
						
						pos.add(-16, -path[i].getOrigin().y+25);
						
						path[i].setPosition(pos.cpy());
						
						pos.add(path[i].getWidth(), path[i].getHeight()-16);
						break;
						
					case ANGLE_UP_LEFT:
						path[i] = new Entity("Pipe_A", world);
						path[i].init(world, BodyType.StaticBody);
						initFixture(path[i]);
						
						path[i].setAngle(90*MathUtils.degRad);
						
						pos.add(0, -path[i].getOrigin().y-10);
						
						path[i].setPosition(pos.cpy());
						
						pos.add(path[i].getWidth()-16, -path[i].origin.y+5);
						break;
						
			}
			
		}
		
		
	}
	
	protected void initFixture(Entity e){
		FixtureDef fd = new FixtureDef();
		fd.filter.categoryBits = WALL;
		fd.filter.maskBits = WALL_MASK;
		
		bodyLoader.attachFixture(e.body, e.texture.name, fd, e.getRealWidth()*e.scaleX*Scene.BOX_TO_WORLD, e.getRealWidth()*e.scaleY*Scene.BOX_TO_WORLD);
		
		e.origin.set(bodyLoader.getOrigin(e.texture.name, e.getRealWidth()*e.scaleX*Scene.BOX_TO_WORLD, e.getRealWidth()*e.scaleY*Scene.BOX_TO_WORLD));
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		for(int i=0; i<path.length; i++){
			path[i].draw(batch);
		}
	}
	

}
