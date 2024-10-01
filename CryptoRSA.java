package AulaCalvetti;

import java.io.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.cert.*;

public class CryptoRSA {
    private byte[] textoCifrado;
    private byte[] textoDecifrado;

    public CryptoRSA() {
        textoCifrado = null;
        textoDecifrado = null;
    }

    public void geraParDeChaves(File fPub, File fPvk) 
            throws IOException, NoSuchAlgorithmException, CertificateException,
                   KeyStoreException, InvalidAlgorithmParameterException {
        final int RSA_KEY_SIZE = 1024;
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(new RSAKeyGenParameterSpec(RSA_KEY_SIZE, RSAKeyGenParameterSpec.F4));
        KeyPair kpr = kpg.generateKeyPair();
        PrivateKey oPriv = kpr.getPrivate();
        PublicKey oPub = kpr.getPublic();
        
        // Gravando a chave pública em formato serializado
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fPub));
        oos.writeObject(oPub);
        oos.close();
        
        // Gravando a chave privada em formato serializado
        oos = new ObjectOutputStream(new FileOutputStream(fPvk));
        oos.writeObject(oPriv);
        oos.close();
    }

    public void geraCifra(byte[] texto, File fPub) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
                   IllegalBlockSizeException, BadPaddingException,
                   InvalidAlgorithmParameterException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fPub));
        PublicKey oPub = (PublicKey) ois.readObject();
        ois.close();
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, oPub);
        textoCifrado = rsaCipher.doFinal(texto);
    }

    public byte[] getTextoCifrado() throws Exception {
        return textoCifrado;
    }

    public void geraDecifra(byte[] texto, File fPvk) 
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
                   IllegalBlockSizeException, BadPaddingException,
                   InvalidAlgorithmParameterException, IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fPvk));
        PrivateKey oPriv = (PrivateKey) ois.readObject();
        ois.close();
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, oPriv);
        textoDecifrado = rsaCipher.doFinal(texto);
    }

    public byte[] getTextoDecifrado() throws Exception {
        return textoDecifrado;
    }
}
    

