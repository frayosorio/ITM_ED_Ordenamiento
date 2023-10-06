package ordenamiento;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;

public class Util {

    public static Object redimensionar(Object arregloOriginal, int nuevoTamano) {
        Object nuevoArreglo = null;
        if (arregloOriginal != null) {
            Class tipo = arregloOriginal.getClass();
            Class t = tipo.getComponentType();
            nuevoArreglo = Array.newInstance(t, nuevoTamano);
            System.arraycopy(arregloOriginal, 0, nuevoArreglo, 0, Math.min(Array.getLength(arregloOriginal), nuevoTamano));
        }
        return nuevoArreglo;
    }

    public static long inicio;

    public static void iniciarCronometro() {
        inicio = System.currentTimeMillis();
    }

    public static long obtenerTiempoCronometro() {
        return System.currentTimeMillis() - inicio;
    }

    public static String obtenerTextoTiempoCronometro() {
        long tiempo = obtenerTiempoCronometro();
        long ms = tiempo % 1000;
        tiempo = (tiempo - ms) / 1000;
        long s = tiempo % 60;
        tiempo = (tiempo - s) / 60;
        long m = tiempo % 60;
        tiempo = (tiempo - m) / 60;
        long h = tiempo % 60;
        DecimalFormat df = new DecimalFormat("00");

        return df.format(h) + ":" + df.format(m) + ":" + df.format(s) + "." + df.format(ms);
    }

}
