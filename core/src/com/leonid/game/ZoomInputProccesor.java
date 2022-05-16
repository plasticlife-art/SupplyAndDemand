package com.leonid.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author Leonid Cheremshantsev
 */
public class ZoomInputProccesor extends InputMultiplexer {

    public static final float DEFAULT_ZOOM = 0.2f;
    public static final int MAX_SCALE = 1000;

    private final OrthographicCamera camera;

    public ZoomInputProccesor(OrthographicCamera camera) {
        super();
        this.camera = camera;
    }

    public void init() {
        camera.zoom = DEFAULT_ZOOM;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom += amountY * 0.1;
        adjustZoom();
        return true;
    }

    public void checkZoom(int key) {
        if (Input.Keys.N == key) {
            camera.zoom -= 0.1;
            adjustZoom();
        }

        if (Input.Keys.M == key) {
            camera.zoom += 0.1;
            adjustZoom();
        }
    }

    private void adjustZoom() {
        if (camera.zoom < 0.1) {
            camera.zoom = 0.1f;
        } else if (camera.zoom > MAX_SCALE) {
            camera.zoom = MAX_SCALE;
        }
    }

}
