package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Catapult {
    private Texture texture;
    private Vector2 position;
    private float width;
    private float height;
    private Vector2 anchorPosition;
    private ShapeRenderer shapeRenderer;
    private Body rectangleBody;
    private float rectangleHeight;

    public Catapult(String texturePath, float x, float y, float width, float height, World world) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.anchorPosition = new Vector2(x + width / 2, y + height);
        this.shapeRenderer = new ShapeRenderer();
        this.rectangleHeight = 0.6f*(anchorPosition.y - position.y);
        createRectangleBody(world);
    }

    private void createRectangleBody(World world) {
        float centerX = position.x + width / 2;
        float centerY = position.y + rectangleHeight / 2;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(centerX, centerY);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, rectangleHeight / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0f;
        rectangleBody = world.createBody(bodyDef);
        rectangleBody.createFixture(fixtureDef);
        shape.dispose();
    }
    public Vector2 getAnchorPosition() {
        return anchorPosition;
    }
    public void render(SpriteBatch batch) {

        batch.draw(texture, position.x, position.y, width, height);
    }


    public void dispose() {
        texture.dispose();
        shapeRenderer.dispose();
    }
}
