package duber.engine;

import org.joml.Vector3f;

public class Face {
    private final Vector3f normal;
    private final Vector3f[] vertices;
    private final Edge[] edges;

    public Face(Vector3f[] vertices) {
        this.vertices = vertices;
        edges = new Edge[vertices.length];
        normal = new Vector3f();

        calculateNormal();
        calculateEdges();
    }

    private Vector3f createTransformedPoint(Vector3f point, Transform transform) {
        Vector3f position = transform.getPosition();
        Vector3f rotation = transform.getRotation();
        Vector3f transformedPoint = new Vector3f(point);

        transformedPoint.mul(transform.getScale());
        transformedPoint.rotateX(rotation.x());
        transformedPoint.rotateY(rotation.y());
        transformedPoint.rotateZ(rotation.z());
        transformedPoint.add(position.x(), position.y(), position.z());

        return transformedPoint;
    }

    public Face createTransformed(Transform transform) {
        Vector3f[] transformedVertices = new Vector3f[vertices.length];
        for(int i = 0; i<vertices.length; i++){
            transformedVertices[i] = createTransformedPoint(vertices[i], transform);
        }
        return new Face(transformedVertices);
    }

    private void calculateNormal() {
        Vector3f v1 = new Vector3f(vertices[0]);
        v1.sub(vertices[1]);
        Vector3f v2 = new Vector3f(vertices[2]);
        v2.sub(vertices[1]);

        normal.set(v1).cross(v2).normalize();
    }

    private void calculateEdges() {
        for(int i = 0; i<edges.length; i++){
            int nextPointIdx = (i + 1) % edges.length;
            edges[i] = new Edge(this, vertices[i], vertices[nextPointIdx]);
        }
    }

    public Edge[] getEdges() {
        return edges;
    }

    public Vector3f[] getVertices() {
        return vertices;
    }

    public Vector3f getNormal() {
        return normal;
    }
}