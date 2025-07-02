package com.common.utilities;

import com.common.models.mapModels.Cell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class FindPath {
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    public static ArrayList<Tile> cells = new ArrayList<>();

    private static ArrayList<Tile> cellToTile(ArrayList<Cell> c) {
        ArrayList<Tile> arr = new ArrayList<>();
        for (Cell cell : c) {
            arr.add(new Tile(cell));
        }
        return arr;
    }

    public static Tile pathBFS(Tile src, Tile dest, ArrayList<Cell> arr) {
        for (Cell c : arr) {
            cells.add(new Tile(c));
        }
        boolean[][] visited = new boolean[75][50];
        Queue<Tile> queue = new PriorityQueue<>(new CellComparator());
        queue.add(src);
        visited[src.getCoordinate().getX()][src.getCoordinate().getY()] = true;
        while (!queue.isEmpty()) {
            Tile curr = queue.poll();
            if (curr.equals(dest)) {
                dest = curr;
                break;
            }
            for (int[] dir : DIRECTIONS) {
                int newX = curr.getCoordinate().getX() + dir[0];
                int newY = curr.getCoordinate().getY() + dir[1];
                if (newX >= 0 && newX < 75 && newY >= 0 && newY < 50 && !visited[newX][newY]) {
                    Tile neighbour = findCell(newX, newY);
                    if (!neighbour.getObjectOnCell().isWalkable) {
                        continue;
                    }
                    neighbour.prev = curr;
                    neighbour.distance = neighbour.prev.distance + 1;
                    if (curr.prev == null) {
                        neighbour.turns = curr.turns;
                    } else if (neighbour.diffXPrev() == curr.diffXPrev() && neighbour.diffYPrev() == curr.diffYPrev()) {
                        neighbour.turns = neighbour.prev.turns;
                    } else {
                        neighbour.turns = neighbour.prev.turns + 1;
                    }
                    neighbour.energy = (neighbour.distance + 10 * neighbour.turns);
                    visited[neighbour.getCoordinate().getX()][neighbour.getCoordinate().getY()] = true;
                    queue.add(neighbour);
                }
            }
        }
        return dest;
    }

    public static Tile findCell(int x, int y) {
        for (Tile cell : cells) {
            if (cell.getCoordinate().getX() == x && cell.getCoordinate().getY() == y) {
                return cell;
            }
        }
        return null;
    }

    public static class CellComparator implements Comparator<Tile> {
        @Override
        public int compare(Tile c1, Tile c2) {
            return Double.compare(c1.energy, c2.energy); // Lower cost = higher priority
        }
    }
}
