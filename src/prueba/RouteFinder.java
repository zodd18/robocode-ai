package prueba;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;
import java.util.Random;

import prueba.utils.Cell;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSetup;
import robocode.control.RobotSpecification;

public class RouteFinder {

	public static final long SEED = 0xF;

	public static final int N_OBSTACLES = 20;

	public static final int CELL_SIZE = 80;
	public static final int CELL_OFFSET = CELL_SIZE / 2;

	public static final int HEIGHT_PIXELS = 800;
	public static final int WIDTH_PIXELS = HEIGHT_PIXELS;

	public static final int HEIGHT = HEIGHT_PIXELS / CELL_SIZE;
	public static final int WIDTH = WIDTH_PIXELS / CELL_SIZE;

	// ----------------------------------------------------------

	private static String ROBOCODE_PATH; // C://Robocode

	private static int SITTING_DUCK = 0;
	private static int BOT3 = 1;

	public static boolean[][] map; // true = casilla ocupada, false = casilla libre
	public static Cell GOAL, START;

	public static Random random;

	// ----------------------------------------------------------

	public static void main(String[] args) {

		if(System.getProperty("os.name").equalsIgnoreCase("Linux")) {
			String username = System.getProperty("user.name");
			ROBOCODE_PATH = "/home/"+ username +"/robocode/";
		} else {
			ROBOCODE_PATH = "C:/Robocode";
		}

		// Semilla para el generador de números aleatorios
		random = new Random(SEED);

		// Creamos un mapa con los datos especificados
		map = new boolean[HEIGHT][WIDTH];

		Problem problem = new Problem();

		// Guarda en mapa los obstaculos y devuelve la lista compuesta por estos
		map = problem.getMap();
		LinkedList<Cell> obstacles = problem.getObstacles();
		GOAL = problem.getGOAL(); // Le asignamos a la meta una posicion
		START = problem.getSTART(); // Asignamos una posicion inicial

		// ------------------------------------------------------------------------------------------------------------

		// Crear el RobocodeEngine desde el directorio de instalación
		RobocodeEngine engine = new RobocodeEngine(new File(ROBOCODE_PATH));
		// Mostrar el simulador de Robocode
		engine.setVisible(true);

		BattlefieldSpecification battlefield = new BattlefieldSpecification(HEIGHT_PIXELS, WIDTH_PIXELS);
		// Establecer parámetros de la batalla
		int numberOfRounds = 5;
		long inactivityTime = 10000000;
		double gunCoolingRate = 1.0;
		int sentryBorderSize = 50;
		boolean hideEnemyNames = false;

		RobotSpecification[] modelRobots = engine.getLocalRepository("sample.SittingDuck,prueba.Bot3*");

		// Incluiremos un robot sittingDuck por obstáculo, más nuestro propio robot.
		RobotSpecification[] existingRobots = new RobotSpecification[N_OBSTACLES + 1];
		RobotSetup[] robotSetups = new RobotSetup[N_OBSTACLES + 1];

		int index = 0;

		// SETUP BOT3 -------------------------------------------------------------------------------------------------
		existingRobots[index] = modelRobots[BOT3];
		Cell fixedPosition = getCenteredRobotPosition(problem.getSTART());
		robotSetups[index] = new RobotSetup((double) fixedPosition.y, (double) fixedPosition.x, 0.0);
		index++;

		// Generacion de 1 SittingDuck por cada obstaculo -------------------------------------------------------------
		for(Cell obs : obstacles) {
			existingRobots[index] = modelRobots[SITTING_DUCK]; // sittingDuck
			// (fila, columna, orientacion)
			fixedPosition = getCenteredRobotPosition(obs);
			robotSetups[index] = new RobotSetup((double) fixedPosition.y, (double) fixedPosition.x, 0.0);
			index++;
		}

		// INFORMACION EN PANTALLA ------------------------------------------------------------------------------------

		System.out.println("Generados " + (index - 1) + " sitting ducks.");

		Point p1 = new Point(GOAL.y, GOAL.x);
		Point p2 = new Point(START.y, START.x);

		System.out.println("\n------------------GENERACION----------------------");
		System.out.println("Semilla generada: " + SEED);
		System.out.println("Posicion inicial: " + START.toString());
		System.out.println("META: " + GOAL.toString());
		System.out.println("Distancia: " + p1.distance(p2));

		printMap(true); // true = arreglado | false = girado (default)

		System.out.println(START.freeSuccessors(map));

		// ------------------------------------------------------------------------------------------------------------

		/* Ejecutar la simulación */
		BattleSpecification battleSpec = new BattleSpecification(battlefield, numberOfRounds, inactivityTime, gunCoolingRate, sentryBorderSize, hideEnemyNames, existingRobots, robotSetups);
		engine.runBattle(battleSpec, true);
		engine.close();
		System.exit(0);
	}

	// Utils ----------------------------------------------------------------------------------------------------------

	private static Cell getCenteredRobotPosition(Cell position) {
		return getCenteredRobotPosition(position.y, position.x);
	}

	private static Cell getCenteredRobotPosition(int y, int x) {
		return new Cell(y * CELL_SIZE + CELL_OFFSET, x * CELL_SIZE + CELL_OFFSET);
	}

	public static void printMap(boolean fixed) {
		char OCCUPIED = 'x';
		char FREE = '.';
		char _START = 'o';
		char _GOAL = '!';

		if(fixed) {
			for(int y = HEIGHT - 1; y >= 0; y--) {
				for(int x = 0; x < WIDTH; x++) {
					boolean b = map[x][y];
					char symbol = b ? OCCUPIED : FREE;
					if(GOAL.y == x && GOAL.x == y) symbol = _GOAL;
					else if(START.y == x && START.x == y) symbol = _START;
					System.out.print(symbol + " ");
				}
				System.out.println();
			}
		} else {
			for(int y = 0; y < HEIGHT; y++) {
				for(int x = 0; x < WIDTH; x++) {
					boolean b = map[y][x];
					char symbol = b ? OCCUPIED : FREE;
					if(GOAL.y == y && GOAL.x == x) symbol = _GOAL;
					else if(START.y == y && START.x == x) symbol = _START;
					System.out.print(symbol + " ");
				}
				System.out.println();
			}
		}

	}
}
