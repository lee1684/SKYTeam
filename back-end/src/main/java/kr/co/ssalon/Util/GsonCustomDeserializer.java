package kr.co.ssalon.Util;

import com.google.gson.*;
import kr.co.ssalon.DTO.Image;
import kr.co.ssalon.DTO.TextBox;
import kr.co.ssalon.DTO.TicketJsonObject;

import java.lang.reflect.Type;

public class GsonCustomDeserializer implements JsonDeserializer<TicketJsonObject> {

    @Override
    public TicketJsonObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String objectType = jsonObject.get("type").getAsString();

        // Type에 따라 다른 클래스로 Deserialize
        switch (objectType) {
            case "textbox":
                return context.deserialize(json, TextBox.class);
            case "image":
                return context.deserialize(json, Image.class);
            default:
                throw new JsonParseException("Unknown object type: " + objectType);
        }
    }
}
