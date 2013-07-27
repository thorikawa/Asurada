package com.polysfactory.asurada;

import android.content.Context;
import android.util.Log;

import com.polysfactory.asurada.util.UrlSigner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by poly on 7/26/13.
 */
public class GooglePlacesApiWrapper {

    private String mApiKey;
    private UrlSigner mSigner;

    public GooglePlacesApiWrapper(Context context) {
        try {
            InputStream in = context.getAssets().open("setting.properties");
            Properties prop = new Properties();
            prop.load(in);
            mApiKey = prop.getProperty("google_api_key");
            mSigner = new UrlSigner(mApiKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getFoodPlaces() {
        String urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.32,-122.01&radius=8000&types=food&rankby=prominence&sensor=false&key=" + mApiKey;
        List<String> names = new ArrayList<String>();
        try {
            URL url = new URL(urlString);
            String request = mSigner.signRequest(url.getPath(), url.getQuery());
            String target = url.getProtocol() + "://" + url.getHost() + request;
            Log.d(App.TAG, "Signed URL :" + target);
            String data = get(new URL(target));
            JSONObject json = new JSONObject(data);
            JSONArray results = json.getJSONArray("results");
            int len = Math.min(3, results.length());
            for (int i = 0; i < len; i++) {
                JSONObject r = results.getJSONObject(i);
                String name = r.optString("name");
                names.add(name);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return names;
    }

    public String get(URL url) {
        String data = null;
        HttpsURLConnection urlConnection = null;
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            data = readStream(in);
            Log.d(App.TAG, data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return data;
    }

    private String readStream(InputStream in) throws IOException {
        InputStreamReader is = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();
        while (read != null) {
            sb.append(read);
            read = br.readLine();
        }
        return sb.toString();
    }
}