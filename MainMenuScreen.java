package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.assets.AssetManager;


public class MainMenuScreen extends ScreenAdapter implements Screen {
    private Game game;
    private Stage stage;
    private ImageButton levelButton;
    private ImageButton settingsButton;
    private ImageButton profileButton;
    private Texture backgroundTexture;
    private Texture levelButtonUpTexture;
    private Texture levelButtonDownTexture;
    private Texture settingsButtonUpTexture;
    private Texture settingsButtonDownTexture;
    private AssetManager assetManager;
    private Viewport viewport;

    private static final float VIRTUAL_WIDTH = 2000;
    private static final float VIRTUAL_HEIGHT = 1080;

    public MainMenuScreen(Game game) {
        this.game = game;
        assetManager = new AssetManager();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        assetManager.load("main_menu1.png", Texture.class);
        assetManager.load("Play_button.png", Texture.class);
        assetManager.load("settings_button.png", Texture.class);
        assetManager.finishLoading();
    }

    private void createUI() {
        backgroundTexture = assetManager.get("main_menu1.png", Texture.class);
        Image background = new Image(backgroundTexture);
        background.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        background.setPosition(0, 0);
        levelButtonUpTexture = assetManager.get("Play_button.png", Texture.class);
        levelButtonDownTexture = levelButtonUpTexture;
        settingsButtonUpTexture = assetManager.get("settings_button.png", Texture.class);
        settingsButtonDownTexture = settingsButtonUpTexture;
        levelButton = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(levelButtonUpTexture)),
            new TextureRegionDrawable(new TextureRegion(levelButtonDownTexture))
        );
        settingsButton = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(settingsButtonUpTexture)),
            new TextureRegionDrawable(new TextureRegion(settingsButtonDownTexture))
        );
        levelButton.setSize(300, 100);
        settingsButton.setSize(300, 100);

        levelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LevelSelectionScreen(game));
            }
        });
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SettingsScreen(game));
            }
        });

        stage.addActor(background);
        stage.addActor(levelButton);
        stage.addActor(settingsButton);
        levelButton.setPosition(800,385);
        settingsButton.setPosition(980,385);

    }

    @Override
    public void show() {
        createUI();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
        assetManager.dispose();
    }
}
