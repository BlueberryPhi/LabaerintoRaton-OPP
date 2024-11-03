/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Lenovo
 */
import java.io.*;
import java.util.*;

public class Raton {
    private char[][] laberinto;
    private int startX, startY;
    private List<String> movimientos;

    public Raton(char[][] laberinto) {
        this.laberinto = laberinto;
        this.movimientos = new ArrayList<>();
        encontrarInicio();
    }

    private void encontrarInicio() {
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {
                if (laberinto[i][j] == 'S') {
                    startX = i;
                    startY = j;
                    return;
                }
            }
        }
        throw new IllegalArgumentException("No se encontró el punto de inicio 'S' en el laberinto.");
    }

    public List<String> resolver() {
        boolean[][] mhm = new boolean[laberinto.length][laberinto[0].length];
        if (SearchPath(startX, startY, mhm)) {
            return movimientos;
        } else {
            return Arrays.asList("No se encontró una ruta válida");
        }
    }

    private boolean SearchPath(int x, int y, boolean[][] mhm) {
        if (x < 0 || y < 0 || x >= laberinto.length || y >= laberinto[0].length || laberinto[x][y] == '1' || mhm[x][y]) {
            return false;
        }

        mhm[x][y] = true;

        if (laberinto[x][y] == 'F') {
            return true;
        }

        if (SearchPath(x - 1, y, mhm)) {
            movimientos.add(0, "arriba");
            return true;
        }

        if (SearchPath(x + 1, y, mhm)) {
            movimientos.add(0, "abajo");
            return true;
        }

        if (SearchPath(x, y - 1, mhm)) {
            movimientos.add(0, "izquierda");
            return true;
        }

        if (SearchPath(x, y + 1, mhm)) {
            movimientos.add(0, "derecha");
            return true;
        }

        mhm[x][y] = false;
        return false;
    }

    private static char[][] leerLaberintoDesdeArchivo(String nombreArchivo) throws IOException {
        List<char[]> filas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                filas.add(linea.toCharArray());
            }
        }
        return filas.toArray(new char[filas.size()][]);
    }

    public void guardarCamino(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String movimiento : movimientos) {
                writer.write(movimiento);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[][] laberinto = null;
        String nombreArchivo;

        while (true) {
            System.out.print("Ingrese el nombre del archivo del laberinto (con extensión .txt): ");
            nombreArchivo = scanner.nextLine();

            try {
                laberinto = leerLaberintoDesdeArchivo(nombreArchivo);
                break;
            } catch (IOException e) {
                System.out.println("Error al leer el archivo");
                System.out.println("Por favor, ingrese un nombre de archivo válido.");
            }
        }

        Raton raton = new Raton(laberinto);
        List<String> ruta = raton.resolver();
        if (ruta.size() == 1 && ruta.get(0).equals("No se encontró una ruta válida")) {
            System.out.println(ruta.get(0));
        } else {
            for (String movimiento : ruta) {
                System.out.println(movimiento);
            }
            try {
                raton.guardarCamino("camino_" + nombreArchivo);
                System.out.println("Ruta guardada en 'camino_" + nombreArchivo + "'");
            } catch (IOException e) {
                System.out.println("Error al guardar el archivo");
            }
        }
    }
}

