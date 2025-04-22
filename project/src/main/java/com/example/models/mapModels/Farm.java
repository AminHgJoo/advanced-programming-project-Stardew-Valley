package com.example.models.mapModels;

import com.example.models.App;
import com.example.models.buildings.Building;
import com.example.models.buildings.Greenhouse;
import com.example.models.buildings.Mine;
import com.example.models.buildings.PlayerHome;
import com.example.models.enums.types.ForagingCropsType;
import com.example.models.enums.types.ForagingMineralsType;
import com.example.models.enums.types.ForagingSeedsType;
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
                cell.setObjectOnCell(new ForagingMineral(ForagingMineralsType.STONE, "gray", "Stone"));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 1) {
                cell.setObjectOnCell(randomForagingCrop());
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 4 && isMineCell(cell)) {
                cell.setObjectOnCell(randomForagingMineral());
            }
        }
    }

    public void foragingRefresh() {
        /// makes sure the map doesn't get crowded.
        int count = 0;
        for (Cell cell : cells) {
            if (!(cell.getObjectOnCell() instanceof EmptyCell)) {
                count++;
            }
        }
        if (count >= 2500) {
            return;
        }

        for (Cell cell : cells) {
            int randomNumber = (int) (Math.random() * 100);
            if (cell.getObjectOnCell().type.equals("empty") && cell.isTilled() && randomNumber == 3) {
                cell.setObjectOnCell(randomForagingSeed());
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 3) {
                cell.setObjectOnCell(randomForagingCrop());
            }
            int randomNumber2 = (int) (Math.random() * 15);
            if (cell.getObjectOnCell().type.equals("empty") && randomNumber2 == 2 && isMineCell(cell)) {
                cell.setObjectOnCell(randomForagingMineral());
            }
        }
    }

    private static ForagingSeed randomForagingSeed() {
        ForagingSeedsType[] values = ForagingSeedsType.values();
        int randomNumber = (int) (Math.random() * values.length);
        return new ForagingSeed(values[randomNumber]);
    }

    private static ForagingCrop randomForagingCrop() {
        int randomNumber = (int) (Math.random() * 22);
        if (randomNumber == 0)
            return new ForagingCrop(ForagingCropsType.WINTER_ROOT, true);
        else if (randomNumber == 1)
            return new ForagingCrop(ForagingCropsType.BLACKBERRY, true);
        else if (randomNumber == 2)
            return new ForagingCrop(ForagingCropsType.COMMON_MUSHROOM, true);
        if (randomNumber == 3)
            return new ForagingCrop(ForagingCropsType.CHANTERELLE, true);
        else if (randomNumber == 4)
            return new ForagingCrop(ForagingCropsType.CROCUS, true);
        else if (randomNumber == 5)
            return new ForagingCrop(ForagingCropsType.CRYSTAL_FRUIT, true);
        if (randomNumber == 6)
            return new ForagingCrop(ForagingCropsType.DAFFODIL, true);
        else if (randomNumber == 7)
            return new ForagingCrop(ForagingCropsType.DANDELION, true);
        else if (randomNumber == 8)
            return new ForagingCrop(ForagingCropsType.FIDDLE_HEAD_FERN, true);
        if (randomNumber == 9)
            return new ForagingCrop(ForagingCropsType.GRAPE, true);
        else if (randomNumber == 10)
            return new ForagingCrop(ForagingCropsType.HAZELNUT, true);
        else if (randomNumber == 11)
            return new ForagingCrop(ForagingCropsType.HOLLY, true);
        if (randomNumber == 12)
            return new ForagingCrop(ForagingCropsType.LEEK, true);
        else if (randomNumber == 13)
            return new ForagingCrop(ForagingCropsType.MOREL, true);
        else if (randomNumber == 14)
            return new ForagingCrop(ForagingCropsType.PURPLE_MUSHROOM, true);
        if (randomNumber == 15)
            return new ForagingCrop(ForagingCropsType.RED_MUSHROOM, true);
        else if (randomNumber == 16)
            return new ForagingCrop(ForagingCropsType.SALMON_BERRY, true);
        else if (randomNumber == 17)
            return new ForagingCrop(ForagingCropsType.SNOW_YAM, true);
        if (randomNumber == 18)
            return new ForagingCrop(ForagingCropsType.SPICE_BERRY, true);
        else if (randomNumber == 19)
            return new ForagingCrop(ForagingCropsType.SPRING_ONION, true);
        else if (randomNumber == 20)
            return new ForagingCrop(ForagingCropsType.SWEET_PEA, true);
        if (randomNumber == 21)
            return new ForagingCrop(ForagingCropsType.WILD_HORSERADISH, true);

        return new ForagingCrop(ForagingCropsType.WILD_PLUM, true);
    }

    private static ForagingMineral randomForagingMineral() {
        ForagingMineralsType[] values = ForagingMineralsType.values();
        int randomNumber = (int) (Math.random() * values.length);
        return new ForagingMineral(values[randomNumber]);
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
        return cell.getCoordinate().getX() <= 9 && cell.getCoordinate().getX() >= 0 && cell.getCoordinate().getY() <= 11 && cell.getCoordinate().getY() >= 0;
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
