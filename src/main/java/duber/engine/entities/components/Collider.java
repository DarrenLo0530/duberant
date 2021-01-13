package duber.engine.entities.components;

import java.util.ArrayList;
import java.util.List;

public class Collider extends Component {
    private final List<ColliderPart> colliderParts;
    private ColliderPart baseCollider;

    public Collider() {
        colliderParts = new ArrayList<>();
    }

    public List<ColliderPart> getColliderParts() {
        return colliderParts;
    }

    public boolean hasColliderParts() {
        return !colliderParts.isEmpty();
    }

    public void setBaseCollider(ColliderPart baseCollider) {
        addColliderPart(baseCollider);
        this.baseCollider = baseCollider;
    }

    public ColliderPart getBaseCollider() {
        return baseCollider;
    }

    public void addColliderPart(ColliderPart colliderPart) {
        colliderParts.add(colliderPart);
        colliderPart.setCollider(this);
    }
}