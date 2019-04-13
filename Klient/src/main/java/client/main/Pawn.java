package client.main;

/**
 * Created by Jakub Kmita on 03.01.2018.
 */
class Pawn {
    private int x;
    private int y;

    Pawn(int y, int x) {
        this.x = x;
        this.y = y;
    }

    void move(int y, int x) {
        this.x = x;
        this.y = y;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

}
