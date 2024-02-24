import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

// BEGIN SOLUTION
// Please import only standard libraries and make sure that your code compiles and runs without unhandled exceptions 
// END SOLUTION
 
public class HW2 {    

  static void P1() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher1.bmp"));
    
    // BEGIN SOLUTION
    byte[] key = new byte[] { 1, 2, 3, 4, 
                              5, 6, 7, 8, 
                              9, 10, 11, 12, 
                              13, 14, 15, 16 };

    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
    IvParameterSpec iv = new IvParameterSpec(new byte[16]);

    Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

    byte[] plainBMP = cipher.doFinal(cipherBMP);    
    // END SOLUTION
    
    Files.write(Paths.get("plain1.bmp"), plainBMP);
  }

  static void P2() throws Exception {
    byte[] cipherBin = Files.readAllBytes(Paths.get("cipher2.bin"));
    
    Map<Integer, SimpleEntry<Integer, Integer>> blockOrder = new TreeMap<>();
    Integer counter = 1;

    for (int i = 0; i < cipherBin.length; i += 16) {
        blockOrder.put(counter++, new SimpleEntry<>(i, i + 16));
    }

    List<List<Integer>> combinations = combinationsOrder(Arrays.asList(1, 2, 3));

    Map<Integer, byte[]> orderedBlocks = new LinkedHashMap<>();

    List<byte[]> combined3blocks  = new ArrayList<>();

    for (List<Integer> combination : combinations) {
        byte[] combined3block = new byte[48];
        for (int i = 0; i < combination.size(); i++) {
            orderedBlocks.put(combination.get(i), Arrays.copyOfRange(cipherBin, blockOrder.get(combination.get(i)).getKey(), blockOrder.get(combination.get(i)).getValue()));
            System.arraycopy(orderedBlocks.get(combination.get(i)), 0, combined3block, i * 16, 16);
        }
        combined3blocks.add(combined3block);
    }

    for (byte[] combined3block : combined3blocks) {
      byte[] plain = decryptBlock(combined3block);
      Files.write(Paths.get("plain2.txt"), plain);
    }

  }

  static List<List<Integer>> combinationsOrder(List<Integer> blockOrder) {
    List<List<Integer>> result = new ArrayList<>();
    for (int i = 0; i < blockOrder.size(); i++) {
        for (int j = 0; j < blockOrder.size(); j++) {
            for (int k = 0; k < blockOrder.size(); k++) {
                if (i != j && j != k && i != k) {
                    result.add(Arrays.asList(blockOrder.get(i), blockOrder.get(j), blockOrder.get(k)));
                }
            }
        }
    }
    return result;
  }

  static byte[] decryptBlock(byte[] block) throws Exception {
    byte[] key = new byte[] {
      1, 2, 3, 4, 
      5, 6, 7, 8, 
      9, 10, 11, 12, 
      13, 14, 15, 16
    };
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
    IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
    return cipher.doFinal(block);
  }


  static void P3() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher3.bmp"));
    byte[] otherBMP = Files.readAllBytes(Paths.get("plain1.bmp"));

    Integer counter = 0;

    while (counter < 2000 ){
      cipherBMP[counter] = otherBMP[counter];
      counter++;
    }

    byte[] modifiedBMP = cipherBMP;

    Files.write(Paths.get("cipher3_modified.bmp"), modifiedBMP);

  }

  static void P4() throws Exception {
    byte[] plainTextA = Files.readAllBytes(Paths.get("plain4A.txt"));
    byte[] cipherTextA = Files.readAllBytes(Paths.get("cipher4A.bin"));
    byte[] cipherTextB = Files.readAllBytes(Paths.get("cipher4B.bin"));

    byte[] keystream = new byte[cipherTextA.length];
    for (int i = 0; i < cipherTextA.length; i++) {
      keystream[i] = (byte) (plainTextA[i] ^ cipherTextA[i]);
    }

    byte[] plainB = new byte[cipherTextB.length];
    for (int i = 0; i < cipherTextB.length; i++) {
      plainB[i] = (byte) (keystream[i] ^ cipherTextB[i]);
    }

    Files.write(Paths.get("plain4B.txt"), plainB);
  }

  static void P5() throws Exception {
    byte[] cipherBMP = Files.readAllBytes(Paths.get("cipher5.bmp"));
    byte[] key = findKey(cipherBMP);
    
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
    IvParameterSpec iv = new IvParameterSpec(new byte[16]);
    Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

    byte[] plainBMP = cipher.doFinal(cipherBMP);

    
    Files.write(Paths.get("plain5.bmp"), plainBMP);
  }

static byte [] findKey(byte[] cipherBMP) throws Exception {
  byte [] iv = new byte[16];
  byte[] key = new byte[] {
    0,0,0,0,
    0,0,0,0,
    0,0,0,0,
    0,0,0,0
  };
  for (int i = 0; i < 100; i++) {
    for (int j = 1; j < 13; j++) {
      for (int k = 1; k < 32; k++) {
        key[0] = (byte) i;
        key[1] = (byte) j;
        key[2] = (byte) k;
        try {
          if (checkKey(cipherBMP, key, iv)){
            key[0] = (byte) i;
            key[1] = (byte) j;
            key[2] = (byte) k;
            return key;
          }
        } catch (Exception e) {
          continue;
        }
      }
    }
  }
  return key;
}


  static boolean checkKey(byte[] block, byte[] key, byte[] iv) throws Exception {
    SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
    byte[] decryptedData = cipher.doFinal(block);
    return (decryptedData[0] == 66 && decryptedData[1] == 77);
  }

  public static void main(String [] args) {
    try {  
      P1();
      P2();
      P3();
      P4();
      P5();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}