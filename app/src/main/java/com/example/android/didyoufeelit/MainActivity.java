/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.didyoufeelit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Displays the perceived strength of a single earthquake event based on responses from people who
 * felt the earthquake.
 */
public class MainActivity extends AppCompatActivity {

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new background task to grab information from a given URL
        EarthquakeAsyncTask backgroundTask = new EarthquakeAsyncTask();
        // Execute the background task
        backgroundTask.execute(USGS_REQUEST_URL);
    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(Event earthquake) {
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(earthquake.title);

        TextView tsunamiTextView = (TextView) findViewById(R.id.number_of_people);
        tsunamiTextView.setText(getString(R.string.num_people_felt_it, earthquake.numOfPeople));

        TextView magnitudeTextView = (TextView) findViewById(R.id.perceived_magnitude);
        magnitudeTextView.setText(earthquake.perceivedStrength);
    }


    /**
     * This is a private custom class that extends AsyncTask class
     * This class executes its given tasks on a background thread
     */
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, Event>{

        /**
         * In this method all the tasks which it is assigned are done on a separate
         * background method
         * @param strings is an array of parameters, in this case of type String
         * @return an Event object, which is actually details about an earthquake grabbed
         * from the params provided
         */
        @Override
        protected Event doInBackground(String... strings) {
            return Utils.fetchEarthquakeData(strings[0]);
        }


        /**
         * This method is done on the main thread after the background thread has finished
         * executing its tasks
         * It updates the UI with new information
         * @param event is an earthquake which is grabbed from a given URL and it is displayed
         *              displayed on the screen
         */
        @Override
        protected void onPostExecute(Event event) {
            updateUi(event);
        }
    }
}
