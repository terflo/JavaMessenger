package com.company.app.network;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JSONParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public synchronized String convertToString(Request request) throws IOException {
        return request.getClass().getSimpleName() + "//" + mapper.writeValueAsString(request);
    }

    public synchronized Request convertToRequest(String str) throws IOException {
        Request request = null;
        switch (str.substring(0, str.indexOf("//"))){
            case "MessageRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), MessageRequest.class);
                break;
            case "LoginRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), LoginRequest.class);
                break;
            case "KeyRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), KeyRequest.class);
                break;
            case "RegisterRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), RegisterRequest.class);
                break;
            case "ChangesRequest":
                request = mapper.readValue(str.substring(str.indexOf("//")+2), ChangesRequest.class);
                break;
            default:
                break;
        }
        return request;
    }
}
