package io.github.some_example_name;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class Pigs {
    private Texture texture;
    private Vector2 position;
    private float width;
    private float height;
    private int health;
    private ShapeRenderer shapeRenderer ;
    private OrthographicCamera camera;
    private float radius;
    private Box2DDebugRenderer debugRenderer;
    private float x;
    private float y;
    private Body body;
    private Boolean IsDestroyed;

    public Pigs(String texturePath , float x , float y , float width , float height, World world, float radius, float mass, float restitution, float friction , int health, OrthographicCamera camera , Box2DDebugRenderer debugRenderer) {
        this.texture = new Texture(texturePath);
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.health = health;
        this.camera = camera;
        camera.position.set(25, 15, 0);
        camera.update();
        createBody(world, x, y, width,height, mass, restitution, friction);
        this.radius = radius;
        this.shapeRenderer = new ShapeRenderer();
        this.debugRenderer = debugRenderer;
        this.x = x;
        this.y = y;
        body.setUserData(this);
        IsDestroyed = false;

    }

    private void createBody(World world, float x, float y, float width, float height, float mass, float restitution, float friction) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.gravityScale = 40f;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); // Define the rectangle by half-width and half-height

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = mass / (width * height); // Adjust density for the rectangular area
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void setPosition(float x,float y){
        body.setTransform(x + radius, y + radius, body.getAngle());
    }

    public int getHealth() {
        return health;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isDestroyed() {
        return health<=0;
    }

    public void applyDamage(int damage){
        if (!isDestroyed()) {
            health -= damage;
            if (health <= 0) {
                IsDestroyed = true;
            }
        }
    }

    public Body getBody(){
        return this.body;
    }

    public void render(SpriteBatch batch, World world) {
        if(!isDestroyed()){
            Vector2 position = body.getPosition();
            batch.draw(texture, position.x-2*width/5, position.y-8*height/11 , 90 , 70);
        }
    }

    public void dispose() {
        texture.dispose();
    }

}

class KingPig extends Pigs {
    public KingPig(float x, float y, World world, OrthographicCamera camera, Box2DDebugRenderer debugRenderer) {
        super("King_pig.png", x, y, 70, 80, world, 50f, 5f, 0.1f, 0.4f, 200, camera, debugRenderer);
    }

    @Override
    public void render(SpriteBatch batch, World world) {
        if(!isDestroyed()){
        Vector2 position = this.getBody().getPosition();
        batch.draw(this.getTexture(), position.x-40, position.y-30,80, 80);
    }
    }
}

class CorporalPig extends Pigs {
    public CorporalPig(float x, float y, World world, OrthographicCamera camera, Box2DDebugRenderer debugRenderer) {
        // Use constructor with specific properties for CorporalPig
        super("corporal_pig.png", x, y, 80, 80, world, 35f, 3f, 0.6f, 0.5f, 120, camera, debugRenderer);
    }
}
