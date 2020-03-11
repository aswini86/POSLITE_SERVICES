package com.retail.services.util;

import java.util.Random;

public class CommonUtil {
	
	public static String getRandomString(int number) {
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)) + "-");
		for (int i = 0; i < number; i++)
		    sb.append(chars[rnd.nextInt(chars.length)]);

		return sb.toString();
	}

}
