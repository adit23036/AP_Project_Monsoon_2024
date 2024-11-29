package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static io.github.orioncraftmc.meditate.YogaMeasureOutput.getHeight;
import static io.github.orioncraftmc.meditate.YogaMeasureOutput.getWidth;

public class Bird extends Actor {
    protected Texture texture;
    protected Body body;
    protected float width;
    protected float height;
    private Vector2 initialPosition;
    private int damage;
    private ShapeRenderer shapeRenderer ;
    private float x;
    private float y;
    public void setType(BodyDef.BodyType bodyType) {
        body.setType(bodyType);
    }
    public enum State {FLYING,ONGROUND,FALLING,START,STOPPED};
    private State currentState;
    private State previousState;
    private OrthographicCamera camera;
    private float radius;
    private Box2DDebugRenderer debugRenderer;
    private int health;
    private Boolean IsFinished;
    private Body ground;


    public Bird(String texturePath, float x, float y, float width, float height,
                World world, float radius, float mass, float restitution, float friction , int damage, OrthographicCamera camera , Box2DDebugRenderer debugRenderer) {
        this.texture = new Texture(texturePath);
        this.width = width;
        this.height = height;
        this.initialPosition = new Vector2(x, y);
        currentState = State.START;
        previousState = State.START;
        createBody(world, x, y, radius, mass, restitution, friction);
        this.damage = damage;
        this.camera = camera;
        camera.position.set(25, 15, 0);
        camera.update();
        this.radius = radius;
        this.shapeRenderer = new ShapeRenderer();
        this.debugRenderer = debugRenderer;
        this.x = x;
        this.y = y;
        this.health = 100;
        body.setUserData(this);
        IsFinished = false;
    }

    public boolean containsPoint(Vector2 point) {
        float birdX = body.getPosition().x;
        float birdY = body.getPosition().y;
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        return (point.x >= birdX - halfWidth && point.x <= birdX + halfWidth &&
            point.y >= birdY - halfHeight && point.y <= birdY + halfHeight);
    }

    public void launch(Vector2 velocity) {
        if (body != null) {
            velocity.scl(10f);
            body.setLinearVelocity(velocity);
            body.setActive(true);
        }
    }

    public int getDamage() {
        return damage;
    }
    public boolean isDestroyed() {
        return health<=0;
    }


    public void applyDamage(int damage){
        if (!isDestroyed()) {
            health -= this.damage;
            if (health <= 0) {
                IsFinished = true;
            }
        }
    }

    public void setCurrentState(State state){
        currentState = state;
    }

    public void setPreviousState(State state){
        previousState = state;
    }

    public void updateState() {
        Vector2 velocity = body.getLinearVelocity();
        float velocityThreshold = 0.1f;

        if (Math.abs(velocity.x) < velocityThreshold &&
            Math.abs(velocity.y) < velocityThreshold) {
            currentState = State.STOPPED;
        }
        else if (velocity.y < 0) {
            currentState = State.FALLING;
        }
        else {
            currentState = State.FLYING;
        }
    }

//    public void launchTheBird(Vector2 anchor, Vector2 dragPosition) {
//        float distX = anchor.x - dragPosition.x;
//        float distY = anchor.y - dragPosition.y;
//
//        float launchStrength = (float) Math.sqrt(distX * distX + distY * distY);
//        float maxLaunchStrength = 30f;
//        if (launchStrength > maxLaunchStrength) {
//            launchStrength = maxLaunchStrength;
//        }
//        float angle = (float) Math.atan2(dragPosition.y - anchor.y, dragPosition.x - anchor.x);
//
//        // Scale factor to increase speed
//        float speedMultiplier = 2.0f; // Adjust this value to control speed
//
//        float velocityX = (float) (Math.cos(angle) * launchStrength * speedMultiplier);
//        float velocityY = (float) (Math.sin(angle) * launchStrength * speedMultiplier);
//
//        if (body != null) {
//            Vector2 velocity = new Vector2(velocityX * 100f, velocityY * 100f);
//            body.setLinearVelocity(velocity);
//            body.setActive(true); // Ensure the body is active
//            System.out.println("Launch Angle: " + Math.toDegrees(angle) + "°");
//            System.out.println("Velocity Applied: " + velocity);
//            System.out.println("Position After Launch: " + body.getPosition());
//        } else {
//            System.out.println("Body is null!");
//        }
//    }


//    public void updateCamera() {
//        if (currentState == State.FLYING || currentState == State.FALLING) {
//            Vector2 position = body.getPosition();
//            camera.position.set(position.x, position.y, 0);
//            camera.update();
//        }
//    }

//    public void drag(Vector2 touchPosition) {
//        if (currentState == State.START) {
//            isDragged = true;
//            body.setTransform(touchPosition.x, touchPosition.y, body.getAngle());
//        }
//    }
//
//    public void release(Vector2 releasePosition) {
//        if (isDragged) {
//            isDragged = false;
//            Vector2 impulse = releasePosition.sub(initialPosition).scl(impulseMultiplier);
//            body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
//            currentState = State.FLYING;
//        }
//    }


    public void setPosition(float x,float y){


    }


    public boolean hasStopped() {
        return body.getLinearVelocity().isZero(0.1f) && body.getAngularVelocity() < 0.1f;
    }

    public State getCurrentState() {
        return currentState;
    }

    public State getPreviousState(){
        return previousState;
    }
    public Body getBody(){
        return this.body;
    }

    public void setGround(Body ground) {
        this.ground = ground;
    }

    public Body getGround() {
        return ground;
    }

    private void createBody(World world, float x, float y, float radius, float mass, float restitution, float friction) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = mass / (float) (Math.PI * radius * radius);
        fixtureDef.restitution = restitution;
        fixtureDef.friction = friction;
        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public boolean isOnGround() {
        return body.getLinearVelocity().isZero(0.1f) && body.getPosition().y <= ground.getPosition().y + 0.5f;
    }

    public void render(SpriteBatch batch, World world) {
        Vector2 position = body.getPosition();
//        debugRenderer.render(world,camera.combined);
        batch.draw(texture, position.x-18, position.y-5 , radius*2, radius*2);
    }
    public void dispose() {
        texture.dispose();
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}

class Red extends Bird {
    public Red(float x, float y, World world, OrthographicCamera camera,Box2DDebugRenderer debugRenderer) {
        super("Red.png", x, y, 60, 60, world, 22f, 100000f, 0.6f, 0.5f , 100,camera, debugRenderer);
    }
}

class Chuck extends Bird {
    public Chuck(float x, float y, World world, OrthographicCamera camera,Box2DDebugRenderer debugRenderer) {
        super("Chuck.png", x, y, 140, 60, world, 30f, 1.2f, 0.8f, 0.4f , 80,camera,debugRenderer);
    }
}

class Bomb extends Bird {
    public Bomb(float x, float y, World world, OrthographicCamera camera,Box2DDebugRenderer debugRenderer) {
        super("Bomb.png", x, y, 70, 70, world, 35f, 2.0f, 0.4f, 0.3f , 130,camera,debugRenderer);
    }
}

class Blue extends Bird {
    public Blue(float x, float y, World world, OrthographicCamera camera,Box2DDebugRenderer debugRenderer) {
        super("Blue_bird.png", x, y, 40, 40, world, 15f, 0.8f, 0.9f, 0.6f , 50,camera,debugRenderer);
    }

}
