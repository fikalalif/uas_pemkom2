package util;

import model.Policy;

import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class PolicySerializer {
    private static final String KEY = "MySecretKey12345"; // 128-bit (16 char)

    public static void save(List<Policy> list, File file) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secret = new SecretKeySpec(KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);

        try (CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(file), cipher);
             ObjectOutputStream oos = new ObjectOutputStream(cos)) {
            oos.writeObject(list);
        }
    }

    public static List<Policy> load(File file) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secret = new SecretKeySpec(KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secret);

        try (CipherInputStream cis = new CipherInputStream(new FileInputStream(file), cipher);
             ObjectInputStream ois = new ObjectInputStream(cis)) {
            return (List<Policy>) ois.readObject();
        }
    }
}
