package kr.co.igns.framework.utils.type;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;

public class GoogleOtpHelper {
	public static String getQRBarcodeURL(String user, String host, String secret) {
		String format = "http://chart.apis.google.com/chart?cht=qr&chs=200x200&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s&chld=H|0";
		return String.format(format, user, host, secret);
	}

	public static HashMap generateOTPKey(String sHost, String sUserid) {
		HashMap oRet = new HashMap();

		try {
			byte[] buffer = new byte[30];
			(new Random()).nextBytes(buffer);
			Base32 codec = new Base32();
			byte[] secretKey = Arrays.copyOf(buffer, 10);
			byte[] bEncodedKey = codec.encode(secretKey);
			String encodedKey = new String(bEncodedKey);
			System.out.println(encodedKey);
			String url = getQRBarcodeURL(sUserid, sHost, encodedKey);
			oRet.put("ERR", "0");
			oRet.put("OTPKEY", encodedKey);
			oRet.put("OTPURL", url);
		} catch (Exception var9) {
			oRet.put("ERR", "-1");
		}

		return oRet;
	}

	public static boolean checkCode(String userCode, String otpkey) {
		long otpnum = (long) Integer.parseInt(userCode);
		Instant instant = Instant.now();
		long wave = instant.toEpochMilli() / 30000L;
		boolean result = false;

		try {
			Base32 codec = new Base32();
			byte[] decodedKey = codec.decode(otpkey);
			int window = 3;

			for (int i = -window; i <= window; ++i) {
				long hash = (long) verify_code(decodedKey, wave + (long) i);
				if (hash == otpnum) {
					result = true;
				}
			}
		} catch (NoSuchAlgorithmException | InvalidKeyException var14) {
			var14.printStackTrace();
		}

		return result;
	}

	private static int verify_code(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;

		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) ((int) value);
		}

		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[19] & 15;
		long truncatedHash = 0L;

		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			truncatedHash |= (long) (hash[offset + i] & 255);
		}

		truncatedHash &= 2147483647L;
		truncatedHash %= 1000000L;
		return (int) truncatedHash;
	}
}