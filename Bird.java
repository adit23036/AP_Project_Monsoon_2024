package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class Bird {
    private Texture texture;
    private Vector2 position;
    private float width;
    private float height;

    public Bird(String texturePath , float x , float y , float width , float height) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, width, height);
    }
    public void dispose() {
        texture.dispose();
    }


}
