import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.BadPaddingException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

// BEGIN SOLUTION
// Please import only standard libraries and make sure that your code compiles and runs without unhandled exceptions 
// END SOLUTION
 
public class HW2 {    

  static void P1() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher1.bmp"));
    
    // BEGIN SOLUTION
    byte[] key = new byte[] { 0, 0, 0, 0, 
                              0, 0, 0, 0, 
                              0, 0, 0, 0, 
                              0, 0, 0, 0 };
    byte[] plainBMP = cipherBMP;    
    // END SOLUTION
    
    Files.write(Paths.get("plain1.bmp"), plainBMP);
  }

  static void P2() throws Exception {
    byte[] cipher = Files.readAllBytes(Paths.get("cipher2.txt"));
    // BEGIN SOLUTION
    byte[] modifiedCipher = cipher;
    modifiedCipher[0] = cipher[16];
    modifiedCipher[16] = cipher[0];
    byte[] plain = modifiedCipher;

    // END SOLUTION
    
    Files.write(Paths.get("plain2.txt"), plain);
  }

  static void P3() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher3.bmp"));
    byte[] otherBMP = Files.readAllBytes(Paths.get("plain1.bmp"));
    
    // BEGIN SOLUTION
    byte[] modifiedBMP = cipherBMP;

    // END SOLUTION
    
    Files.write(Paths.get("cipher3_modified.bmp"), modifiedBMP);
  }

  static void P4() throws Exception {
    byte[] plainA = Files.readAllBytes(Paths.get("plain4A.txt"));
    byte[] cipherA = Files.readAllBytes(Paths.get("cipher4A.txt"));
    byte[] cipherB = Files.readAllBytes(Paths.get("cipher4B.txt"));
    
    // BEGIN SOLUTION
    byte[] plainB = cipherB;

    // END SOLUTION
    
    Files.write(Paths.get("plain4B.txt"), plainB);
  }

  static void P5() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher5.bmp"));
    
    // BEGIN SOLUTION
    byte[] plainBMP;
    byte[] key = new byte[] {   0,   0,    0,   0, 
                                0,   0,    0,   0,
                                0,   0,    0,   0,
                                0,   0,    0,   0 }; // byte array size
    // try {
      plainBMP = cipherBMP;
      // decryption might throw a BadPaddingException!
    // }
    // catch (BadPaddingException e) {
    // }

    // END SOLUTION
    
    Files.write(Paths.get("plain5.bmp"), plainBMP);
  }

  public static void main(String [] args) {
    try {  
      P1();
      //P2();
      //P3();
      //P4();
      //P5();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}