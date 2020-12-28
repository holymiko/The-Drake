package thedrake;

public final class Offset2D {
    public final int x;
    public final int y;

    public Offset2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equalsTo(int x, int y){
        return this.x == x && this.y == y;
    }

    public Offset2D yFlipped(){
        return new Offset2D(this.x, -this.y);
    }
}
