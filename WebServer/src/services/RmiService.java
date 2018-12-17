package services;

import core.AlbumInterface;
import core.ArtistInterface;
import core.UserInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiService {
    private static RmiService instance;
    public UserInterface userInterface;
    public AlbumInterface albumInterface;
    public ArtistInterface artistInterface;
    int RMI_PORT = 8000;
    String RMI_IP = "localhost";

    private RmiService() { }

    public void connect() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(RMI_IP, RMI_PORT);
        userInterface = (UserInterface) registry.lookup("UserInterface");
        albumInterface = (AlbumInterface) registry.lookup("AlbumInterface");
        artistInterface = (ArtistInterface) registry.lookup("ArtistInterface");
    }

    public static synchronized RmiService getInstance() throws RemoteException, NotBoundException {
        if (instance == null) {
            instance = new RmiService();
            instance.connect();
        }
        return instance;
    }
}
