import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Servidor {

    public static void main(String[] args) {
        Tablero juego = null;

        try {
            // Socket de servidor
            DatagramSocket socket = new DatagramSocket(4040);
            System.out.println("Servidor escuchando ..............");

            while (true) {
                // Aceptar paquete de cliente y abrir flujos de IO
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                System.out.println("Cliente conectado");

                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                ObjectInputStream is = new ObjectInputStream(bais);

                Jugada j = (Jugada) is.readObject();

                if (j != null) {
                    if (j.getX() < 0 && j.getY() < 0) {
                        System.out.println("Nuevo juego");
                        juego = new Tablero(j.getTipo());
                    } else {
                        juego.hazJugada(j.getX(), j.getY(), j.getTipo());
                        if (juego.getEstadoJuego() == -1) {
                            System.out.println("El jugador perdio");
                        }
                    }
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(juego);
                oos.flush();

                byte[] responseBuffer = baos.toByteArray();
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            }

        } catch (ClassNotFoundException ce) {
        } catch (SocketException se) {
        } catch (IOException ex) {
        }
    }
}
