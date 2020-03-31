package pl.strzelecki.spaceagency.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/start")
public class StartController {

    @GetMapping(path = "/info", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> start() {
        JsonObject jsonObjectCM1 = new JsonObject();
        JsonObject jsonObjectCM2 = new JsonObject();
        JsonObject jsonObjectCUS1 = new JsonObject();
        JsonObject jsonObjectCUS2 = new JsonObject();
        JsonArray jsonArrayContentManager = new JsonArray();

        jsonObjectCM1.addProperty("Content manager 1 username", "pnowak");
        jsonObjectCM1.addProperty("Content manager 1 password", "nowakp");
        jsonObjectCM2.addProperty("Content manager 2 username", "jkowalski");
        jsonObjectCM2.addProperty("Content manager 2 password", "kowalskij");
        jsonObjectCUS1.addProperty("Customer 1 username", "amarciniak");
        jsonObjectCUS1.addProperty("Customer 1 password", "marciniaka");
        jsonObjectCUS2.addProperty("Customer 2 username", "hkrupa");
        jsonObjectCUS2.addProperty("Customer 2 password", "krupah");

        jsonArrayContentManager.add(jsonObjectCM1);
        jsonArrayContentManager.add(jsonObjectCM2);
        jsonArrayContentManager.add(jsonObjectCUS1);
        jsonArrayContentManager.add(jsonObjectCUS2);
        return new ResponseEntity<>(jsonArrayContentManager.toString(), HttpStatus.OK);
    }
}
