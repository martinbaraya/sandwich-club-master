package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mOriginTextView = null;
    private TextView mAlsoKnownTextView = null;
    private TextView mIngredientsTextView = null;
    
    private TextView mDescriptionTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mOriginTextView = (TextView) findViewById(R.id.origin_tv);
        mAlsoKnownTextView = (TextView) findViewById(R.id.also_known_tv);
        mIngredientsTextView = (TextView) findViewById(R.id.ingredients_tv);
        mDescriptionTextView = (TextView) findViewById(R.id.description_tv);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method will take a sandwich object as input
     * and use that object's data to populates the UI.
     */
    private void populateUI(Sandwich sandwich) {
        mDescriptionTextView.setText(sandwich.getDescription());

        // if the PlaceOfOrigin is empty set the Origin to N/A (not available)
        mOriginTextView.setText(sandwich.getPlaceOfOrigin().isEmpty() ? getString(R.string.not_available) : sandwich.getPlaceOfOrigin());

        /*
            The sandwich.getAlsoKnownAs() returns a list of Names,
            if the list has more than one item an newline is added
            between items
         */
        if (sandwich.getAlsoKnownAs().size() > 0) {
            mAlsoKnownTextView.setText(TextUtils.join("\n", sandwich.getAlsoKnownAs()));
        } else {
            mAlsoKnownTextView.setText(getString(R.string.not_available));
        }

        /*
            The sandwich.getIngredients() returns a list of Ingredients,
            if the list has more than one item an newline is added
            between items
         */
        mIngredientsTextView.setText(TextUtils.join("\n", sandwich.getIngredients()));

    }
}
