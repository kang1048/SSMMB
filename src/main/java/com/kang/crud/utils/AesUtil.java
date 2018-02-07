package com.kang.crud.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.util.StringUtils;

/**
 * AES加密的应用场景
 *  像我这里的话是对配置文件的数据库账号和密码进行加解密
 *  commons-codec-1.10.jar
 * @Author:kangjunyu
 * @Description:
 * @Date Create in 17:47 2018/2/6
 */
public class AesUtil {

  /**
   * AES加密
   * @param content 加密内容
   * @param key     键(限定16位)
   * @param iv      值
   * @return
   */
  public static String encrypt(String content, String key, String iv) {
    if (StringUtils.isEmpty(key)) {
      return null;
    }
    if (key.length() != 16) {
      return null;
    }
    String encryptText = "";
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

      IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
      byte[] encrypted = cipher.doFinal(content.getBytes());
      encryptText = parseByte2HexStr(encrypted);
    } catch (IllegalBlockSizeException e) {
    } catch (BadPaddingException e) {
    } catch (InvalidKeyException e) {
    } catch (InvalidAlgorithmParameterException e) {
    } catch (NoSuchAlgorithmException e) {
    } catch (NoSuchPaddingException e) {
    }
    return encryptText;
  }

  /**
   * AES解密
   * @param content
   * @param key
   * @param iv
   * @return
   */
  public static String decrypt(String content, String key, String iv) {
    if (StringUtils.isEmpty(key)) {
      return "AES加密KEY为空!";
    }
    if (key.length() != 16) {
      return "AES加密KEY长度必须16位!";
    }
    String decryptText = "";
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
      byte[] byteContent = parseHexStr2Byte(content);
      byte[] original = cipher.doFinal(byteContent);
      decryptText = new String(original);
    } catch (NoSuchAlgorithmException e) {
    } catch (NoSuchPaddingException e) {
    } catch (InvalidAlgorithmParameterException e) {
    } catch (InvalidKeyException e) {
    } catch (BadPaddingException e) {
    } catch (IllegalBlockSizeException e) {
    }
    return decryptText;
  }


  /**
   * 将二进制转换成16进制(加密原理)
   *
   * @return sb
   */
  public static String parseByte2HexStr(byte buf[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  }

  /**
   * 将16进制转换为二进制(解密原理)
   *
   * @return result
   */
  public static byte[] parseHexStr2Byte(String hexStr) {
    if (hexStr.length() < 1) {
      return null;
    }
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
      result[i] = (byte) (high * 16 + low);
    }
    return result;
  }

  public static void main(String[] args) {
    String key = "AkxfGVoMiKDxUiJM";
    String iv = "1841611841611010";
    String str = encrypt("root", key, iv);
    System.out.println(str);

    System.out.println(decrypt(str, key, iv));
  }
}
