import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n, m, p;

        System.out.println("Bienvenido a buscaminas");
        System.out.println("Elige la dificultad");
        System.out.println("1.Facil");
        System.out.println("2.Intermedio");
        System.out.println("3.Avanzado");
        n = sc.nextInt();
        Tablero tab = Tiro(-1, -1, n);

        System.out.println(tab);
        while (tab.getEstadoJuego() == 0) {
            System.out.println("Elige una Opcion");
            System.out.println("1.Tirar");
            System.out.println("2.Marcar");
            n = sc.nextInt()-1;
            System.out.println("Ingrese las coordenadas, Primero Filas y luego Columnas");
            m = sc.nextInt()-1;
            p = sc.nextInt()-1;
            tab = Tiro(m, p, n);
            System.out.println(tab);
        }
        if (tab.getEstadoJuego() == 1) {
            System.out.println("------GANASTE------");
        } else {
            System.out.println("-----PERDISTE-----");
        }
    }

    public static Tablero Tiro(int x, int y, int jugada) {
        Tablero t = null;
        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            int port = 4040;
            DatagramSocket socket = new DatagramSocket();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            Jugada j1 = new Jugada();
            j1.setTipo(jugada);
            j1.setX(x);
            j1.setY(y);
            oos.writeObject(j1);
            oos.flush();

            byte[] buffer = baos.toByteArray();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
            socket.send(packet);

            byte[] responseBuffer = new byte[4096];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);

            ByteArrayInputStream bais = new ByteArrayInputStream(responseBuffer);
            ObjectInputStream ois = new ObjectInputStream(bais);

            t = (Tablero) ois.readObject();

        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }
        return t;
    }
}
