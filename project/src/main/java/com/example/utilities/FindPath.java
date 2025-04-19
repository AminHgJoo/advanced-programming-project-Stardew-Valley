package com.example.utilities;

import com.example.models.mapModels.Cell;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class FindPath {
    public static ArrayList<Cell> cells;
    private static final int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public static void pathBFS(Cell src, Cell dest, ArrayList<Cell> arr) {
        cells = arr;
        boolean[][] visited = new boolean[9][10];
        Queue<Cell> queue = new PriorityQueue<>(new CellComparator());
        queue.add(src);
        visited[src.getCoordinate().getX()][src.getCoordinate().getY()] = true;
        while (!queue.isEmpty()) {
            Cell curr = queue.poll();
            for (int[] dir : DIRECTIONS) {
                int newX = curr.getCoordinate().getX() + dir[0];
                int newY = curr.getCoordinate().getY() + dir[1];
                if (newX >= 0 && newX < 9 && newY >= 0 && newY < 10 && !visited[newX][newY]) {
                    Cell neighbour = findCell(newX, newY);
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
                    for (int[] dir2 : DIRECTIONS) {
                        int i = newX + dir2[0];
                        int j = newY + dir2[1];
                        Cell d = findCell(i, j);
                        if (d != null && i == dest.getCoordinate().getX() && j == dest.getCoordinate().getY()) {
                            d.prev = neighbour;
                            if (!(neighbour.diffXPrev() == d.diffXPrev() && neighbour.diffYPrev() == d.diffYPrev())) {
                                neighbour.turns += 1;
                            }
                        }
                    }
                    neighbour.energy = (neighbour.distance + 10 * neighbour.turns);
                    visited[neighbour.getCoordinate().getX()][neighbour.getCoordinate().getY()] = true;
                    if ((newX == 0 && newY == 3) || (newX == 1 && newY == 3)) {
                        int x = 5;
                    }
                    queue.add(neighbour);
                }
            }
        }
    }

    public static class CellComparator implements Comparator<Cell> {
        @Override
        public int compare(Cell c1, Cell c2) {
            return Integer.compare(c1.energy, c2.energy); // Lower cost = higher priority
        }
    }

    public static Cell findCell(int x, int y) {
        for (Cell cell : cells) {
            if (cell.getCoordinate().getX() == x && cell.getCoordinate().getY() == y) {
                return cell;
            }
        }
        return null;
    }
}
