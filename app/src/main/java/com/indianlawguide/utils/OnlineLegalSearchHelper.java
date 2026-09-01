package com.indianlawguide.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OnlineLegalSearchHelper {

    private static final String TAG = "OnlineLegalSearchHelper";

    public static class OnlineLegalItem {
        private final String title;
        private final String snippet;
        private final String sourceUrl;

        public OnlineLegalItem(String title, String snippet, String sourceUrl) {
            this.title = title;
            this.snippet = snippet;
            this.sourceUrl = sourceUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getSnippet() {
            return snippet;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }
    }

    public interface SearchCallback {
        void onOnlineResults(List<OnlineLegalItem> results);
        void onOfflineMode();
        void onError(String message);
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static void searchOnline(Context context, String query, SearchCallback callback) {
        if (!isNetworkAvailable(context)) {
            callback.onOfflineMode();
            return;
        }

        new Thread(() -> {
            try {
                String encodedQuery = URLEncoder.encode("Indian Law " + query, "UTF-8");
                String endpoint = "https://api.duckduckgo.com/?q=" + encodedQuery + "&format=json&no_redirect=1&no_html=1";

                URL url = new URL(endpoint);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                conn.setRequestProperty("User-Agent", "IndianLawGuide-App/1.0");

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();

                    JSONObject json = new JSONObject(sb.toString());
                    List<OnlineLegalItem> results = new ArrayList<>();

                    String heading = json.optString("Heading", "");
                    String abstractText = json.optString("AbstractText", "");
                    String abstractUrl = json.optString("AbstractURL", "");

                    if (!abstractText.isEmpty()) {
                        results.add(new OnlineLegalItem(
                            heading.isEmpty() ? "Official Legal Statutory Overview: " + query : heading,
                            abstractText,
                            abstractUrl.isEmpty() ? "https://india.gov.in" : abstractUrl
                        ));
                    }

                    JSONArray relatedTopics = json.optJSONArray("RelatedTopics");
                    if (relatedTopics != null) {
                        for (int i = 0; i < relatedTopics.length() && results.size() < 4; i++) {
                            JSONObject topic = relatedTopics.optJSONObject(i);
                            if (topic != null) {
                                String text = topic.optString("Text", "");
                                String firstUrl = topic.optString("FirstURL", "");
                                if (!text.isEmpty()) {
                                    results.add(new OnlineLegalItem(
                                        "Indian Legal Reference • Web Topic",
                                        text,
                                        firstUrl.isEmpty() ? "https://www.indiacode.nic.in" : firstUrl
                                    ));
                                }
                            }
                        }
                    }

                    if (results.isEmpty()) {
                        results.add(new OnlineLegalItem(
                            "India Code & Ministry Legal Repository",
                            "Online statutory search for: \"" + query + "\". Official legal documents can be verified under Legislative Department of India and IndiaCode gazettes.",
                            "https://www.indiacode.nic.in"
                        ));
                    }

                    callback.onOnlineResults(results);
                } else {
                    callback.onError("Server returned status " + responseCode);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error fetching online results", e);
                callback.onError(e.getLocalizedMessage());
            }
        }).start();
    }
}
