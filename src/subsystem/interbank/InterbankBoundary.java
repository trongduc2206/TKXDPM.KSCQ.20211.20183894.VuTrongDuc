package subsystem.interbank;

import common.exception.UnrecognizedException;
import utils.API;
import views.screen.popup.PopupScreen;

import java.io.IOException;

public class InterbankBoundary {

	String query(String url, String data, String token) throws IOException {
		String response = null;
		try {
			response = API.post(url, data, token);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			PopupScreen.error(e.getMessage());
		}
		return response;
	}

}
