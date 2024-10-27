package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseScreen implements Screen {
    private Game game;
    private Stage stage;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Image background;
    private TextButton resumeButton;
    private TextButton returnToMenuButton;
    private Skin skin;
    private static final float VIRTUAL_WIDTH = 2000;
    private static final float VIRTUAL_HEIGHT = 1080;
    private Image image1;
    private Image image2;
    private Texture imageTexture1;
    private Texture imageTexture2;

    public PauseScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = new Texture("level_background.png");
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);
        skin = new Skin();
        skin.add("default-font", new BitmapFont());
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        skin.add("default", textButtonStyle);
        addImages();
        addButtons();
    }

    private void addImages() {
        imageTexture1 = new Texture("empty_button.png");
        imageTexture2 = new Texture("empty_button.png");
        image1 = new Image(imageTexture1);
        image2 = new Image(imageTexture2);
        image1.setPosition(VIRTUAL_WIDTH / 4 - image1.getWidth() / 2, VIRTUAL_HEIGHT / 2 + 100);
        image2.setPosition(3 * VIRTUAL_WIDTH / 4 - image2.getWidth() / 2, VIRTUAL_HEIGHT / 2 + 100);
        stage.addActor(image1);
        stage.addActor(image2);
    }

    private void addButtons() {
        resumeButton = new TextButton("Resume", skin);
        returnToMenuButton = new TextButton("Return to Menu", skin);
        resumeButton.setSize(300, 100);
        returnToMenuButton.setSize(300, 100);
        float buttonSpacing = 50;
        float image1X = image1.getX() + (image1.getWidth() - resumeButton.getWidth()) / 2;
        float image2X = image2.getX() + (image2.getWidth() - returnToMenuButton.getWidth()) / 2;
        float imageY = image1.getY() + (image1.getHeight() - resumeButton.getHeight()) / 2;
        resumeButton.setPosition(image1X, imageY);
        returnToMenuButton.setPosition(image2X, imageY);
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });
        returnToMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(resumeButton);
        stage.addActor(returnToMenuButton);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
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
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        skin.dispose();
        imageTexture1.dispose();
        imageTexture2.dispose();
    }
}
