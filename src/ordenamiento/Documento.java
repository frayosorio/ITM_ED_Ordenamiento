package ordenamiento;

import java.io.BufferedReader;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class Documento {

    private String apellido1;
    private String apellido2;
    private String nombre;
    private String documento;

    public Documento(String apellido1, String apellido2, String nombre, String documento) {
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.nombre = nombre;
        this.documento = documento;
    }

    public String getApellido1() {
        return apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombreCompleto() {
        return apellido1 + " " + apellido2 + " " + nombre;
    }

//******************** Metodos estaticos ******************************
    private static Documento[] documentos;

    //Llena la lista de documentos desde un archivo plano
    public static void desdeArchivo(String nombreArchivo) {
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        documentos = null;
        try {
            String linea = br.readLine();
            linea = br.readLine();
            while (linea != null) {
                String[] datos = linea.split(";");
                if (datos.length >= 4) {
                    Documento d = new Documento(datos[0],
                            datos[1],
                            datos[2],
                            datos[3]);
                    if (documentos == null) {
                        documentos = new Documento[1];
                    } else {
                        documentos = (Documento[]) Util.redimensionar(documentos, documentos.length + 1);
                    }
                    documentos[documentos.length - 1] = d;
                }

                linea = br.readLine();
            }

        } catch (IOException ex) {

        }

    }

    //Muestra la lista en una tabla
    public static void mostrar(JTable tbl) {
        if (documentos != null) {
            String[] encabezados = new String[]{"Nombre Completo", "Documento"};
            String[][] datos = new String[documentos.length][encabezados.length];
            int fila = 0;
            for (Documento d : documentos) {
                datos[fila][0] = d.getNombreCompleto();
                datos[fila][1] = d.getDocumento();
                fila++;
            }
            DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);

            tbl.setModel(dtm);
        }
    }

    public static void intercambiar(int origen, int destino) {
        Documento dt = documentos[origen];
        documentos[origen] = documentos[destino];
        documentos[destino] = dt;
    }

    public static boolean esMayor(Documento dI, Documento dJ, CriterioOrdenamiento criterio) {
        if (criterio == CriterioOrdenamiento.NOMBRECOMPLETO_DOCUMENTO) {
            //ordenar primero por Nombre Completo y luego por Tipo de Documento
            return ((dI.getNombreCompleto().compareTo(dJ.getNombreCompleto()) > 0)
                    || (dI.getNombreCompleto().compareTo(dJ.getNombreCompleto()) == 0
                    && dI.getDocumento().compareTo(dJ.getDocumento()) > 0));
        } else {
            return ((dI.getDocumento().compareTo(dJ.getDocumento()) > 0)
                    || (dI.getDocumento().compareTo(dJ.getDocumento()) == 0)
                    && dI.getNombreCompleto().compareTo(dJ.getNombreCompleto()) > 0);
        }
    }

    public static String ordenarBurbuja(CriterioOrdenamiento criterio) {
        Util.iniciarCronometro();
        for (int i = 0; i < documentos.length - 1; i++) {
            for (int j = i + 1; j < documentos.length; j++) {
                if (esMayor(documentos[i], documentos[j], criterio)) {
                    intercambiar(i, j);
                }
            }
        }
        return Util.obtenerTextoTiempoCronometro();
    }

    private static int localizarPivote(int inicio, int fin, CriterioOrdenamiento criterio) {
        Documento dPivote = documentos[inicio];
        int posicionPivote = inicio;
        for (int i = inicio + 1; i < fin + 1; i++) {
            if (esMayor(dPivote, documentos[i], criterio)) {
                posicionPivote++;
                if (i != posicionPivote) {
                    intercambiar(i, posicionPivote);
                }
            }
        }
        if (inicio != posicionPivote) {
            intercambiar(inicio, posicionPivote);
        }
        return posicionPivote;
    }

    //algoritmo recursivo
    private static void ordenarRapido(int inicio, int fin, CriterioOrdenamiento criterio) {
        //punto de finalizacion
        if (inicio >= fin) {
            return;
        }
        int posicionPivote = localizarPivote(inicio, fin, criterio);
        ordenarRapido(inicio, posicionPivote - 1, criterio);
        ordenarRapido(posicionPivote + 1, fin, criterio);
    }

    public static String ordenarRapido(CriterioOrdenamiento criterio) {
        Util.iniciarCronometro();
        ordenarRapido(0, documentos.length - 1, criterio);
        return Util.obtenerTextoTiempoCronometro();
    }

}
