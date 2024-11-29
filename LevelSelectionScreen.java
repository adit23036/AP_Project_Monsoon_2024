package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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

public class LevelSelectionScreen implements Screen {
    private Stage stage;
    private AssetManager assetManager;
    private Viewport viewport;
    private static final float VIRTUAL_WIDTH = 2000;
    private static final float VIRTUAL_HEIGHT = 1080;
    private Texture backgroundTexture;
    private Texture level1Texture;
    private Texture level2Texture;
    private Texture level3Texture;
    private Texture titleTexture;
    private Texture backButtonTexture;
    private Texture level1ButtonUpTexture;
    private Texture level1ButtonDownTexture;
    private Texture level2ButtonUpTexture;
    private Texture level2ButtonDownTexture;
    private Texture level3ButtonUpTexture;
    private Texture level3ButtonDownTexture;
    private Texture SelectLevelText;
    private Game game;
    private ImageButton level1Button;
    private ImageButton level2Button;
    private ImageButton level3Button;
    private ImageButton backButton;
    private Texture titleTexture1;

    public LevelSelectionScreen(Game game) {
        this.game = game;
        assetManager = new AssetManager();
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        assetManager.load("level_background.png", Texture.class);
        assetManager.load("ice_rock.png", Texture.class);
        assetManager.load("volvcano.png", Texture.class);
        assetManager.load("forest.png", Texture.class);
        assetManager.load("back_button.png", Texture.class);
        assetManager.load("title1.png", Texture.class);
        assetManager.load("1press_button.png", Texture.class);
        assetManager.load("2press_button.png", Texture.class);
        assetManager.load("3_pressbutton.png", Texture.class);
        assetManager.load("selectlevel.png", Texture.class);
        assetManager.finishLoading();
    }

    public void createUI() {
        backgroundTexture = assetManager.get("level_background.png", Texture.class);
        Image background = new Image(backgroundTexture);
        background.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        background.setPosition(0, 0);
        stage.addActor(background);
        titleTexture = assetManager.get("title1.png", Texture.class);
        Image title = new Image(titleTexture);
        title.setSize(600, 200);
        title.setPosition((VIRTUAL_WIDTH - title.getWidth()) / 2, VIRTUAL_HEIGHT - 200);
        stage.addActor(title);
        level1Texture = assetManager.get("volvcano.png", Texture.class);
        Image level1 = new Image(level1Texture);
        level1.setSize(400, 400);
        level1.setPosition(1300, 460);
        stage.addActor(level1);

        level2Texture = assetManager.get("ice_rock.png", Texture.class);
        Image level2 = new Image(level2Texture);
        level2.setSize(400, 400);
        level2.setPosition(800, 460);
        stage.addActor(level2);

        level3Texture = assetManager.get("forest.png", Texture.class);
        Image level3 = new Image(level3Texture);
        level3.setSize(400, 400);
        level3.setPosition(270, 460);
        stage.addActor(level3);
        level1ButtonUpTexture = assetManager.get("1press_button.png", Texture.class);
        level1ButtonDownTexture = level1ButtonUpTexture;
        level2ButtonUpTexture = assetManager.get("2press_button.png", Texture.class);
        level2ButtonDownTexture = level2ButtonUpTexture;
        level3ButtonUpTexture = assetManager.get("3_pressbutton.png", Texture.class);
        level3ButtonDownTexture = level3ButtonUpTexture;
        level1Button = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(level1ButtonUpTexture)),
            new TextureRegionDrawable(new TextureRegion(level1ButtonDownTexture))
        );
        level1Button.setSize(300, 100);
        level1Button.setPosition(315, 345);
        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstLevel(game));
            }
        });
        stage.addActor(level1Button);

        level2Button = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(level2ButtonUpTexture)),
            new TextureRegionDrawable(new TextureRegion(level2ButtonDownTexture))
        );
        level2Button.setSize(300, 100);
        level2Button.setPosition(825, 345);
        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstLevel(game));
            }
        });
        stage.addActor(level2Button);
        level3Button = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(level3ButtonUpTexture)),
            new TextureRegionDrawable(new TextureRegion(level3ButtonDownTexture))
        );
        level3Button.setSize(300, 100);
        level3Button.setPosition(1350, 345);
        level3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new FirstLevel(game));
            }
        });
        stage.addActor(level3Button);
        backButtonTexture = assetManager.get("back_button.png", Texture.class);
        backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(backButtonTexture)));
        backButton.setSize(140, 140);
        backButton.setPosition(50, VIRTUAL_HEIGHT - 175);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(backButton);
        titleTexture1 = assetManager.get("selectlevel.png", Texture.class);
        Image title1 = new Image(titleTexture1);
        title1.setSize(600, 200);
        title1.setPosition(700, 100);

        stage.addActor(title1);

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
    public void pause() {}

    @Override
    public void resume() {}

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
