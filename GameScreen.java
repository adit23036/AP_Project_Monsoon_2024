package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class GameScreen implements Screen {
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

    private static final float VIRTUAL_WIDTH = 2000;
    private static final float VIRTUAL_HEIGHT = 1080;

    public GameScreen(Game game){
        this.game = game;
        batch = new SpriteBatch();
        background = new Texture("GameScreen.png");
        pauseButtonTexture = new Texture("pause_button.png");
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();
        birdsList = new ArrayList<Bird>();
        birdsList.add(new Bird("Chuck.png", 170, 210, 80, 60));
        birdsList.add(new Bird("Bomb.png", 70, 210, 100, 80));
        birdsList.add(new Bird("Blue_bird.png", 20, 210, 40, 30));
        catapult = new Catapult("catapult.png", 250, 210, 100, 200);
        blockList = new ArrayList<Block>();
        blockList.add(new Block("thin_vertical_wood.png", 1600, 210, 15, 150));
        blockList.add(new Block("Thin_vertical_wood.png", 1485, 210, 15, 150));
        blockList.add(new Block("steel_thinblock.png", 1485, 355, 130, 15));
        blockList.add(new Block("thin_vertical_wood.png", 1600, 360, 15, 150));
        blockList.add(new Block("Thin_vertical_wood.png", 1485, 360, 15, 150));
        blockList.add(new Block("steel_thinblock.png", 1485, 505, 130, 15));
        blockList.add(new Block("Thick_vertical.png", 1487, 515, 30, 100));
        blockList.add(new Block("Thick_vertical.png", 1580, 515, 30, 100));
        pigsList = new ArrayList<Pigs>();
        pigsList.add(new Pigs("Pigs.png", 1517, 515, 65, 45));
        pigsList.add(new Pigs("Pigs.png", 1510, 362, 80, 60));
        pigsList.add(new Pigs("King_pig.png", 1495, 207, 110, 100));
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);
        TextureRegionDrawable pauseDrawable = new TextureRegionDrawable(pauseButtonTexture);
        pauseButton = new ImageButton(pauseDrawable);
        pauseButton.setPosition(VIRTUAL_WIDTH - pauseButton.getWidth() - 20, VIRTUAL_HEIGHT - pauseButton.getHeight() - 20);
        stage.addActor(pauseButton);
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PauseScreen(game));
            }
        });
        pauseButton.setPosition(100,900);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        for (Bird bird : birdsList) {
            bird.render(batch);
        }
        for (Block blo : blockList) {
            blo.render(batch);
        }
        for (Pigs pig : pigsList) {
            pig.render(batch);
        }
        catapult.render(batch);
        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        for (Block blo : blockList) blo.dispose();
        for (Pigs pig : pigsList) pig.dispose();
        catapult.dispose();
    }
}
