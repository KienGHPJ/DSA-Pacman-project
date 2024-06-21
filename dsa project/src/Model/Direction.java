package Model;
public enum Direction {
    UP(0, -1, 90),
    DOWN(0, 1, 270),
    LEFT(-1, 0, 180),
    RIGHT(1, 0, 0);

    private final int dx, dy;
	final int mouthOffset;

    Direction(int dx, int dy, int mouthOffset) {
        this.dx = dx;
        this.dy = dy;
        this.mouthOffset = mouthOffset;
    }

    public int getDx() { return dx; }
    public int getDy() { return dy; }
    public int getMouthOffset() { return mouthOffset; }
}
