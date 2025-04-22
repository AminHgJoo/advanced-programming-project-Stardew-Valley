package com.example.models.mapModels;

import com.example.models.App;
import com.example.models.buildings.Building;
import com.example.models.buildings.Greenhouse;
import com.example.models.buildings.Mine;
import com.example.models.buildings.PlayerHome;
import com.example.models.enums.types.TreeType;
import com.example.models.mapObjects.*;
import dev.morphia.annotations.Embedded;

import java.util.ArrayList;

@Embedded
public class Farm {
    private ArrayList<Cell> cells;
    private ArrayList<Building> buildings;

    public Farm() {

    }

    public Farm(ArrayList<Cell> cells, ArrayList<Building> buildings) {
        this.cells = cells;
        this.buildings = buildings;
    }

    public void showFarm(int x, int y, int size) {
        int playerX = App.getLoggedInUser().getCurrentGame().getCurrentPlayer().getCoordinate().getX();
        int playerY = App.getLoggedInUser().getCurrentGame().getCurrentPlayer().getCoordinate().getY();

        for (Cell cell : cells) {
            Coordinate coordinate = cell.getCoordinate();

            int xOfCell = coordinate.getX();
            int yOfCell = coordinate.getY();

            if (Math.abs(x - xOfCell) <= size / 2 && Math.abs(y - yOfCell) <= size / 2) {
                if (xOfCell == playerX && yOfCell == playerY)
                    System.out.print("\u001B[34m " + "P" + "\033[0m");
                else if (cell.getObjectOnCell().color.equals("blue"))
                    System.out.print("\u001B[34m " + cell.getObjectOnCell().toString() + "\033[0m");
                else if (cell.getObjectOnCell().color.equals("red"))
                    System.out.print("\u001B[31m " + cell.getObjectOnCell().toString() + "\033[0m");
                else if (cell.getObjectOnCell().color.equals("green"))
                    System.out.print("\u001B[32m " + cell.getObjectOnCell().toString() + "\033[0m");
                else if (cell.getObjectOnCell().color.equals("yellow"))
                    System.out.print("\u001B[33m " + cell.getObjectOnCell().toString() + "\033[0m");
                else if (cell.getObjectOnCell().color.equals("black"))
                    System.out.print("\u001B[90m " + cell.getObjectOnCell().toString() + "\033[0m");
                else if (cell.getObjectOnCell().color.equals("gray"))
                    System.out.print("\u001B[37m " + cell.getObjectOnCell().toString() + "\033[0m");
                if (xOfCell - x == size / 2) {
                    System.out.print("\n");
                }
            }
        }
    }

    /// Debug method.
    public void showEntireFarm() {
        int playerX = App.getLoggedInUser().getCurrentGame().getCurrentPlayer().getCoordinate().getX();
        int playerY = App.getLoggedInUser().getCurrentGame().getCurrentPlayer().getCoordinate().getY();

        for (int i = 0; i < 152; i++) {
            System.out.print("_");
        }
        System.out.println();

        int cellIndex = 0;

        for (Cell cell : cells) {
            if (cellIndex % 75 == 0)
                System.out.print("|");

            if (cell.getCoordinate().getX() == playerX && cell.getCoordinate().getY() == playerY)
                System.out.print("\u001B[34m " + "P" + "\033[0m");
            else if (cell.getObjectOnCell().color.equals("blue"))
                System.out.print("\u001B[34m " + cell.getObjectOnCell().toString() + "\033[0m");
            else if (cell.getObjectOnCell().color.equals("red"))
                System.out.print("\u001B[31m " + cell.getObjectOnCell().toString() + "\033[0m");
            else if (cell.getObjectOnCell().color.equals("green"))
                System.out.print("\u001B[32m " + cell.getObjectOnCell().toString() + "\033[0m");
            else if (cell.getObjectOnCell().color.equals("yellow"))
                System.out.print("\u001B[33m " + cell.getObjectOnCell().toString() + "\033[0m");
            else if (cell.getObjectOnCell().color.equals("black"))
                System.out.print("\u001B[90m " + cell.getObjectOnCell().toString() + "\033[0m");
            else if (cell.getObjectOnCell().color.equals("gray"))
                System.out.print("\u001B[37m " + cell.getObjectOnCell().toString() + "\033[0m");

            cellIndex++;
            if (cellIndex % 75 == 0)
                System.out.println("|");
        }
        for (int i = 0; i < 152; i++) {
            System.out.print("_");
        }
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public static Farm makeFarm(int lakeModifier) {
        ArrayList<Cell> farmCells = new ArrayList<>();
        ArrayList<Building> farmBuildings = new ArrayList<>();
        makeEmptyCells(farmCells);
        addBuildings(farmBuildings, farmCells);
        if (lakeModifier == 1)
            addOneLake(farmCells);
        else
            addTwoLakes(farmCells);

        addRandomItems(farmCells);
        return new Farm(farmCells, farmBuildings);
    }

    private static void addRandomItems(ArrayList<Cell> farmCells) {
        for (Cell cell : farmCells) {
            // TODO parameters
            int randomNumber = (int) (Math.random() * 8);
            if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 3) {
                cell.setObjectOnCell(new Tree(TreeType.NORMAL_TREE));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 2) {
                cell.setObjectOnCell(new Stone());
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 1) {
                cell.setObjectOnCell(new ForagingCrop());
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 4) {
                cell.setObjectOnCell(new ForagingSeed());
            }
        }
    }

    public void foragingRefresh(){
        for(Cell cell : cells){
            int randomNumber = (int) (Math.random() * 100);
            if(cell.getObjectOnCell().type.equals("empty") && cell.isTilled() && randomNumber == 3){
                cell.setObjectOnCell(new ForagingSeed());
            }
            else if(cell.getObjectOnCell().type.equals("empty") && randomNumber == 3){
                cell.setObjectOnCell(new ForagingCrop());
            }
            int randomNumber2 = (int) (Math.random() * 20);
            if(cell.getObjectOnCell().type.equals("empty") && randomNumber2 == 2 ){}
        }
    }

    private static void addOneLake(ArrayList<Cell> farmCells) {
        for (int j = 37; j < 46; j++) {
            for (int i = 33; i < 41; i++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new Water());
            }
        }
    }

    private static void addTwoLakes(ArrayList<Cell> farmCells) {
        for (int j = 37; j < 46; j++) {
            for (int i = 33; i < 41; i++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new Water());
            }
        }
        for (int j = 34; j < 40; j++) {
            for (int i = 42; i < 48; i++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new Water());
            }
        }
    }


    private static void makeEmptyCells(ArrayList<Cell> farmCells) {
        for (int j = 0; j < 50; j++) {
            for (int i = 0; i < 75; i++) {
                Coordinate coordinate = new Coordinate(i, j);
                farmCells.add(new Cell(new EmptyCell(), coordinate));
            }
        }
    }

    private static boolean isMineCell(Cell cell) {
        return cell.getCoordinate().getX() <=9 && cell.getCoordinate().getX() >=0 && cell.getCoordinate().getY() <=11 && cell.getCoordinate().getY() >=0;
    }

    private static void addBuildings(ArrayList<Building> farmBuildings, ArrayList<Cell> farmCells) {
        ArrayList<Cell> playerHomeCells = new ArrayList<>();
        ArrayList<Cell> greenHouseCells = new ArrayList<>();
        ArrayList<Cell> mineCells = new ArrayList<>();
        for (int i = 61; i < 65; i++) {
            for (int j = 4; j < 8; j++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new BuildingBlock(false));
                playerHomeCells.add(cell);
            }
        }
        for (int i = 22; i < 29; i++) {
            for (int j = 3; j < 11; j++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                if (i != 22 && i != 28 && j != 3 && j != 10) {
                    cell.setObjectOnCell(new BuildingBlock(false));
                } else
                    cell.setObjectOnCell(new BuildingBlock(true));
                greenHouseCells.add(cell);
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 12; j++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new BuildingBlock(true));
                mineCells.add(cell);
            }
        }
        farmBuildings.add(new PlayerHome(playerHomeCells));
        farmBuildings.add(new Greenhouse(greenHouseCells));
        farmBuildings.add(new Mine(mineCells));
    }

    private static Cell getCellByCoordinate(int x, int y, ArrayList<Cell> cells) {
        for (Cell cell : cells) {
            if (cell.getCoordinate().getX() == x && cell.getCoordinate().getY() == y) {
                return cell;
            }
        }
        return null;
    }

    public Cell findCellByCoordinate(int x, int y) {
        for (Cell cell : cells) {
            if (cell.getCoordinate().getX() == x && cell.getCoordinate().getY() == y) {
                return cell;
            }
        }
        return null;
    }

    /// For pathfinding only.
    public void initialCells() {
        for (Cell cell : cells) {
            cell.energy = 0;
            cell.distance = 0;
            cell.turns = 0;
            cell.prev = null;
        }
    }
}
