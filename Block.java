package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Block {
    private Texture texture;
    private Vector2 position;
    private float width;
    private float height;
    private int health;
    private int damage;
    private Body body;
    private ShapeRenderer shapeRenderer ;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private Boolean isFinished =false;

    public Block(String texturePath, float x, float y, float width, float height, int initialHealth, int damage, World world ,  OrthographicCamera camera , Box2DDebugRenderer debugRenderer) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.health = initialHealth;
        this.damage = damage;
        this.camera = camera;
        this.debugRenderer = debugRenderer;
        this.shapeRenderer = new ShapeRenderer();
        createBody(world, x, y, width, height);
        body.setUserData(this);
    }

    public Texture getTexture() {
        return texture;
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    private void createBody(World world, float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.gravityScale = 9.8f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.0f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.friction = 0.5f;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public int getDamage() {
        return damage;
    }

    public void applyDamage(int damage) {
        if (!isDestroyed()) {
            health -= this.damage;
            if (health <= 0) {
                isFinished = true;
            }
        }
    }

    public boolean isDestroyed() {
        return health<=0;
    }


    public int getHealth() {
        return health;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Body getBody() {
        return body;
    }

    public void onCollisionWithBird(Contact contact) {
        if (contact.getFixtureA().getBody().getType() == BodyDef.BodyType.DynamicBody) {
            // Apply a force when the block hits a bird
            Vector2 impulse = contact.getFixtureA().getBody().getLinearVelocity().scl(0.5f);  // Adjust scaling factor as needed
            body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
        }
    }


    public void render(SpriteBatch batch, World world) {
//        debugRenderer.render(world, camera.combined);
        if(!isDestroyed()) {
            Vector2 position = body.getPosition();
            batch.draw(texture, position.x, position.y - 80, width, height + 20);
        }
    }
    public void dispose() {
        texture.dispose();
    }
}

class Wood extends Block {
    public Wood(String texturePath, float x, float y, float width, float height, int initialHealth, int damage, World world, OrthographicCamera camera, Box2DDebugRenderer debugRenderer) {
        super(texturePath, x, y, width, height, initialHealth, damage, world, camera, debugRenderer);
    }

}

class Steel extends Block {
    public Steel(String texturePath, float x, float y, float width, float height, int initialHealth, int damage, World world, OrthographicCamera camera, Box2DDebugRenderer debugRenderer) {
        super(texturePath, x, y, width, height, initialHealth, damage, world, camera, debugRenderer);
    }

    @Override
    public void render(SpriteBatch batch, World world) {
        if(!isDestroyed()) {
            Vector2 position = getBody().getPosition();
            batch.draw(this.getTexture(), position.x - 55, position.y - 17, getWidth(), getHeight());
        }
    }
}


class Ice extends Block {
    public Ice(String texturePath, float x, float y, float width, float height, int initialHealth, int damage, World world, OrthographicCamera camera, Box2DDebugRenderer debugRenderer) {
        super(texturePath, x, y, width, height, initialHealth, damage, world, camera, debugRenderer);
    }

}

