package ro.gagarin;

import org.junit.Test;

import ro.gagarin.wsclient.WSClient;

public class WSClientTest {
	@Test
	public void testCreateWSClient() throws Exception {
		WSClient client = WSClient.getWSClient("http://localhost:8080/ws/");
		client.getAuthentication().createSession(null, null);
		// TODO: add more tests
		// client.getUserService().getAllPermissionList(session);
	}
}
