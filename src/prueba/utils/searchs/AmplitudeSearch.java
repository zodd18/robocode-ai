package prueba.utils.searchs;

import prueba.Problem;
import prueba.utils.Cell;
import robocode.Robot;

import java.util.*;

public class AmplitudeSearch extends SearchEngine {

    public AmplitudeSearch(Robot robot, Problem problem) {
        super(robot, problem);

        Stack<Cell> bfsPath = search();

        if(bfsPath != null) {
            super.simulate(bfsPath);
            solved = true;
        } else {
            solved = false;
            while(true) robot.turnRight(90);
        }
    }

    // TODO - Implement search algorithm
    @Override
    public Stack<Cell> search() {
        Stack<Cell> path = new Stack<>();
        Queue<Cell> openNodes, closedNodes;

        // Crear un conjunto de nodos ABIERTOS con s.
        openNodes = new LinkedList<>();
        openNodes.add(robotPosition);

        // Crear un conjunto de nodos CERRADOS vacío.
        closedNodes = new LinkedList<>();

        // Mientras ABIERTOS no esté vacío
        while(!openNodes.isEmpty()) {
            // n ← obtener(ABIERTOS).
            Cell n = openNodes.remove();

            // Pasar n de ABIERTOS a CERRADOS.
            closedNodes.add(n);

            // Si n es objetivo, entonces devolver el camino de s hasta n en A.
            if(n.isFinalPosition(goal)) {
                // Devolver camino resultado
                while (n != null) {
                    path.insertElementAt(n, 0);
                    n = n.getParent();
                }

                System.out.println("EXITO!!!");
                System.out.println("Camino: " + path.toString());
                System.out.println("Movimientos: " + (path.size() - 1));
                return path;
            }

            // Expandir n. M <-sucesores(n, G) –antecesores(n, A).
            LinkedList<Cell> M = n.freeSuccessors(map); // successors

            for(Cell n2 : M) {
                // Si n2 es nuevo (n2 no está ABIERTO ni CERRADO)
                if(!openNodes.contains(n2) && !closedNodes.contains(n2)) {
                    // 1. Poner un puntero de n2 -> n
                    n2.setParent(n);

                    // 2. Añadir n2 a ABIERTOS.       (FINAL COLA)
                    openNodes.add(n2);
                }
            }
        }

        // Devolver "FRACASO"
        System.out.println("FRACASO");
        return null;
    }
}
