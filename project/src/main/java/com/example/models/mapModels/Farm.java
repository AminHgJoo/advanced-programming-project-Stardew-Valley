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

    public static Farm makeFarm() {
        ArrayList<Cell> farmCells = new ArrayList<>();
        ArrayList<Building> farmBuildings = new ArrayList<>();
        addBuildings(farmBuildings);
        makeEmptyCells(farmCells);
        addLake(farmCells);
        addRandomItems(farmCells);
        return new Farm(farmCells, farmBuildings);
    }

    private static void addRandomItems(ArrayList<Cell> farmCells) {
        for (Cell cell : farmCells) {
            // TODO parameters
            int randomNumber = (int) (Math.random() * 8);
            if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 3) {
                cell.setObjectOnCell(new Tree(null,null));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 2) {
                cell.setObjectOnCell(new Stone(null));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 1) {
                cell.setObjectOnCell(new ForagingCrop(false));
            }
        }
    }

    private static void addLake(ArrayList<Cell> farmCells) {
        for (int j = 37; j < 46; j++) {
            for (int i = 33; i < 41; i++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new Water(false));
            }
        }
        int randNumber = (int) (Math.random() * 3);
        if (randNumber == 1) {
            for (int j = 34; j < 40; j++) {
                for (int i = 42; i < 48; i++) {
                    Cell cell = getCellByCoordinate(i, j, farmCells);
                    cell.setObjectOnCell(new Water(false));
                }
            }
        }
    }

    private static void makeEmptyCells(ArrayList<Cell> farmCells) {
        for (int i = 0; i < 75; i++) {
            for (int j = 0; j < 50; j++) {
                Coordinate coordinate = new Coordinate(i, j);
                farmCells.add(new Cell(new EmptyCell(true), coordinate));
            }
        }
    }

    private static void addBuildings(ArrayList<Building> farmBuildings) {
        farmBuildings.add(new PlayerHome());
        farmBuildings.add(new Greenhouse());
        farmBuildings.add(new Mine());
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
