package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class SettingsScreen implements Screen {
    private Game game;
    private Stage stage;
    private Viewport viewport;
    private Texture backgroundTexture;
    private Image background;

    private static final float VIRTUAL_WIDTH = 2000;
    private static final float VIRTUAL_HEIGHT = 1080;

    public SettingsScreen(Game game) {
        this.game = game;
        viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        stage = new Stage(viewport);
        backgroundTexture = new Texture("level_background.png");
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);
        addButtons();
        Gdx.input.setInputProcessor(stage);
    }

    private void addButtons() {
        Texture termsTexture = new Texture("terms_privacy.png");
        Texture connectTexture = new Texture("connect.png");
        Texture musicTexture = new Texture("music_icon.png");
        Texture zoomTexture = new Texture("zoom_in_button.png");
        Texture closeTexture = new Texture("exit_button.png");
        Texture supportTexture = new Texture("support_button.png");
        ImageButton termsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(termsTexture)));
        ImageButton connectButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(connectTexture)));
        ImageButton musicButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(musicTexture)));
        ImageButton zoomButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(zoomTexture)));
        ImageButton closeButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(closeTexture)));
        ImageButton supportButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(supportTexture)));
        termsButton.setPosition(1000, 500);
        connectButton.setPosition(1000, 300);
        musicButton.setPosition(700, 530);
        zoomButton.setPosition(500, 530);
        supportButton.setPosition(400, 300);
        closeButton.setPosition(1800, 900);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        stage.addActor(termsButton);
        stage.addActor(connectButton);
        stage.addActor(musicButton);
        stage.addActor(zoomButton);
        stage.addActor(supportButton);
        stage.addActor(closeButton);
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
    }
}
