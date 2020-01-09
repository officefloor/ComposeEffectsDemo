package net.officefloor.demo;

import net.officefloor.server.http.HttpMethod;
import net.officefloor.server.http.mock.MockHttpResponse;
import net.officefloor.woof.mock.MockWoofServer;
import net.officefloor.woof.mock.MockWoofServerRule;
import org.junit.Rule;
import org.junit.Test;

public class DemoTest {

    @Rule
    public final MockWoofServerRule server = new MockWoofServerRule();

    @Test
    public void requestCats() {
        MockHttpResponse response = this.server.send(
                MockWoofServer.mockJsonRequest(HttpMethod.POST, "/cats", new ServerRequest(1)));
        response.assertResponse(200, "{\"message\":\"Hi via Cats\"}");
    }

    @Test
    public void requestZIO() {
        MockHttpResponse response = this.server.send(
                MockWoofServer.mockJsonRequest(HttpMethod.POST, "/zio", new ServerRequest(1)));
        response.assertResponse(200, "{\"message\":\"Hi via ZIO\"}");
    }

    @Test
    public void requestReactor() {
        MockHttpResponse response = this.server.send(
                MockWoofServer.mockJsonRequest(HttpMethod.POST, "/reactor", new ServerRequest(1)));
        response.assertResponse(200, "{\"message\":\"Hi via Reactor\"}");
    }

    @Test
    public void requestImperative() {
        MockHttpResponse response = this.server.send(
                MockWoofServer.mockJsonRequest(HttpMethod.POST, "/imperative", new ServerRequest(1)));
        response.assertResponse(200, "{\"message\":\"Hi via Imperative\"}");
    }

}
