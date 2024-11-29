package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;

public class Ground {
    private Body groundBody;
    private Texture texture;
    private float width;
    private float height;
    private int damage;

    public Ground(World world, float x, float y, float width,float height) {
        this.width = width;
        this.height = height;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + width / 2, y + height / 2);
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(width / 2, 3*height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundShape;
        fixtureDef.friction = 0.6f;
        fixtureDef.restitution = 0;
        groundBody = world.createBody(bodyDef);
        groundBody.createFixture(fixtureDef);
        groundShape.dispose();
        this.damage = 60;
    }

    public int getDamage(){
        return damage;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, groundBody.getPosition().x - width / 2, groundBody.getPosition().y - height / 2, width, height);

    }

    public Body getGroundBody(){
        return groundBody;
    }

    public void dispose() {
        texture.dispose();
    }

}
