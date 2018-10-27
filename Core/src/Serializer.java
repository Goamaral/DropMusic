import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Serializer {
    public static String serialize(Object resource) {

    }

    public static Object deserialize(String resource_string) throws CustomException {
        byte resource_bytes[] = Base64.decode(resource_string);
        ByteArrayInputStream bAIS = new ByteArrayInputStream(resource_bytes);

        try {
            ObjectInputStream oIS = new ObjectInputStream(bAIS);
            return oIS.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CustomException("Internal error");
        }
    }
}
