package net.officefloor.demo;

import net.officefloor.web.HttpObject;

@HttpObject
public class ServerRequest {

    private int id;

    public ServerRequest() {
    }

    public ServerRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
