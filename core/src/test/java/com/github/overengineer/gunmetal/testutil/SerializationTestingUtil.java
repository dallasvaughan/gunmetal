package com.github.overengineer.gunmetal.testutil;

import java.io.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SerializationTestingUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T getSerializedCopy(T target) {

        // test serialization
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(target);
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fail("Object could not be serialized:  " + ioe.getMessage());
        }

        // assert that serialization produced something
        byte[] bytes = out.toByteArray();
        assertTrue(bytes.length > 0);

        // test deserialization
        Object o = null;
        try {
            InputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(in);
            o = ois.readObject();
        } catch (ClassNotFoundException cnfe) {
            fail("The object could not be deserialized.  There was an error finding its class:"
                    + cnfe.getMessage());
        } catch (IOException ioe) {
            fail("The object could not be deserialized:  " + ioe.getMessage());
        }

        // cast the deserialized object back to the original's class
        T copy = null;
        try {
            copy = (T) o;
        } catch (ClassCastException cce) {
            fail("Deserialized instance's class does not match original:  "
                    + cce.getMessage());
        }

        return copy;
    }

}
