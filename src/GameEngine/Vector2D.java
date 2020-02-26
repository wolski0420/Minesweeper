package GameEngine;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Vector2D {
    public int x;
    public int y;

    /** ==================== KONSTRUKTORY ==================== **/

    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    /** ======================= METODY ======================= **/

    public static Vector2D randPosition(Vector2D rightUpper){
        Random rand = new Random();
        int x = rand.nextInt(rightUpper.x+1);
        int y = rand.nextInt(rightUpper.y+1);
        return new Vector2D(x,y);
    }

    public ArrayList<Vector2D> adjacentingPositions(Vector2D rightUpper){
        Vector2D[] toAddList = {
                this.add(new Vector2D(0,1)),
                this.add(new Vector2D(0,-1)),
                this.add(new Vector2D(1,1)),
                this.add(new Vector2D(-1,1)),
                this.add(new Vector2D(1,-1)),
                this.add(new Vector2D(-1,-1)),
                this.add(new Vector2D(1,0)),
                this.add(new Vector2D(-1,0))
        };

        ArrayList<Vector2D> listToReturn = new ArrayList<>();

        for(int i=0; i<8; i++)
            if(toAddList[i].follows(new Vector2D(0,0)) && toAddList[i].precedes(rightUpper))
                listToReturn.add(toAddList[i]);

        return listToReturn;
    }

    public Vector2D add(Vector2D toADD){
        return new Vector2D(this.x + toADD.x, this.y + toADD.y);
    }

    public boolean precedes(Vector2D toCompare){
        return this.x <= toCompare.x && this.y <= toCompare.y;
    }

    public boolean follows(Vector2D toCompare){
        return this.x >= toCompare.x && this.y >= toCompare.y;
    }

    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Vector2D)) return false;
        Vector2D v = (Vector2D) other;
        return this.x == v.x && this.y == v.y;
    }
}