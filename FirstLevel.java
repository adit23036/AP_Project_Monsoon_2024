package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class FirstLevel extends InputAdapter implements ApplicationListener,Screen{
    private Game game;
    private SpriteBatch batch;
    private Texture background;
    private Texture pauseButtonTexture;
    private ImageButton pauseButton;
    private Stage stage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private ArrayList<Bird> birdsList;
    private ArrayList<Block> blockList;
    private ArrayList<Pigs> pigsList;
    private Catapult catapult;
    private Physics physicsWorld;
    private Bird activeBird;
    private boolean isDragging = false;
    private Vector2 dragStart = new Vector2();
    private Vector2 dragEnd = new Vector2();
    private ShapeRenderer shapeRenderer;

    private static final float VIRTUAL_WIDTH = 2000;
    private static final float VIRTUAL_HEIGHT = 1080;
    private static final float MAX_DRAG_DISTANCE = 200f;
    private static final float BIRD_GROUND_LIMIT = 2f;

    private Body ground;
    private float birdOnGroundTimer = 2f;

    public FirstLevel(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        physicsWorld = new Physics();
        background = new Texture("GameScreen.png");
        pauseButtonTexture = new Texture("pause_button.png");
        camera = new OrthographicCamera();
        Box2D.init();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);

        catapult = new Catapult("catapult_main.png", 250, 200, 100, 200, physicsWorld.getWorld());

        float groundHeight = 95;
        Ground ground1 = new Ground(physicsWorld.getWorld(), 0, 13, VIRTUAL_WIDTH, groundHeight);
        ground = ground1.getGroundBody();

        initializeGameObjects();
        stage = new Stage(viewport, batch);

        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);
        pauseButton.setPosition(100, 900);
        stage.addActor(pauseButton);

        physicsWorld.SetGround(ground1);
        if (!birdsList.isEmpty()) activeBird.setGround(ground);
    }


    private void initializeGameObjects() {
        birdsList = new ArrayList<>();
        birdsList.add(new Red(270, 355, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        birdsList.add(new Chuck(170, 130, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        birdsList.add(new Bomb(90, 210, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        birdsList.add(new Blue(20, 210, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));

        if (!birdsList.isEmpty()) activeBird = birdsList.get(0);

        blockList = new ArrayList<>();
        blockList.add(new Block("thin_vertical_wood.png", 1100, 275, 15, 150, 100, 20, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        blockList.add(new Block("Thin_vertical_wood.png", 1205, 275, 15, 150, 100, 20, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        blockList.add(new Steel("steel_thinblock.png", 1154, 360, 130, 15, 100, 50, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        blockList.add(new Block("thin_vertical_wood.png", 1100, 430, 15, 150, 100, 20, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        blockList.add(new Block("Thin_vertical_wood.png", 1205, 430, 15, 150, 100, 20, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));
        blockList.add(new Steel("steel_thinblock.png", 1154, 525, 130, 15, 100, 50, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));

        pigsList = new ArrayList<>();
        pigsList.add(new Pigs("Pigs.png", 1140, 550, 50, 65, physicsWorld.getWorld(), 32.5f, 500f, 0.6f, 1f, 100, camera, physicsWorld.getRenderer()));
        pigsList.add(new Pigs("Pigs.png", 1560, 362, 80, 60, physicsWorld.getWorld(), 40.0f, 6.0f, 0.5f, 0.4f, 100, camera, physicsWorld.getRenderer()));
        pigsList.add(new KingPig(1155, 240, physicsWorld.getWorld(), camera, physicsWorld.getRenderer()));

        physicsWorld.getWorld().setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                handleCollision(contact);
            }
            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {}

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });
    }

    private void handleCollision(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        Object userDataA = fixtureA.getBody().getUserData();
        Object userDataB = fixtureB.getBody().getUserData();

        if (userDataA instanceof Bird && userDataB instanceof Block) {
            handleBirdCollision((Bird) userDataA, (Block) userDataB);
        } else if (userDataB instanceof Bird && userDataA instanceof Block) {
            handleBirdCollision((Bird) userDataB, (Block) userDataA);
        } else if (userDataA instanceof Bird && userDataB instanceof Pigs) {
            handlePigCollision((Bird) userDataA, (Pigs) userDataB);
        } else if (userDataB instanceof Bird && userDataA instanceof Pigs) {
            handlePigCollision((Bird) userDataB, (Pigs) userDataA);
        }
        else if(userDataB instanceof Bird && userDataA instanceof Ground){
            handleBirdCollisionwithGround((Bird) userDataB, (Ground) userDataA);
        }
    }

    private void handleBirdCollisionwithGround(Bird bird, Ground ground) {
        bird.applyDamage(30);
    }
    private void handleBirdCollision(Bird bird, Block block) {
        block.applyDamage(bird.getDamage());
        bird.applyDamage(block.getDamage());
    }

    private void handlePigCollision(Bird bird, Pigs pig) {
        pig.applyDamage(bird.getDamage());
        bird.applyDamage(bird.getDamage());
    }



    private boolean isGround(Fixture fixture) {
        return fixture.getBody() == ground;
    }

    private void switchToNextBird() {
        if (activeBird instanceof Red) {
            activeBird = new Chuck(270, 355, physicsWorld.getWorld(), camera, physicsWorld.getRenderer());
            physicsWorld.setActiveBird(activeBird);
        }
        else if(activeBird instanceof Chuck){
            activeBird = new Bomb(90, 210, physicsWorld.getWorld(), camera, physicsWorld.getRenderer());
            physicsWorld.setActiveBird(activeBird);
        }
        else if(activeBird instanceof Bomb){
            activeBird = new Blue(20, 210, physicsWorld.getWorld(), camera, physicsWorld.getRenderer());
            physicsWorld.setActiveBird(activeBird);
        }
        else {
            checkGameState();
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));
        dragStart.set(270,355);
        activeBird.setPosition(270, 350);
        if(activeBird.containsPoint(new Vector2(touchPos.x,touchPos.y))) isDragging = true;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));
        dragEnd.set(touchPos.x, touchPos.y);
        float distance = dragStart.dst(dragEnd);
        if (distance > MAX_DRAG_DISTANCE) {
            dragEnd.set(
                dragStart.x + (dragEnd.x - dragStart.x) * MAX_DRAG_DISTANCE / distance,
                dragStart.y + (dragEnd.y - dragStart.y) * MAX_DRAG_DISTANCE / distance
            );
        }

        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (!isDragging) return false;
        Vector3 touchPos = camera.unproject(new Vector3(screenX, screenY, 0));
        dragEnd.set(touchPos.x, touchPos.y);
        Vector2 launchVelocity = new Vector2(dragStart.x - dragEnd.x, dragStart.y - dragEnd.y).scl(10f);
        activeBird.launch(launchVelocity);

//        activeBird.launchTheBird(dragStart,dragEnd);
        isDragging = false;
        return true;
    }

    @Override
    public void create() {}

    @Override
    public void show() {
        physicsWorld.setActiveBird(activeBird);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        physicsWorld.getWorld().step(1 / 60f, 6, 2);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        if(!activeBird.isDestroyed()) activeBird.render(batch,physicsWorld.getWorld());
        for (Block block : blockList) {
            if (!block.getFinished()) block.render(batch, physicsWorld.getWorld());
        }
        for (Pigs pig : pigsList) {
            if (!pig.isDestroyed()) pig.render(batch, physicsWorld.getWorld());
        }
        catapult.render(batch);
        batch.end();

        if (isDragging && activeBird != null) {
            Vector2 velocity = new Vector2(dragStart.x - dragEnd.x, dragStart.y - dragEnd.y).scl(10f);
            renderTrajectory(shapeRenderer, velocity, dragStart);
        }
        if(activeBird==null){
            switchToNextBird();
        }
        stage.act();
        stage.draw();
    }

    public void checkGameState(){
        boolean allPigsDestroyed = true;
        for (Pigs pig : pigsList) {
            if (!pig.isDestroyed()) {
                allPigsDestroyed = false;
                break;
            }
        }
        if (allPigsDestroyed) {
            game.setScreen(new WinScreen(game));
        } else if (birdsList.isEmpty()) {
            game.setScreen(new LosingScreen(game));
        }
    }

    public void renderTrajectory(ShapeRenderer shapeRenderer, Vector2 velocity, Vector2 startPosition) {
        float timeStep = 0.1f;
        int maxSteps = 100000;
        float gravity = physicsWorld.getWorld().getGravity().y;

        Vector2 currentPosition = new Vector2(startPosition);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);

        for (int i = 0; i < maxSteps; i++) {
            float time = i * timeStep;
            float nextX = startPosition.x + velocity.x * time;
            float nextY = startPosition.y + velocity.y * time + 0.5f * gravity * time * time;
            if (nextY < 0) break;
            shapeRenderer.circle(nextX, nextY, 3);
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        pauseButtonTexture.dispose();
        stage.dispose();
        activeBird.dispose();
        for (Bird bird : birdsList) bird.dispose();
        for (Block block : blockList)
            if(block.getFinished()) block.dispose();
        for (Pigs pig : pigsList) {
            if(pig.isDestroyed()) pig.dispose();
        }
        catapult.dispose();

    }
}


