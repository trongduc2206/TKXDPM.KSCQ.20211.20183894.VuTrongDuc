package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;

import entity.payment.CreditCard;
import entity.payment.PaymentTransaction;

/**
 * Class cung cap cac phuong thuc giup gui request len server va nhan du lieu tra ve
 * Date 07/12/2021
 * @author Vu Trong Duc - 20183894
 * @version 1.0
 */
public class API {
	/**
	 * Thuoc tinh giup log ra thong tin tren console
	 */
	private static Logger LOGGER = Utils.getLogger(Utils.class.getName());

	/**
	 * Phuong thuc giup goi cac API dang GET
	 * @param url path to server resource
	 * @param token authenticate
	 * @return response: phan hoi tu server (dang String)
	 * @throws Exception
	 */
	public static String get(String url, String token) throws Exception {
		// Vu Trong Duc - 20183894
		//setup ket noi http
		HttpURLConnection conn = getHttpURLConnection(url, token, "GET");
		// tra ve du lieu tra ve tu server
		return readResponse(conn);
	}

	/**
	 * Phuong thuc giup goi cac API dang POST
	 * @param url path to server resource
	 * @param data request body
	 * @return response: phan hoi tu server (dang String)
	 * @throws IOException
	 */
	public static String post(String url, String data, String token) throws IOException {
		// Vu Trong Duc - 20183894
		allowMethods("PATCH");
		//setup ket noi http
		HttpURLConnection conn = getHttpURLConnection(url, token, "PATCH");

		//gui du lieu
		Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		writer.write(data);
		writer.close();
		//tra ve du lieu tra ve tu server
		return readResponse(conn);
	}

	/**
	 * Phuong thuc giup doc response
	 * @param conn http url connection
	 * @return response: phan hoi tu server (dang String)
	 * @throws IOException
	 */
	private static String readResponse(HttpURLConnection conn) throws IOException {
		BufferedReader in;
		String inputLine;
		if (conn.getResponseCode() / 100 == 2) {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		LOGGER.info("Respone Info: " + response.toString());
		return response.toString();
	}

	/**
	 * Phuong thuc mo method cho ket noi http
	 * @param methods methods are allowed
	 */
	private static void allowMethods(String... methods) {
		// Vu Trong Duc - 20183894
		try {
			Field methodsField = HttpURLConnection.class.getDeclaredField("methods");
			methodsField.setAccessible(true);

			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

			String[] oldMethods = (String[]) methodsField.get(null);
			Set<String> methodsSet = new LinkedHashSet<>(Arrays.asList(oldMethods));
			methodsSet.addAll(Arrays.asList(methods));
			String[] newMethods = methodsSet.toArray(new String[0]);

			methodsField.set(null/* static field */, newMethods);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Phuong thuc giup khoi tao ket noi http den server
	 * @param url path to server resource
	 * @param token authenticate
	 * @param method method to create connection
	 * @return HttpURLConnection
	 * @throws IOException
	 */
	private static HttpURLConnection getHttpURLConnection(String url, String token, String method) throws IOException {
		// Vu Trong Duc - 20183894
		LOGGER.info("Request URL: " + url + "\n");
		URL line_api_url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) line_api_url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod(method);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Authorization", "Bearer " + token);
		return conn;
	}

}
