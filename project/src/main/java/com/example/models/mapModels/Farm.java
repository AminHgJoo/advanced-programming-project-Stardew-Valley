package com.example.models.mapModels;

import com.example.models.buildings.Building;
import com.example.models.buildings.Greenhouse;
import com.example.models.buildings.Mine;
import com.example.models.buildings.PlayerHome;
import com.example.models.mapObjects.*;

import java.util.ArrayList;

public class Farm {
    final private ArrayList<Cell> cells;
    final private ArrayList<Building> buildings;

    public Farm(ArrayList<Cell> cells, ArrayList<Building> buildings) {
        this.cells = cells;
        this.buildings = buildings;
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
                cell.setObjectOnCell(new Tree(null, null));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 2) {
                cell.setObjectOnCell(new Stone(null));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 1) {
                cell.setObjectOnCell(new ForagingCrop());
            }
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
        for (int i = 0; i < 75; i++) {
            for (int j = 0; j < 50; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                farmCells.add(new Cell(new EmptyCell(), coordinate));
            }
        }
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
}
