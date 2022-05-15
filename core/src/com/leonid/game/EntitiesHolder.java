package com.leonid.game;

import com.leonid.game.domain.common.HasPhysics;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Leonid Cheremshantsev
 */
@Component
public class EntitiesHolder {

    private final Set<HasPhysics> entities = new LinkedHashSet<>();

    public void addEntity(Collection<HasPhysics> kiosks) {
        this.entities.addAll(kiosks);
    }

    public void addEntity(HasPhysics kiosk) {
        entities.add(kiosk);
    }

    public Iterator<HasPhysics> getEntities() {
        return entities.iterator();
    }

    public void remove(HasPhysics entity) {
        entities.remove(entity);
    }

    public void clear() {
        entities.clear();
    }

}
