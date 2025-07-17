package com.common.models.mapModels;

import com.common.models.Animal;
import com.common.models.GameData;
import com.common.models.Player;
import com.common.models.buildings.*;
import com.common.models.enums.types.itemTypes.CropSeedsType;
import com.common.models.enums.types.itemTypes.ForagingMineralsType;
import com.common.models.enums.types.mapObjectTypes.ArtisanBlockType;
import com.common.models.enums.types.mapObjectTypes.ForagingCropsType;
import com.common.models.enums.types.mapObjectTypes.TreeType;
import com.common.models.mapObjects.*;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Embedded
public class Farm {
    //TODO: When greenhouse gets built no map tiles should get overridden!
    private static int lastFarmNumber;

    static {
        lastFarmNumber = 0;
    }

    private ArrayList<Cell> cells;
    private ArrayList<Building> buildings;
    private int farmNumber;

    public Farm() {

    }

    public Farm(ArrayList<Cell> cells, ArrayList<Building> buildings) {
        this.cells = cells;
        this.buildings = buildings;
        this.farmNumber = lastFarmNumber;
        lastFarmNumber++;
    }

    public static Farm makeFarm(int lakeModifier, GameData gameData) {
        ArrayList<Cell> farmCells = new ArrayList<>();
        ArrayList<Building> farmBuildings = new ArrayList<>();
        makeEmptyCells(farmCells);
        addBuildings(farmBuildings, farmCells);
        if (lakeModifier == 1)
            addOneLake(farmCells);
        else
            addTwoLakes(farmCells);

        addRandomItems(farmCells, gameData);
        return new Farm(farmCells, farmBuildings);
    }

    private static void addRandomItems(ArrayList<Cell> farmCells, GameData gameData) {
        for (Cell cell : farmCells) {
            int randomNumber = (int) (Math.random() * 50);
            if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 3) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                //TODO: Plz kill me.
                cell.setObjectOnCell(new Tree(TreeType.NORMAL_TREE, LocalDateTime.parse("2025-01-01 09:00:00", dateTimeFormatter), gameData));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 2) {
                cell.setObjectOnCell(new ForagingMineral(ForagingMineralsType.STONE, "gray", "Stone"));
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 1) {
                cell.setObjectOnCell(randomForagingCrop(gameData));
            } else if (cell.getObjectOnCell().type.equals("Mine") && (randomNumber == 4 || randomNumber == 3) && isMineCell(cell)) {
                cell.setObjectOnCell(randomForagingMineral());
            }
        }
    }

    private static ForagingCrop randomForagingCrop(GameData gameData) {
        ForagingCropsType[] allValues = ForagingCropsType.values();
        ArrayList<ForagingCropsType> validValues = new ArrayList<>();

        for (int i = 0; i < allValues.length; i++) {
            if (allValues[i].getSeasons().length > 1 || allValues[i].getSeasons()[0] == gameData.getSeason()) {
                validValues.add(allValues[i]);
            }
        }

        int randomNumber = (int) (Math.random() * validValues.size());
        return new ForagingCrop(validValues.get(randomNumber), true);
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
        return cell.getCoordinate().getX() <= 9 && cell.getCoordinate().getY() <= 11;
    }

    private static void addBuildings(ArrayList<Building> farmBuildings, ArrayList<Cell> farmCells) {
        ArrayList<Cell> playerHomeCells = new ArrayList<>();
        ArrayList<Cell> greenHouseCells = new ArrayList<>();
        ArrayList<Cell> mineCells = new ArrayList<>();
        for (int i = 61; i <= 65; i++) { // house is 5 by 5
            for (int j = 4; j <= 8; j++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new BuildingBlock(false, "Home"));
                playerHomeCells.add(cell);
            }
        }
        // Greenhouse runs from x : [22, 25] & y : [3, 6] (4 by 4)
        for (int i = 22; i <= 25; i++) {
            for (int j = 3; j <= 6; j++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);

                if (i != 22 && i != 28 && j != 3 && j != 10) {
                    cell.setObjectOnCell(new BuildingBlock(false, "Greenhouse"));
                } else
                    cell.setObjectOnCell(new BuildingBlock(false, "Greenhouse"));

                greenHouseCells.add(cell);
            }
        }
        //Mine Cells : x : [0, 9] y : [0, 11]
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 12; j++) {
                Cell cell = getCellByCoordinate(i, j, farmCells);
                cell.setObjectOnCell(new BuildingBlock(true, "Mine"));
                mineCells.add(cell);
            }
        }
        farmBuildings.add(new PlayerHome(playerHomeCells));
        farmBuildings.add(new Greenhouse(greenHouseCells));
        farmBuildings.add(new Mine(mineCells));
    }

    public static Cell getCellByCoordinate(int x, int y, ArrayList<Cell> cells) {
        for (Cell cell : cells) {
            if (cell.getCoordinate().getX() == x && cell.getCoordinate().getY() == y) {
                return cell;
            }
        }
        return null;
    }

    public void showFarm(int x, int y, int size, GameData gameData) {
        Player owner = gameData.getCurrentPlayer();
        Player partner = gameData.getPartner(owner);
        float ownerX = -1;
        float ownerY = -1;
        float partnerX = -1;
        float partnerY = -1;
        if (owner.getCurrentFarm(gameData) == this && !owner.isInVillage()) {
            ownerX = owner.getCoordinate().getX();
            ownerY = owner.getCoordinate().getY();
        }
        if (partner != null && partner.getCurrentFarm(gameData) == this && !partner.isInVillage()) {
            partnerX = partner.getCoordinate().getX();
            partnerY = partner.getCoordinate().getY();
        }

        for (Cell cell : cells) {
            Coordinate coordinate = cell.getCoordinate();

            float xOfCell = coordinate.getX();
            float yOfCell = coordinate.getY();

            if (Math.abs(x - xOfCell) <= size / 2 && Math.abs(y - yOfCell) <= size / 2) {
                if (xOfCell == ownerX && yOfCell == ownerY)
                    System.out.print("\u001B[34m " + "O" + "\033[0m");
                else if (xOfCell == partnerX && yOfCell == partnerY)
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
                else if (cell.getObjectOnCell().color.equals("purple"))
                    System.out.print("\u001B[35m " + cell.getObjectOnCell().toString() + "\033[0m");
                if (xOfCell - x == size / 2) {
                    System.out.print("\n");
                }
            }
        }
    }

    public ArtisanBlock getArtisanBlock(String artisanName) {
        for (Cell cell : cells) {
            if (cell.getObjectOnCell() instanceof ArtisanBlock && ((ArtisanBlock) cell.getObjectOnCell()).getArtisanType().name.equals(artisanName))
                return (ArtisanBlock) cell.getObjectOnCell();
        }
        return null;
    }

    public void removeAnimalFromBuilding(Animal animal) {
        for (Building building : buildings) {
            if (building instanceof Barn) {
                if (((Barn) building).animals.contains(animal)) {
                    ((Barn) building).animals.remove(animal);
                    return;
                }
            }
            if (building instanceof Coop) {
                if (((Coop) building).animals.contains(animal)) {
                    ((Coop) building).animals.remove(animal);
                    return;
                }
            }
        }
    }

    /// Debug method.
    public void showEntireFarm(GameData gameData) {
        Player owner = gameData.getCurrentPlayer();
        Player partner = gameData.getPartner(owner);
        float ownerX = -1;
        float ownerY = -1;
        float partnerX = -1;
        float partnerY = -1;
        if (owner.getCurrentFarm(gameData) == this && !owner.isInVillage()) {
            ownerX = owner.getCoordinate().getX();
            ownerY = owner.getCoordinate().getY();
        }
        if (partner != null && partner.getCurrentFarm(gameData) == this && !partner.isInVillage()) {
            partnerX = partner.getCoordinate().getX();
            partnerY = partner.getCoordinate().getY();
        }
        for (int i = 0; i < 152; i++) {
            System.out.print("_");
        }
        System.out.println();

        int cellIndex = 0;

        for (Cell cell : cells) {
            if (cellIndex % 75 == 0)
                System.out.print("|");

            if (cell.getCoordinate().getX() == ownerX && cell.getCoordinate().getY() == ownerY)
                System.out.print("\u001B[34m " + "O" + "\033[0m");
            else if (cell.getCoordinate().getX() == partnerX && cell.getCoordinate().getY() == partnerY)
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
            else if (cell.getObjectOnCell().color.equals("purple"))
                System.out.print("\u001B[35m " + cell.getObjectOnCell().toString() + "\033[0m");

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

    public void foragingRefresh(GameData gameData) {
        // makes sure the map doesn't get crowded.
        int count = 0;
        for (Cell cell : cells) {
            if (!(cell.getObjectOnCell() instanceof EmptyCell)) {
                count++;
            }
        }
        if (count >= 3200) {
            return;
        }

        for (Cell cell : cells) {
            int randomNumber = (int) (Math.random() * 150);
            if (cell.getObjectOnCell().type.equals("empty") && cell.isTilled() && randomNumber == 3) {
                CropSeedsType cropSeedsType = CropSeedsType.values()[(int) (Math.random() * (CropSeedsType.values().length - 1))];
                Crop crop = new Crop(cropSeedsType, gameData.getDate());
                cell.setObjectOnCell(crop);
            } else if (cell.getObjectOnCell().type.equals("empty") && randomNumber == 3) {
                cell.setObjectOnCell(randomForagingCrop(gameData));
            }
            int randomNumber2 = (int) (Math.random() * 15);

            if (cell.getObjectOnCell().type.compareToIgnoreCase("Mine") == 0 && randomNumber2 == 2 && isMineCell(cell)) {
                cell.setObjectOnCell(randomForagingMineral());
            }
        }
    }

    public Cell findCellByCoordinate(float x, float y) {
        for (Cell cell : cells) {
            if (isXInRange(cell, x) && isYInRange(cell, y)) {
                return cell;
            }
        }
        return null;
    }
    // x/32 > floor

    public boolean isXInRange(Cell c, float x) {
        //return (x >= (c.getCoordinate().getX() - 0.5f)) && (x <= (c.getCoordinate().getX() + 0.5f));
        return Math.floor(x) == c.getCoordinate().getX();
    }

    public boolean isYInRange(Cell c, float y) {
        //return (y >= (c.getCoordinate().getY() - 0.5f)) && (y <= (c.getCoordinate().getY() + 0.5f));
        return Math.floor(y + 1) == c.getCoordinate().getY();
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

    public boolean doesAnimalExist(String animalName) {
        for (Building b : getBuildings()) {
            if (b instanceof Coop) {
                for (Animal animal : ((Coop) b).animals) {
                    if (animal.getName().equals(animalName)) {
                        return true;
                    }
                }
            } else if (b instanceof Barn) {
                for (Animal animal : ((Barn) b).animals) {
                    if (animal.getName().equals(animalName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Building getAnimalBuilding(Animal animal) {
        for (Building b : getBuildings()) {
            if (b instanceof Coop) {
                for (Animal a : ((Coop) b).animals) {
                    if (a == animal) {
                        return b;
                    }
                }
            } else if (b instanceof Barn) {
                for (Animal a : ((Barn) b).animals) {
                    if (a == animal) {
                        return b;
                    }
                }
            }
        }
        return null;
    }

    public Cell getAnimalBlock(Animal animal) {
        for (Cell cell : getCells()) {
            if (cell.getObjectOnCell() instanceof AnimalBlock) {
                if (((AnimalBlock) cell.getObjectOnCell()).animal == animal) {
                    return cell;
                }
            }
        }
        return null;
    }

    public boolean isCellCoveredByScarecrow(Cell queryCell) {
        ArrayList<Cell> scarecrowCells = new ArrayList<>();
        for (Cell farmCell : cells) {
            if (farmCell.getObjectOnCell() instanceof ArtisanBlock) {
                ArtisanBlock artisanBlock = (ArtisanBlock) farmCell.getObjectOnCell();
                if (artisanBlock.getArtisanType() == ArtisanBlockType.DELUXE_SCARE_CROW
                    || artisanBlock.getArtisanType() == ArtisanBlockType.SCARE_CROW) {
                    scarecrowCells.add(farmCell);
                }
            }
        }

        for (Cell scarecrowCell : scarecrowCells) {
            double distance = Coordinate.calculateEuclideanDistance(scarecrowCell, queryCell);
            ArtisanBlock a = (ArtisanBlock) (scarecrowCell.getObjectOnCell());

            double maxDistance = (a.getArtisanType() == ArtisanBlockType.SCARE_CROW) ? 8 : 12;

            if (distance <= maxDistance) {
                return true;
            }
        }
        return false;
    }


    public void strikeLightning(int targetX, int targetY, LocalDateTime source, GameData gameData) {
        //Greenhouse Tile
        if (targetX >= 22 && targetX <= 28 && targetY >= 3 && targetY <= 10) {
            return;
        }

        Cell targetCell = findCellByCoordinate(targetX, targetY);
        if (targetCell != null) {
            if (targetCell.getObjectOnCell() instanceof Tree) {
                targetCell.setObjectOnCell(new Tree(TreeType.BURNT_TREE, source, gameData));
            }
            if (targetCell.getObjectOnCell() instanceof Crop) {
                targetCell.setObjectOnCell(new EmptyCell());
            }
            if (targetCell.getObjectOnCell() instanceof ForagingCrop) {
                targetCell.setObjectOnCell(new EmptyCell());
            }
        }
        System.out.println("Lightning has struck coordinates: " + targetX + ", " + targetY);
    }

    public int getFarmNumber() {
        return farmNumber;
    }

    public void setFarmNumber(int farmNumber) {
        this.farmNumber = farmNumber;
    }

    public int[][] giantCropsTogether(Cell targetCell) {
        int dir[][][] = {
            {{0, 1}, {1, 0}, {1, 1}},
            {{0, 1}, {-1, 0}, {-1, 1}},
            {{0, -1}, {-1, 0}, {-1, -1}},
            {{0, -1}, {1, 0}, {1, -1}}
        };
        for (int i = 0; i < 3; i++) {
            int arr[][] = dir[i];
            boolean check = true;
            for (int j = 0; j < 3; j++) {
                float x = arr[j][0] + targetCell.getCoordinate().getX();
                float y = arr[j][1] + targetCell.getCoordinate().getY();
                Cell c = findCellByCoordinate(x, y);
                if (c != null) {
                    if (c.getObjectOnCell() instanceof Crop crop) {
                        Crop cellCrop = (Crop) targetCell.getObjectOnCell();
                        if (crop.cropSeedsType != cellCrop.cropSeedsType) {
                            check = false;
                            break;
                        }
                    } else {
                        check = false;
                        break;
                    }
                } else {
                    check = false;
                    break;
                }
            }
            if (check) {
                return arr;
            }
        }
        return null;
    }
}
