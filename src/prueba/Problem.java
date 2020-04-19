package prueba;

import prueba.utils.Cell;

import java.util.LinkedList;
import java.util.Random;

public class Problem {

    public static final long SEED = RouteFinder.SEED;

    public static final int N_OBSTACLES = RouteFinder.N_OBSTACLES;

    public static final int CELL_SIZE = RouteFinder.CELL_SIZE;
    public static final int CELL_OFFSET = CELL_SIZE / 2;

    public static final int HEIGHT_PIXELS = RouteFinder.HEIGHT_PIXELS;
    public static final int WIDTH_PIXELS = HEIGHT_PIXELS;

    public static final int HEIGHT = HEIGHT_PIXELS / CELL_SIZE;
    public static final int WIDTH = WIDTH_PIXELS / CELL_SIZE;

    /* Random generated variables */
    private boolean[][] map; // true = casilla ocupada, false = casilla libre
    private LinkedList<Cell> obstacles;
    private Cell GOAL, START;
    private Random random;

    public Problem() {
        this.random = new Random(SEED);

        this.map = new boolean[HEIGHT][WIDTH];

        // Guarda en mapa los obstaculos y devuelve la lista compuesta por estos
        this.obstacles = generateObstacles();

        this.GOAL = getFreeCell(); // Le asignamos a la meta una posicion
        this.map[GOAL.y][GOAL.x] = true; // Añadimos temporalmente a la meta como una casilla ocupada en el mapa para que el robot no pueda aparecer sobre ella

        this.START = getFreeCell(); // Asignamos una posicion inicial
        this.map[GOAL.y][GOAL.x] = false; // Desocupamos la casilla meta para que el robot pueda situarse sobre ella
    }

    private Cell getFreeCell() {
        LinkedList<Cell> freeCells = getFreeCells();
        int randomFreeCell = random.nextInt(freeCells.size());
        return freeCells.get(randomFreeCell);
    }

    public LinkedList<Cell> generateObstacles() {
        LinkedList<Cell> obstacles = new LinkedList<>();

        for(int i = 0; i < N_OBSTACLES; i++) {
            LinkedList<Cell> freeCells = getFreeCells();
            int index = random.nextInt(freeCells.size());

            Cell obstacle = freeCells.get(index);
            map[obstacle.y][obstacle.x] = true;
            obstacles.add(obstacle);
        }

        return obstacles; // Devuelve una lista de obstaculos generados de manera aleatoria
    }

    public LinkedList<Cell> getFreeCells() {
        LinkedList<Cell> freeCells = new LinkedList<>();

        for(int y = 0; y < HEIGHT; y++) {
            for(int x = 0; x < WIDTH; x++) {
                // false = casilla libre
                if(!map[y][x]) {
                    freeCells.add(new Cell(y, x));
                }
            }
        }

        return freeCells;
    }

    // ------------------------- Getters -------------------------

    public boolean[][] getMap() {
        return map;
    }

    public LinkedList<Cell> getObstacles() {
        return obstacles;
    }

    public Cell getGOAL() {
        return GOAL;
    }

    public Cell getSTART() {
        return START;
    }
}
