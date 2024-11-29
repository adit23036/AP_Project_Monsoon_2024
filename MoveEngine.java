package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class MoveEngine extends ApplicationAdapter implements InputProcessor {
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private boolean isDragging = false;
    private Bird Active_bird;
    private Vector2 dragStart = new Vector2();
    private Vector2 dragEnd = new Vector2();
    private static final float MAX_DRAG_DISTANCE = 2.0f;
    private static final float FORCE_SCALING = 10f;
    private MouseJoint mouseJoint;
    private MouseJointDef mouseJointDef;
    public MoveEngine(World world , Box2DDebugRenderer debugRenderer , OrthographicCamera camera, Bird Active_bird){
        this.world = world;
        this.debugRenderer = debugRenderer;
        this.camera = camera ;
        this.Active_bird = Active_bird;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(1 / 60f, 6, 2);
        camera.update();
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, Input.Buttons.LEFT);
            touchDragged(Gdx.input.getX(), Gdx.input.getY(),0);
            touchUp(Gdx.input.getX(), Gdx.input.getY(), 0, Input.Buttons.LEFT);
        }

    }

    private Vector3 screenToWorld(int screenX, int screenY) {
        Vector3 screenCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(screenCoordinates);
        screenCoordinates.scl(1 / 100f);
        return screenCoordinates;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) { // Ensure left-click
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            Vector2 touchPos = new Vector2(worldCoords.x, worldCoords.y);

            if (Active_bird != null && Active_bird.getPosition().dst(touchPos) < 1.0f) {
                isDragging = true;
                dragStart.set(Active_bird.getPosition());
                dragEnd.set(touchPos);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && isDragging && Active_bird != null) {
            isDragging = false;
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            dragEnd.set(worldCoords.x, worldCoords.y);
            Vector2 dragVector = dragStart.cpy().sub(dragEnd);
            if (dragVector.len() > MAX_DRAG_DISTANCE) {
                dragVector.nor().scl(MAX_DRAG_DISTANCE);
            }
            Active_bird.getBody().applyLinearImpulse(
                dragVector.scl(10f),
                Active_bird.getBody().getWorldCenter(),
                true
            );
            return true;
        }
        return false;
    }
    private void applyThrowForce(float angle, float speed) {
        float angleRad = (float) Math.toRadians(angle);
        float forceX = speed * (float) Math.cos(angleRad);
        float forceY = speed * (float) Math.sin(angleRad);
        Active_bird.getBody().applyLinearImpulse(new Vector2(forceX, forceY), Active_bird.getBody().getWorldCenter(), true);
    }

    public Bird getActive_bird() {
        return Active_bird;
    }

    public void setActive_bird(Bird active_bird) {
        Active_bird = active_bird;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging && Active_bird != null) {
            Vector3 worldCoords = camera.unproject(new Vector3(screenX, screenY, 0));
            dragEnd.set(worldCoords.x, worldCoords.y);

            // Constrain drag distance
            Vector2 dragVector = dragEnd.cpy().sub(dragStart);
            if (dragVector.len() > MAX_DRAG_DISTANCE) {
                dragVector.nor().scl(MAX_DRAG_DISTANCE);
                dragEnd.set(dragStart.x + dragVector.x, dragStart.y + dragVector.y);
            }

            return true;
        }
        return false;
    }
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {return false;}
    @Override
    public boolean keyDown(int keycode) {return false;}
    @Override
    public boolean keyUp(int keycode) {return false;}
    @Override
    public boolean keyTyped(char character) {return false;}
    @Override
    public boolean mouseMoved(int screenX, int screenY) {return false;}
    @Override
    public boolean scrolled(float amountX, float amountY) {return false;}
}
