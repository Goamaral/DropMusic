package core;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.*;

public class Serializer {
    public static String serialize(Object resource) throws CustomException {
        try {
            ObjectOutputStream oOS;
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
            oOS = new ObjectOutputStream(bAOS);
            oOS.writeObject(resource);
            oOS.flush();
            String stringData = Base64.encode(bAOS.toByteArray());
            return stringData;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Serialize error");
            throw new CustomException("Internal error");
        }
    }

    public static Object deserialize(String resource_string) throws CustomException {
        try {
            byte resource_bytes[] = Base64.decode(resource_string);
            ByteArrayInputStream bAIS = new ByteArrayInputStream(resource_bytes);
            ObjectInputStream oIS = new ObjectInputStream(bAIS);
            return oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CustomException("Internal error");
        }
    }
}
