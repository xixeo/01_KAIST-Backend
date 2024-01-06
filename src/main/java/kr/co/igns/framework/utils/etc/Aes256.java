package kr.co.igns.framework.utils.etc;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class Aes256 {
	
	public static final String AES_KEY = "iljoogns004134112345678123456789";
	public static final String IV = "iljoogns12345678";
	
	/**
	 * ?��?��?��
	 * @param key
	 * @param iv
	 * @param text
	 * @return
	 */
	public static String encryptString(String text) {
		if (text == null) {
			text = "";
		}
		try {
			return new String(Base64.encodeBase64(cryptProcess(text.getBytes("UTF-8"), true))).replaceAll("\\n","");
		} catch (UnsupportedEncodingException e) {
			return new String(Base64.encodeBase64(cryptProcess(text.getBytes(), true))).replaceAll("\\n","");
		}
	}
	
	/**
	 * 복호?��
	 * @param key
	 * @param iv
	 * @param text
	 * @return
	 */
	public static String decryptString(String text) {
		if (text != null && text.length() > 0) {
			try {
				return new String(cryptProcess(Base64.decodeBase64(text), false), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return new String(cryptProcess(Base64.decodeBase64(text), false));
			}
		} else {
			return "";
		}
	}
	
	/**
	 * ?��?��?��/복호?�� ?��로세?��
	 * @param key
	 * @param iv
	 * @param text
	 * @param mode
	 * @return
	 */
	private static byte[] cryptProcess(byte[] text, boolean mode) {
		
		byte[] resultString = null;
		
		try {
			BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
			
			cipher.init(mode, new ParametersWithIV(new KeyParameter(AES_KEY.getBytes()), IV.getBytes()));
			
			ByteArrayInputStream input = new ByteArrayInputStream(text);
	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        
	        int inputLen;
	        int outputLen;
	        
	        byte[] inputBuffer = new byte[4096];
	        byte[] outputBuffer = new byte[cipher.getOutputSize(inputBuffer.length)];
	        
	        while ((inputLen = input.read(inputBuffer)) > -1) {
	            outputLen = cipher.processBytes(inputBuffer, 0, inputLen, outputBuffer, 0);
	            if (outputLen > 0) {
	                output.write(outputBuffer, 0, outputLen);
	            }
	        }
	        
	        outputLen = cipher.doFinal(outputBuffer, 0);
	        
	        if (outputLen > 0) {
	            output.write(outputBuffer, 0, outputLen);
	        }
	        
			resultString = output.toByteArray();
			
			input.close();
			output.close();
			
		} catch (DataLengthException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		} catch (IOException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		}
    	
    	return resultString;
	}
	
	/**
	 * ?��?�� ?��?��?��
	 * @param inputPath
	 * @param outputPath
	 */
	public static void encryptFile(String inputPath, String outputPath) {
		
		try {
			BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
			
			cipher.init(true, new ParametersWithIV(new KeyParameter(AES_KEY.getBytes()), IV.getBytes()));
			
			InputStream input = new FileInputStream(inputPath);
	        OutputStream output = new FileOutputStream(outputPath);
	        
	        int inputLen;
	        int outputLen;
	        
	        byte[] inputBuffer = new byte[4096];
	        byte[] outputBuffer = new byte[cipher.getOutputSize(inputBuffer.length)];
	        
	        while ((inputLen = input.read(inputBuffer)) > -1) {
	            outputLen = cipher.processBytes(inputBuffer, 0, inputLen, outputBuffer, 0);
	            if (outputLen > 0) {
	                output.write(outputBuffer, 0, outputLen);
	            }
	        }
	        
	        outputLen = cipher.doFinal(outputBuffer, 0);
	        
	        if (outputLen > 0) {
	            output.write(outputBuffer, 0, outputLen);
	        }
            
            input.close();
			output.close();
        	
		} catch (DataLengthException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		} catch (IOException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {
			// TODO ?��?�� ?��?��?�� catch 블록
			e.printStackTrace();
		}
	}
	
	
	public static String sha256(String msg) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(msg.getBytes());
		
		return bytesToHex( md.digest() );
	}
	
	public static String bytesToHex(byte[] bytes){
		StringBuilder builder = new StringBuilder();
		for( byte b:bytes){
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
			
	}

}