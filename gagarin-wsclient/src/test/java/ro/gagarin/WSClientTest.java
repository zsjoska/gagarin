package ro.gagarin;

import org.junit.Test;

import ro.gagarin.wsclient.WSClient;

public class WSClientTest {
    @Test
    public void testCreateWSClient() throws Exception {
	WSClient client = WSClient.getWSClient("http://localhost:8080/gagarin-ws/ws/");
	client.getAuthentication().createSession(null, "TEST");
	// TODO: add more tests
	// client.getUserService().getAllPermissionList(session);
    }
}
