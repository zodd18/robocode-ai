package prueba.utils;

import prueba.RouteFinder;

import java.awt.*;
import java.util.LinkedList;

public class Cell {

	public int y, x;
	private Cell parent;

	// y, x
	public Cell(int y, int x) {
		this.y = y;
		this.x = x;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public LinkedList<Cell> freeSuccessors(boolean[][] map) {
		LinkedList<Cell> sucesores = new LinkedList<>();
		int SIZE = map.length;

		// ARRIBA
		if (y > 0 && !map[y - 1][x]) {
			sucesores.add(new Cell(y - 1, x));
		}
		// ABAJO
		if (y < SIZE - 1 && !map[y + 1][x]) {
			sucesores.add(new Cell(y + 1, x));
		}
		// IZQUIERDA
		if (x > 0 && !map[y][x - 1]) {
			sucesores.add(new Cell(y, x - 1));
		}

		// DERECHA
		if (x < SIZE - 1 && !map[y][x + 1]) {
			sucesores.add(new Cell(y, x + 1));
		}

		// ARRIBA IZQ
		if (y > 0 && x > 0 && !map[y - 1][x - 1] && (!map[y][x - 1] && !map[y - 1][x])) {
			sucesores.add(new Cell(y - 1, x - 1));
		}

		// ARRIBA DER
		if (y > 0 && x < SIZE - 1 && !map[y - 1][x + 1] && (!map[y][x + 1] && !map[y - 1][x])) {
			sucesores.add(new Cell(y - 1, x + 1));
		}

		// ABAJO IZQ
		if (y < SIZE - 1 && x > 0 && !map[y + 1][x - 1] && (!map[y][x - 1] && !map[y + 1][x])) {
			sucesores.add(new Cell(y + 1, x - 1));
		}

		// ABAJO DER
		if (y < SIZE - 1 && x < SIZE - 1 && !map[y + 1][x + 1]
				&& (!map[y][x + 1] && !map[y + 1][x])) {
			sucesores.add(new Cell(y + 1, x + 1));
		}

		return sucesores;
	}

	public boolean isFinalPosition(Cell goal) {
		return this.equals(goal);
	}

	public double h(Cell other) {
		double distance = new Point(y, x).distance(new Point(other.y, other.x));
		return distance;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	@Override
	public String toString() {
		return String.format("(%d, %d)", y, x);
	}

	public Cell getParent() {
		return parent;
	}

	public void setParent(Cell parent) {
		this.parent = parent;
	}
}
