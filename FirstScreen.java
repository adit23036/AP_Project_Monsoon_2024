package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static java.awt.geom.Path2D.contains;

public class FirstScreen implements Screen {
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
    private Vector2 dragStart;
    private Vector2 dragEnd;
    private ShapeRenderer shapeRenderer;
    private static final float VIRTUAL_WIDTH = 2000;
    private static final float VIRTUAL_HEIGHT = 1080;
    private static final float MAX_DRAG_DISTANCE = 200f;

    public FirstScreen(Game game) {
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
        float groundWidth = VIRTUAL_WIDTH;
        Ground ground = new Ground(
            physicsWorld.getWorld(),
            0,
            -10,
            groundWidth,
            groundHeight
        );
        initializeGameObjects();
        camera.update();
        stage = new Stage(viewport, batch);
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);
        pauseButton.setPosition(VIRTUAL_WIDTH - pauseButton.getWidth() - 20, VIRTUAL_HEIGHT - pauseButton.getHeight() - 60);
        stage.addActor(pauseButton);
        pauseButton.setPosition(100,900);
        dragStart = new Vector2();
        dragEnd = new Vector2();
    }

    private void initializeGameObjects() {
        birdsList = new ArrayList<>();
        birdsList.add(new Red(270, 355, physicsWorld.getWorld(),camera,physicsWorld.getRenderer()));
        birdsList.add(new Chuck(170, 130, physicsWorld.getWorld(),camera,physicsWorld.getRenderer()));
        birdsList.add(new Bomb(90, 210, physicsWorld.getWorld(),camera,physicsWorld.getRenderer()));
        birdsList.add(new Blue(20, 210, physicsWorld.getWorld(),camera,physicsWorld.getRenderer()));
        physicsWorld.SetBirdsList(birdsList);
        activeBird = birdsList.get(0);
        physicsWorld.call_mouse();
        blockList = new ArrayList<>();
//        blockList.add(new Block("thin_vertical_wood.png", 1600, 210, 15, 150));
//        blockList.add(new Block("Thin_vertical_wood.png", 1485, 210, 15, 150));
//        blockList.add(new Block("steel_thinblock.png", 1485, 355, 130, 15));
//        blockList.add(new Block("thin_vertical_wood.png", 1600, 360, 15, 150));
//        blockList.add(new Block("Thin_vertical_wood.png", 1485, 360, 15, 150));
//        blockList.add(new Block("steel_thinblock.png", 1485, 505, 130, 15));
//        blockList.add(new Block("Thick_vertical.png", 1487, 515, 30, 100));
//        blockList.add(new Block("Thick_vertical.png", 1580, 515, 30, 100));
        pigsList = new ArrayList<>();
//        pigsList.add(new Pigs("Pigs.png", 1517, 515, 65, 45));
//        pigsList.add(new Pigs("Pigs.png", 1510, 362, 80, 60));
//        pigsList.add(new Pigs("King_pig.png", 1495, 207, 110, 100));
    }

    @Override
    public void show() {
        physicsWorld.setActiveBird(activeBird);
        stage.setKeyboardFocus(null);
        stage.setScrollFocus(null);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        physicsWorld.getWorld().step(1 / 60f, 6, 2);

        for (Bird bird : birdsList) bird.render(batch, physicsWorld.getWorld());
//        for (Block block : blockList) block.render(batch);
        for (Pigs pig : pigsList) pig.render(batch,physicsWorld.getWorld());
        catapult.render(batch);
        batch.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float touchX = Gdx.input.getX() - 20;
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY() - 300;
            if (pauseButton.hit(touchX, touchY, true) != null) {
                game.setScreen(new PauseScreen(game));
            }
        }


        if (activeBird != null) {
            drawTrajectory();
        }
        physicsWorld.getRenderer().render(physicsWorld.getWorld(), camera.combined);
        physicsWorld.render(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    private void switchToNextBird() {
        int currentIndex = birdsList.indexOf(activeBird);
        if (currentIndex < birdsList.size() - 1) {
            activeBird = birdsList.get(currentIndex + 1);
            physicsWorld.setActiveBird(activeBird);
        }
    }

    private void drawTrajectory() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        Vector2 launchVelocity = calculateLaunchVelocity(dragStart, dragEnd);
        float gravity = -9.8f;
        float timeStep = 0.1f;
        int maxSteps = 100;
        Vector2 position = new Vector2(activeBird.getPosition());
        for (int i = 0; i < maxSteps; i++) {
            float t = i * timeStep;
            float x = position.x + launchVelocity.x * t;
            float y = position.y + launchVelocity.y * t + 0.5f * gravity * t * t;

            if (y < 0) break; // Stop rendering if below the ground
            shapeRenderer.circle(x, y, 2); // Draw small circles for the path
        }

        shapeRenderer.end();
    }

    private Vector2 calculateLaunchVelocity(Vector2 start, Vector2 end) {
        Vector2 dragVector = new Vector2(start.x - end.x, start.y - end.y);
        if (dragVector.len() > MAX_DRAG_DISTANCE) {
            dragVector.setLength(MAX_DRAG_DISTANCE);
        }
        float scale = 5f;
        return dragVector.scl(scale);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        pauseButtonTexture.dispose();
        stage.dispose();
        for (Bird bird : birdsList) bird.dispose();
        for (Block block : blockList) block.dispose();
        for (Pigs pig : pigsList) pig.dispose();
        catapult.dispose();
    }
}
