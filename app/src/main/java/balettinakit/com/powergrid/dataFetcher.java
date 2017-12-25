package balettinakit.com.powergrid;

import android.os.AsyncTask;

/**
 * Created by Olli Peura on 12/22/2017.
 */

class dataFetcher {

    static void doFetch(fetchData mfechdata){
        new fecth().execute(mfechdata);
    }

    private static class fecth extends AsyncTask<fetchData, Void, String> {

        @Override
        protected String doInBackground(fetchData... params) {
            params[0].doInBackground();

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

}
