package GameEngine;

import java.util.ArrayList;
import java.util.HashSet;

public class NumericMap {
    private Vector2D rightUpperCoordinates;
    private int numberOfBombs;
    private int[][] numericFields;
    public HashSet<Vector2D> mapOfBombs;

    /** ==================== KONSTRUKTORY ==================== **/

    public NumericMap(int xSize, int ySize, int bombs){
        this.rightUpperCoordinates = new Vector2D(xSize-1, ySize-1);
        this.numericFields = new int[xSize][ySize];
        this.numberOfBombs = bombs;
        this.mapOfBombs = new HashSet<>();
        this.placeBombs();
    }

    /** ======================= METODY ======================= **/

    private void placeBombs(){
        for(int i=0; i<this.numberOfBombs; i++){
            Vector2D randedCoordinates;
            do{
                randedCoordinates = Vector2D.randPosition(this.rightUpperCoordinates);
            } while(this.isFieldMined(randedCoordinates));

            this.mapOfBombs.add(randedCoordinates);
        }

        for(Vector2D pos: this.mapOfBombs){
            this.changeNumericValues(pos);
        }
    }

    private boolean isFieldMined(Vector2D position){
        return this.mapOfBombs.contains(position);
    }

    private void changeNumericValues(Vector2D position){
        ArrayList<Vector2D> listOfAdjacentPositions = position.adjacentingPositions(this.rightUpperCoordinates);

        for(Vector2D adj: listOfAdjacentPositions){
            if(!isFieldMined(adj) && this.numericFields[adj.x][adj.y]!=-1){
                this.numericFields[adj.x][adj.y]++;
            }
        }

        this.numericFields[position.x][position.y] = -1;
    }

    public HashSet<Vector2D> findPositionsToReveal(Vector2D position){
        HashSet<Vector2D> listOfPositionsToReveal = new HashSet<>();

        if(this.numericFields[position.x][position.y] == 0){
            this.findRecursivelyAdjacentingZeros(position, listOfPositionsToReveal);
        }

        return listOfPositionsToReveal;
    }

    private void findRecursivelyAdjacentingZeros(Vector2D actualPosition, HashSet<Vector2D> listOfPositionsToReveal){
        listOfPositionsToReveal.add(actualPosition);

        Vector2D[] neighboursZerosList = {
                actualPosition.add(new Vector2D(1,0)),
                actualPosition.add(new Vector2D(0,1)),
                actualPosition.add(new Vector2D(-1,0)),
                actualPosition.add(new Vector2D(0,-1))
        };

        for(int i=0; i<neighboursZerosList.length; i++){
            if(neighboursZerosList[i].follows(new Vector2D(0,0))
                    && neighboursZerosList[i].precedes(this.rightUpperCoordinates)
                    && this.numericFields[neighboursZerosList[i].x][neighboursZerosList[i].y] == 0
                    && !listOfPositionsToReveal.contains(neighboursZerosList[i]))
                findRecursivelyAdjacentingZeros(neighboursZerosList[i], listOfPositionsToReveal);
        }

        findNumbersAdjacentingToZeros(actualPosition, listOfPositionsToReveal);
    }

    private void findNumbersAdjacentingToZeros(Vector2D actualPosition, HashSet<Vector2D> listOfPositionsToReveal){
        Vector2D[] neighboursList = {
                actualPosition.add(new Vector2D(1,0)),
                actualPosition.add(new Vector2D(0,1)),
                actualPosition.add(new Vector2D(-1,0)),
                actualPosition.add(new Vector2D(0,-1)),
                actualPosition.add(new Vector2D(1,1)),
                actualPosition.add(new Vector2D(-1,1)),
                actualPosition.add(new Vector2D(1,-1)),
                actualPosition.add(new Vector2D(-1,-1))
        };

        for(int i=0; i<neighboursList.length; i++){
            if(neighboursList[i].follows(new Vector2D(0,0))
                    && neighboursList[i].precedes(this.rightUpperCoordinates)
                    && this.numericFields[neighboursList[i].x][neighboursList[i].y] > 0
                    && !listOfPositionsToReveal.contains(neighboursList[i]))
                listOfPositionsToReveal.add(neighboursList[i]);
        }
    }

    public int getValueFromPosition(Vector2D position){
        return this.numericFields[position.x][position.y];
    }
}
