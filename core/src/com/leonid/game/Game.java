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
import com.leonid.game.calc.Calculator;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.domain.kiosk.KioskContext;
import com.leonid.game.domain.kiosk.KioskDeadState;
import com.leonid.game.domain.kiosk.state.KioskWaitingState;
import com.leonid.game.view.RenderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.Input.Keys.R;
import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.utils.ScreenUtils.clear;
import static com.leonid.game.domain.kiosk.KioskStatus.DEAD;

@Component
public class Game extends ApplicationAdapter {

	public static final int WIDTH = 1080 * 4;
	public static final int HEIGHT = WIDTH / 4 * 3;


	@Autowired
	private GameContext context;
	@Autowired
	private EntityHolder holder;
	@Autowired
	private ApplicationContext app;
	@Autowired
	private RenderController renderController;
	@Autowired
	private Calculator calculator;
	private OrthographicCamera camera;
	private SpriteBatch entityBatch;
	private SpriteBatch statisticBatch;
	private BitmapFont font;
	private ExtendViewport viewport;
	private ShapeRenderer shapeRenderer;
	private ZoomInputProcessor zoomInputProcessor;

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

		zoomInputProcessor = new ZoomInputProcessor(camera);
		zoomInputProcessor.init();
		Gdx.input.setInputProcessor(zoomInputProcessor);
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

		drawStatistic();
	}

	private void drawStatistic() {
		statisticBatch.begin();

		font.setColor(BLACK);
		font.draw(statisticBatch, String.format("Dead/Alive: %s/%s", context.deadKioskCount(), context.getKioskCount() - context.deadKioskCount()), 10, 20);
		font.draw(statisticBatch, String.format("Max Kiosk Level: %s", context.getMaxKioskLevel()), 10, 50);
		font.draw(statisticBatch, String.format("Customers: %s", context.getCustomerCount()), 10, 80);

		statisticBatch.end();
	}

	private void drawEntities() {
		renderController.render();
	}

	private void processInput() {
		processRestart();
		processCameraControl();

		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
			int x = Gdx.input.getX();
			int y = Gdx.input.getY();

			Vector3 realMouse = getRealMouse(x, y);

			HasPhysics entityByClick = calculator.getEntityByClick(realMouse.x, realMouse.y);

			if (entityByClick instanceof Kiosk) {
				Kiosk kiosk = (Kiosk) entityByClick;

				KioskContext kioskContext = holder.getContext(kiosk);
				if (kiosk.getStatus() == DEAD) {
					app.getBean(KioskWaitingState.class, kioskContext);
				} else {
					app.getBean(KioskDeadState.class, kioskContext);
				}
			}
		}
	}

	private Vector3 getRealMouse(float x, float y) {
		Vector3 realMouse = new Vector3(x, y, 0);
		camera.unproject(realMouse);
		return realMouse;
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
		boolean mKeyPressed = Gdx.input.isKeyPressed(Input.Keys.M);
		boolean nKeyPressed = Gdx.input.isKeyPressed(Input.Keys.N);

		if (mKeyPressed || nKeyPressed) {
			zoomInputProcessor.checkZoom(mKeyPressed ? Input.Keys.M : Input.Keys.N);
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
