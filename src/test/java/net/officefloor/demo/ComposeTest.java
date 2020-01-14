package net.officefloor.demo;

import net.officefloor.server.http.mock.MockHttpResponse;
import net.officefloor.woof.mock.MockWoofServer;
import net.officefloor.woof.mock.MockWoofServerRule;
import org.junit.Rule;
import org.junit.Test;

public class ComposeTest {

    @Rule
    public final MockWoofServerRule server = new MockWoofServerRule();

    @Test
    public void requestCompose() {
        MockHttpResponse response = this.server.send(MockWoofServer.mockRequest("/compose"));
        response.assertResponse(200, "{\"message\":\"Hi, via Cats, via Reactor, via ZIO, via Imperative\"}");
    }

}
