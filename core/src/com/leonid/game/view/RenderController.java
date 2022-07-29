package com.leonid.game.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.leonid.game.EntityHolder;
import com.leonid.game.domain.common.HasPhysics;
import com.leonid.game.domain.customer.Customer;
import com.leonid.game.domain.home.Home;
import com.leonid.game.domain.kiosk.Kiosk;
import com.leonid.game.view.rander.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;

/**
 * @author Leonid Cheremshantsev
 */
@Configuration
@Component
public class RenderController {

    private final Logger log = LoggerFactory.getLogger(RenderController.class);

    private final StopWatch stopWatch;
    @SuppressWarnings("rawtypes")
    @Autowired
    private List<Renderer> renderers;
    @Autowired
    private EntityHolder holder;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;

    public RenderController() {
        stopWatch = new StopWatch("Rendering");
    }

    @SuppressWarnings({"unchecked", "rawtypes"})

    public List<HasPhysics> queue = new ArrayList<>();

    private Comparator<HasPhysics> getRenderingComparator() {
        return (o1, o2) -> {
            if (o1 instanceof Customer) {
                return -1;
            } else if (o2 instanceof Customer) {
                return 1;
            } else if (o1 instanceof Home && o2 instanceof Kiosk) {
                return -1;
            } else if (o1 instanceof Kiosk && o2 instanceof Home) {
                return 1;
            } else {
                return 0;
            }
        };
    }

    private LocalTime printAt = LocalTime.now().plusMinutes(1);

    private int lastCustomersCount = 0;
    private int lastHomesCount = 0;
    private int lastKiosksCount = 0;

    public void render() {
        stopWatch.start();


        lastCustomersCount = 0;
        lastHomesCount = 0;
        lastKiosksCount = 0;

        holder.getEntities().forEachRemaining(entity -> {
            if (!entity.isVisible()) {
                return;
            }

            if (entity instanceof Customer) {
                lastCustomersCount++;
            } else if (entity instanceof Home) {
                lastHomesCount++;
            } else if (entity instanceof Kiosk) {
                lastKiosksCount++;
            }
            queue.add(entity);
        });

        queue.sort(getRenderingComparator());

        shapeRenderer.begin(Filled);

        queue.forEach(this::render);

        shapeRenderer.end();

        queue.clear();


        stopWatch.stop();
        printPerformanceStatisticIfNeeded();
    }

    private void printPerformanceStatisticIfNeeded() {
        if (LocalTime.now().isAfter(printAt)) {
            printAt = LocalTime.now().plusMinutes(1);
            log.info("Rendering time avg: {} ns", formatNanos(Math.round(1.0f * stopWatch.getTotalTimeNanos() / stopWatch.getTaskCount())));
            log.info("Last render: {} ns. Total objects: {}. Customers: {}, Homes: {}, Kiosks: {}.",
                    formatNanos(stopWatch.getLastTaskTimeNanos()),
                    lastCustomersCount + lastHomesCount + lastKiosksCount,
                    lastCustomersCount,
                    lastHomesCount,
                    lastKiosksCount);
        }
    }

    private String formatNanos(long nanos) {
        String s = String.valueOf(nanos);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = s.length() - 1; i >= 0; i--) {
            if ((s.length() - 1 - i) % 3 == 0 && i != s.length() - 1) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(s.charAt(i));
        }

        return stringBuilder.reverse().toString();
    }


    private void render(HasPhysics entity) {
        for (Renderer renderer : renderers) {
            if (renderer.supportMasterClass(entity.getClass())) {
                renderer.init(shapeRenderer, batch, font, holder);
                renderer.render(entity);
            }
        }
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }
}
