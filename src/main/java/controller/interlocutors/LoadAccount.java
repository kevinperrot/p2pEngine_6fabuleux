package controller.interlocutors;

import java.io.IOException;

import javax.websocket.Session;

import model.data.user.User;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import controller.ManagerBridge;

public class LoadAccount extends AbstractInterlocutor {

	public LoadAccount() {
	}

	@Override
	public void sender(String msg, Session session) throws JSONException,
			IOException {
		User user = ManagerBridge.getCurrentUser();
		JSONObject data = new JSONObject();
		data.put("query", "accountLoaded");
		JSONObject content = new JSONObject();
		content.put("username", user.getNick());
		content.put("name", user.getName());
		content.put("firstname", user.getFirstName());
		content.put("phone", user.getPhone());
		content.put("email", user.getEmail());
		data.put("content", content);
		session.getBasicRemote().sendText(data.toString());
	}

}
