package com.leonid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.leonid.game.view.RenderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.Input.Keys.R;
import static com.badlogic.gdx.graphics.Color.BLACK;
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
	private SpriteBatch entityBatch;
	private SpriteBatch statisticBatch;
	private BitmapFont font;
	private ExtendViewport viewport;
	private ShapeRenderer shapeRenderer;
	private ZoomInputProccesor zoomInputProccesor;

	@Override
	public void create() {
		camera = new OrthographicCamera();

		font = new BitmapFont();

		entityBatch = new SpriteBatch();

		statisticBatch = new SpriteBatch();

		viewport = new ExtendViewport(WIDTH, HEIGHT, camera);

		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		renderController.setShapeRenderer(shapeRenderer);
		renderController.setBatch(entityBatch);
		renderController.setFont(font);

		zoomInputProccesor = new ZoomInputProccesor(camera);
		zoomInputProccesor.init();
		Gdx.input.setInputProcessor(zoomInputProccesor);
	}

	@Override
	public void render() {
		processInput();
		setProjectionMatrix();
		draw();
		tic();
	}

	private void tic() {
		context.tic();
	}

	private void setProjectionMatrix() {
		entityBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}

	private void draw() {
		clear(1, 1, 1, 1);

		drawEntities();

		drawMenu();
	}

	private void drawMenu() {
		statisticBatch.begin();

		font.setColor(BLACK);
		font.draw(statisticBatch, String.format("Dead/Alive: %s/%s", context.deadKioskCount(), context.getKioskCount() - context.deadKioskCount()), 10, 20);
		font.draw(statisticBatch, String.format("Max Kiosk Level: %s", context.getMaxKioskLevel()), 10, 50);
		font.draw(statisticBatch, String.format("Customers: %s", context.getCustomerCount()), 10, 80);

		statisticBatch.end();
	}

	private void drawEntities() {
		shapeRenderer.begin(Filled);
		entitiesHolder.getEntities().forEachRemaining(entity -> {
			renderController.render(entity);
		});
		shapeRenderer.end();
	}

	private void processInput() {
		processRestart();
		processCameraControl();
	}

	private void processRestart() {
		if (Gdx.input.isKeyJustPressed(R)) {
			context.reinit();
		}
	}

	private void processCameraControl() {
		processZoom();
		processCameraPosition();

		camera.update();
	}

	private void processCameraPosition() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			camera.position.set(new Vector3());
		}

		int move = 25;
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.position.set(camera.position.x, camera.position.y + move, camera.position.z);
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.position.set(camera.position.x, camera.position.y - move, camera.position.z);
		}


		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.position.set(camera.position.x - move, camera.position.y, camera.position.z);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.position.set(camera.position.x + move, camera.position.y, camera.position.z);
		}
	}

	private void processZoom() {
		boolean mKeyPressed = Gdx.input.isKeyJustPressed(Input.Keys.M);
		boolean nKeyPressed = Gdx.input.isKeyJustPressed(Input.Keys.N);

		if (mKeyPressed || nKeyPressed) {
			zoomInputProccesor.checkZoom(mKeyPressed ? Input.Keys.M : Input.Keys.N);
		}
	}


	@Override
	public void dispose() {
		entityBatch.dispose();
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
		return entityBatch;
	}

	@Bean
	public BitmapFont bitmapFont() {
		return font;
	}
}
