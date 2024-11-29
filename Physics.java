package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Physics implements Screen {
    private World world;
    private Box2DDebugRenderer renderer;
    private OrthographicCamera camera;
    private ArrayList<Bird> birdsList;
    private Bird activeBird;
    private int activeBirdIndex = 0;
    private boolean isDragging;
    private ShapeRenderer shapeRenderer;
    private static final float WORLD_GRAVITY = -9.8f;
    private static final int VELOCITY_ITERATIONS = 600;
    private static final int POSITION_ITERATIONS = 200;
    private MouseJoint mouseJoint;
    private MouseJointDef mouseJointDef;
    public Vector2 slingshotAnchor;
    public Ground ground;

    public Physics() {
        this.world = new World(new Vector2(0, WORLD_GRAVITY), false);
        this.renderer = new Box2DDebugRenderer(
            true,
            true,
            false,
            true,
            true,
            true
        );
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false,
            Gdx.graphics.getWidth() / 100f,
            Gdx.graphics.getHeight() / 100f
        );
        this.shapeRenderer = new ShapeRenderer();
        this.isDragging = false;
    }

    public void call_mouse(){
        slingshotAnchor = new Vector2(290, 355);
        mouseJointDef = new MouseJointDef();
        mouseJointDef.bodyA = world.createBody(new BodyDef());

        mouseJointDef.bodyB = activeBird.getBody();
        mouseJointDef.target.set(activeBird.getBody().getPosition());

        mouseJointDef.collideConnected = true;

    }

    
    public void SetGround(Ground ground){
        this.ground = ground;
    }
    private boolean isGround(Fixture fixture) {
        return fixture.getBody() == ground.getGroundBody();
    }

//    private void handleBirdCollision(Bird bird, Fixture otherFixture) {
//        Object otherUserData = otherFixture.getBody().getUserData();
//        if (otherUserData instanceof Block) {
//            Block block = (Block) otherUserData;
//            block.applyDamage(bird.getDamage());
//        }
//        if (isGround(otherFixture)) bird.updateState(); // Stop the bird
//    }

//    private void handleBlockCollision(Block blockA, Block blockB) {
//        blockA.applyDamage(blockB.getDamage());
//        blockB.applyDamage(blockA.getDamage());
//    }

//    private void handleCollision(Contact contact) {
//        Fixture fixtureA = contact.getFixtureA();
//        Fixture fixtureB = contact.getFixtureB();
//        if (isBirdCollision(fixtureA, fixtureB)) {
//            handleBirdCollision();
//        }
//    }



//    private boolean isBirdCollision(Fixture a, Fixture b) {
//        return (a.getBody() == activeBird.getBody() || b.getBody() == activeBird.getBody());
//    }
//
//    private void handleBirdCollision() {
//        if (activeBird != null) {
//            activeBird.updateState();
//        }
//    }

    public void SetBirdsList(ArrayList<Bird> birdsList) {
        this.birdsList = birdsList;
        this.activeBird = birdsList.get(0);
    }

    @Override
    public void show() {}

    public void render(float delta) {
//        renderer.render(world,camera.combined);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Box2DDebugRenderer getRenderer() {
        return renderer;
    }

    public World getWorld() {
        return world;
    }



    public void setActiveBird(Bird bird) {
        this.activeBird = bird;
    }

    public void dispose() {
        world.dispose();
        renderer.dispose();
        shapeRenderer.dispose();
    }

//    private void drawTrajectory(Bird bird) {
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.RED);
//
//        Vector2 velocity = bird.getPosition().cpy().sub(bird.getPosition()).scl(5);
//        Vector2 gravity = world.getGravity();
//        Vector2 currentPosition = bird.getPosition().cpy();
//
//        float timeStep = 0.1f; // Step size for trajectory points
//        for (int i = 0; i < 50; i++) {
//            float t = i * timeStep;
//            Vector2 nextPosition = new Vector2(
//                currentPosition.x + velocity.x * t,
//                currentPosition.y + velocity.y * t + 0.5f * gravity.y * t * t
//            );
//            shapeRenderer.line(currentPosition.x, currentPosition.y, nextPosition.x, nextPosition.y);
//            currentPosition = nextPosition;
//        }
//
//        shapeRenderer.end();
//    }

//    private void switchToNextBird() {
//        activeBirdIndex++;
//        if (activeBirdIndex < birdsList.size()) {
//            activeBird = birdsList.get(activeBirdIndex);
//        }
//    }


}
