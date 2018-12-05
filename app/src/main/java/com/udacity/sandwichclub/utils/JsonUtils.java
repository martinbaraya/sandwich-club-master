package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /**
     * This method will take a json string as input and use that
     * json data to build a Sandwich object
     */
    public static Sandwich parseSandwichJson(String json) throws JSONException {
        Log.d("parseSandwichJson", json);

        JSONObject sandwichData = new JSONObject(json);

        JSONObject sandwichName = sandwichData.getJSONObject("name");

        List<String> listAlsoKnownAs = new ArrayList<String>();
        JSONArray arrAlsoKnownAs = sandwichName.getJSONArray("alsoKnownAs");
        for (int i=0; i<arrAlsoKnownAs.length(); i++) {
            listAlsoKnownAs.add(arrAlsoKnownAs.optString(i));
        }

        // ArrayList to hold the ingredients Strings
        List<String> listIngredients = new ArrayList<String>();
        JSONArray arrIngredients = sandwichData.getJSONArray("ingredients");
        for (int i=0; i<arrIngredients.length(); i++) {
            listIngredients.add(arrIngredients.optString(i));
        }

        // Build the sandwich object
        Sandwich sandwich = new Sandwich(
                sandwichName.optString("mainName"),
                listAlsoKnownAs,
                sandwichData.optString("placeOfOrigin"),
                sandwichData.optString("description"),
                sandwichData.optString("image"),
                listIngredients
        );

        return sandwich;
    }
}
