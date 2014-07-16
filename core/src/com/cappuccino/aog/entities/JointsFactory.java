package com.cappuccino.aog.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.cappuccino.aog.scene.GameScene;

public class JointsFactory {

	public static Joint createWeldJoint(World world, Entity e1, Entity e2, Vector2 anchorE1, Vector2 anchorE2){
		WeldJointDef jointDef = new WeldJointDef();
		
		jointDef.bodyA = e1.getBody();
		jointDef.bodyB = e2.getBody();
		
		jointDef.localAnchorA.set(anchorE1.scl(GameScene.BOX_TO_WORLD));
		jointDef.localAnchorB.set(anchorE2.scl(GameScene.BOX_TO_WORLD));
		
		jointDef.collideConnected = false;
		
		return world.createJoint(jointDef);
	}
	
	public static Joint createWeldJoint(World world, Entity e1, Entity e2, Vector2 anchorE1, Vector2 anchorE2, float angle, boolean collide){
		WeldJointDef jointDef = new WeldJointDef();
		
		jointDef.bodyA = e1.getBody();
		jointDef.bodyB = e2.getBody();
		
		jointDef.localAnchorA.set(anchorE1.scl(GameScene.BOX_TO_WORLD));
		jointDef.localAnchorB.set(anchorE2.scl(GameScene.BOX_TO_WORLD));
		
		jointDef.collideConnected = collide;
		jointDef.referenceAngle = angle;
		
		return world.createJoint(jointDef);
	}
	
	public static Joint createRopeJoint(World world, Entity e1, Entity e2, Vector2 anchorE1, Vector2 anchorE2, float len, boolean collide){
		RopeJointDef jointDef = new RopeJointDef();
		
		jointDef.bodyA = e1.getBody();
		jointDef.bodyB = e2.getBody();
		
		jointDef.localAnchorA.set(anchorE1.scl(GameScene.BOX_TO_WORLD));
		jointDef.localAnchorB.set(anchorE2.scl(GameScene.BOX_TO_WORLD));
		
		jointDef.maxLength = len*GameScene.BOX_TO_WORLD;
		jointDef.collideConnected = collide;
		
		return world.createJoint(jointDef);
	}
	
	
	public static Joint createRevoluteJoint(World world, Entity e1, Entity e2, Vector2 anchorE1, Vector2 anchorE2, boolean collide){
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.bodyA = e1.getBody();
		jointDef.bodyB = e2.getBody();
		
		jointDef.localAnchorA.set(anchorE1.scl(GameScene.BOX_TO_WORLD));
		jointDef.localAnchorB.set(anchorE2.scl(GameScene.BOX_TO_WORLD));
		
		jointDef.collideConnected = collide;
		
		return world.createJoint(jointDef);
	
	}
	
	
	
	
}
