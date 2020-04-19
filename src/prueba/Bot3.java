package prueba;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

import prueba.utils.searchs.AmplitudeSearch;
import prueba.utils.searchs.SearchEngine;
import prueba.utils.Cell;
import robocode.Robot;

public class Bot3 extends Robot {
	private Problem problem;
	private SearchEngine searchEngine;

	// The main method in every robot
	public void run() {

		System.out.println("Iniciando ejecución del robot");

		// Seleccionar el color del robot
		setColors(Color.BLACK, Color.BLUE, Color.CYAN);

		// Orientamos inicialmente el robot hacia arriba
		turnRight(normalRelativeAngleDegrees(0 - getHeading()));

		// Seleccionar algoritmo de busqueda
		this.problem = new Problem();
		searchEngine =
			new AmplitudeSearch(this, problem); // Busqueda por Amplitud
	}

	public void onPaint(Graphics2D g) {
		Cell _final = problem.getGOAL();
		Cell _inicial = problem.getSTART();

		// Inicial
		g.setColor(new Color(0x00, 0xff, 0x00, 0x80));
		g.fillRect(((_inicial.y + 0) * problem.CELL_SIZE) -20 + problem.CELL_OFFSET, ((_inicial.x + 0) * problem.CELL_SIZE) -20 + problem.CELL_OFFSET, 40, 40);

		// DESTINO
		g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
		g.fillRect(((_final.y + 0) * problem.CELL_SIZE) -20 + problem.CELL_OFFSET, ((_final.x + 0) * problem.CELL_SIZE) -20 + problem.CELL_OFFSET, 40, 40);

		//Cuadrículas
		int fila 	 	= problem.WIDTH;
		int columna  	= problem.WIDTH;
		int tamCelda 	= problem.CELL_SIZE;
		int filaPixels 	= problem.HEIGHT_PIXELS;

		g.setPaint(Color.white);

		for (int i=0; i<columna;i++)
			g.drawLine(i*tamCelda, 0, i*tamCelda, filaPixels);

		for (int i=0; i<fila;i++)
			g.drawLine (0, i*tamCelda, filaPixels, i*tamCelda);

//		// Información acciones.
		g.setPaint(Color.yellow);
		g.setFont(new Font("Serif", Font.BOLD, 16));


		String cfgString = "Algoritmo: "+ "BUSQUEDA AMPLITUD" + " Semilla: "+ problem.SEED + " Obstáculos: "+ problem.N_OBSTACLES;
		g.drawString(cfgString, 5, 25);
	}
}
