package prueba.utils.searchs;

import prueba.Problem;
import prueba.utils.Cell;
import robocode.Robot;

import java.util.Collection;
import java.util.Stack;

public abstract class SearchEngine {
	protected long seed;

	protected boolean[][] map;
	protected int height, width;
	protected int cellSize;
	protected int nObstacles;

	protected Cell goal, start;

	// Informacion con respecto al robot
	protected Robot robot;
	protected Cell robotPosition; // Posicion del robot en todo momento
	protected double orientation;

	// Problema resuelto
	protected boolean solved;

	public SearchEngine(Robot robot, Problem problem) {
		this.robot = robot;
		this.seed = problem.SEED;

		this.solved = false;

		// Map
		this.map = problem.getMap();
		this.height = problem.HEIGHT;
		this.width = problem.WIDTH;
		this.cellSize = problem.CELL_SIZE;
		this.nObstacles = problem.N_OBSTACLES;

		// Robot's goal/position
		this.start = problem.getSTART();
		this.goal = problem.getGOAL();

		this.robotPosition = new Cell(start.getY(), start.getX());
		this.orientation = 0.0;

		System.out.println("Problema: ");
		printInformation();
	}

	// TODO - Implement search algorithm
	public Collection<Cell> search() { return null; }

	// Movement --------------------------------------------

	protected void simulate(Stack<Cell> path) {
		for(Cell dest : path) {
			if(!dest.equals(robotPosition)) {
				Direction direction = getDirectionType(robotPosition, dest);

				switch (direction) {
					case UP: goUp(); break;
					case UP_LEFT: goUpLeft(); break;
					case UP_RIGHT: goUpRight(); break;

					case DOWN: goDown(); break;
					case DOWN_LEFT: goDownLeft(); break;
					case DOWN_RIGHT: goDownRight(); break;

					case LEFT: goLeft(); break;
					case RIGHT: goRight(); break;
				}
				robotPosition = dest;
			}
		}

		System.out.println("GOAL!!!");
		while(true) robot.turnRight(90);
	}

	private Direction getDirectionType(Cell src, Cell dest) {
		Direction vertical = getVerticalType(src, dest);
		Direction horizontal = getHorizontalType(src, dest);
		Direction direction = null;

//		System.out.println("VERTICAL: " + vertical + " | HORIZONTAL: " + horizontal);

		if(vertical == null) {
			direction = horizontal;
		} else if(horizontal == null) {
			direction = vertical;
		} else if(vertical == Direction.UP) {
			if(horizontal == Direction.LEFT) direction = Direction.UP_LEFT;
			else direction = Direction.UP_RIGHT;
		} else if(vertical == Direction.DOWN) {
			if(horizontal == Direction.LEFT) direction = Direction.DOWN_LEFT;
			else direction = Direction.DOWN_RIGHT;
		}

		return direction;
	}

	private Direction getVerticalType(Cell src, Cell dest) {
		Direction vertical = null;

		if(dest.y < src.y) vertical = Direction.UP;
		else if(dest.y > src.y) vertical = Direction.DOWN;

		return vertical;
	}

	private Direction getHorizontalType(Cell src, Cell dest) {
		Direction horizontal = null;

		if(dest.x < src.x) horizontal = Direction.LEFT;
		else if(dest.x > src.x) horizontal = Direction.RIGHT;

		return horizontal;
	}

	private void setRobotDirection(Direction d) {
		System.out.println("MOVIMIENTO: " + d);

		double robotHeading = robot.getHeading();
		double neededDirection = directionToDouble(d);

		while(robotHeading != neededDirection) {
			robot.turnRight(neededDirection - robotHeading);
			robotHeading = robot.getHeading();
		}
	}

	protected double directionToDouble(Direction d) {
		double facingDirection = 0.0;

		switch(d) {
			case UP: facingDirection = 0.0; break;
			case UP_LEFT: facingDirection = 315.0; break;
			case UP_RIGHT: facingDirection = 45.0; break;
			case DOWN: facingDirection = 180.0; break;
			case DOWN_LEFT: facingDirection = 225; break;
			case DOWN_RIGHT: facingDirection = 135; break;
			case LEFT: facingDirection = 270.0; break;
			case RIGHT: facingDirection = 90.0; break;
		}

		return facingDirection;
	}

	private void walkStraight() {
		robot.ahead(Problem.CELL_SIZE);
	}

	private void walkDiagonal() {
		robot.ahead(Problem.CELL_SIZE * Math.sqrt(2));
	}

	// map=up, robot=left
	private void goUp() {
		setRobotDirection(Direction.LEFT);
		walkStraight();
	}

	// map=up-left, robot=down-left
	private void goUpLeft() {
		setRobotDirection(Direction.DOWN_LEFT);
		walkDiagonal();
	}

	// map=up-right, robot=up-left
	private void goUpRight() {
		setRobotDirection(Direction.UP_LEFT);
		walkDiagonal();
	}

	// map=down, robot=right
	private void goDown() {
		setRobotDirection(Direction.RIGHT);
		walkStraight();
	}

	// map=down-left, robot=down-right
	private void goDownLeft() {
		setRobotDirection(Direction.DOWN_RIGHT);
		walkDiagonal();
	}

	// map=down-right, robot=up-right
	private void goDownRight() {
		setRobotDirection(Direction.UP_RIGHT);
		walkDiagonal();
	}

	// map=left, robot=down
	private void goLeft() {
		setRobotDirection(Direction.DOWN);
		walkStraight();
	}

	// map=right, robot=up
	private void goRight() {
		setRobotDirection(Direction.UP);
		walkStraight();
	}

	// LOG INFO
	protected void printInformation() {
		char OCCUPIED = 'x';
		char FREE = '-';
		char START = 'o';
		char GOAL = '!';

		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				boolean b = map[y][x];
				char symbol = b ? OCCUPIED : FREE;
				if(goal.y == y && goal.x == x) symbol = GOAL;
				else if(start.y == y && start.x == x) symbol = START;
				System.out.print(symbol + " ");
			}
			System.out.println();
		}
	}

	protected void printMapCode() {
		for(int i = 0; i < height; i++) {
			System.out.print("{ ");
			for(int j = 0; j < width; j++) {
				String bool = map[i][j] ? "true" : "false";
				if(j < width - 1) System.out.print(bool + ", ");
				else System.out.print(bool);
			}
			if(i < height - 1) System.out.print(" }, ");
			else System.out.print(" }");
		}
		System.out.println();
	}

	// Has solution?
	public boolean hasSolution() {
		return solved;
	}
}
