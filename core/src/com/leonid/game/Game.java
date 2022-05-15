package com.leonid.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.view.KioskRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.badlogic.gdx.Input.Keys.R;

@Component
public class Game extends ApplicationAdapter {

	public static final int WIDTH = 1080;
	public static final int HEIGHT = WIDTH / 4 * 3;


	@Autowired
	private GameContext context;
	@Autowired
	private EntitiesHolder entitiesHolder;

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
	}

	@Override
	public void render() {
		processRestart();

		batch.setProjectionMatrix(camera.combined);

		draw();

		context.tic();
	}

	private void draw() {
		ScreenUtils.clear(1, 1, 1, 1);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		entitiesHolder.getEntities().forEachRemaining(entity -> {
			if (entity instanceof Kiosk) {
				getKioskRenderer().render(context.getKioskContext((Kiosk) entity));
			} else {
				shapeRenderer.setColor(entity.getColor());
				shapeRenderer.circle(entity.getPosition().getX(), entity.getPosition().getY(), entity.getSize());
			}

		});
		shapeRenderer.end();
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
	public KioskRenderer getKioskRenderer() {
		return new KioskRenderer(shapeRenderer, batch, font);
	}
}
