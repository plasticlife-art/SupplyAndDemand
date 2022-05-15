package com.leonid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.view.RenderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.Input.Keys.R;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.badlogic.gdx.utils.ScreenUtils.clear;

@Component
public class Game extends ApplicationAdapter {

	public static final int WIDTH = 1080;
	public static final int HEIGHT = WIDTH / 4 * 3;


	@Autowired
	private GameContext context;
	@Autowired
	private EntitiesHolder entitiesHolder;
	@Autowired
	private RenderController renderController;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private ExtendViewport viewport;
	private ShapeRenderer shapeRenderer;

	@Override
	public void create() {
		camera = new OrthographicCamera();
		font = new BitmapFont();
		batch = new SpriteBatch();
		viewport = new ExtendViewport(WIDTH, HEIGHT, camera);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		renderController.setShapeRenderer(shapeRenderer);
		renderController.setBatch(batch);
		renderController.setFont(font);
	}

	@Override
	public void render() {
		processRestart();
		setProjectionMatrix();
		draw();
		tic();
	}

	private void tic() {
		context.tic();
	}

	private void setProjectionMatrix() {
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	private void draw() {
		clear(1, 1, 1, 1);
		shapeRenderer.begin(Filled);
		entitiesHolder.getEntities().forEachRemaining(entity -> {
			if (entity instanceof Kiosk) {
				drawKiosk((Kiosk) entity);
			} else if (entity instanceof Customer) {
				drawCustomer(((Customer) entity));
			}
		});
		shapeRenderer.end();
	}

	private void drawCustomer(Customer entity) {
		renderController.render(context.getContext(entity));
	}

	private void drawKiosk(Kiosk entity) {
		renderController.render(context.getContext(entity));
	}

	private void processRestart() {
		if (Gdx.input.isKeyJustPressed(R)) {
			context.reinit();
			camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeRenderer.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	@Bean
	public ShapeRenderer shapeRenderer() {
		return shapeRenderer;
	}

	@Bean
	public SpriteBatch spriteBatch() {
		return batch;
	}

	@Bean
	public BitmapFont bitmapFont() {
		return font;
	}
}
