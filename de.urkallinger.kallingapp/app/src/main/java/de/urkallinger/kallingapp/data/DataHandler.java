package de.urkallinger.kallingapp.data;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import de.urkallinger.kallingapp.datastructure.Motion;
import de.urkallinger.kallingapp.datastructure.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static de.urkallinger.kallingapp.TestFragment.JSON;

public class DataHandler {

    private static DataHandler instance = null;

    private static final String BASE_URL = "http://188.102.243.65:8080/kallingapp/";



    private OkHttpClient client = new OkHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    private DataHandler () {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static DataHandler getInstance() {
        if(instance == null) {
            instance = new DataHandler();
        }

        return instance;
    }

    public void getMotions(final Callback callback) {
        post("motions/getMotions", "", callback);
    }

    public void getUsers(final KallingCallback<List<User>> kcb) {

        post("getUsers", "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                kcb.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    List<User> userList = Arrays.asList(mapper.readValue(resp, User[].class));
                    kcb.onSuccess(userList);
                }
            }
        });
    }

    public void getUser(String id, final KallingCallback<User> kcb) {
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject();
            jsonObj.put("id", id);
        } catch (JSONException e) {
            kcb.onFailure(e);
        }

        post("getUser", jsonObj.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                kcb.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    User user = mapper.readValue(resp, User.class);
                    kcb.onSuccess(user);
                }
            }
        });
    }

    public void login(String username, String password, final KallingCallback<Boolean> kcb) {
        JSONObject jsonObj = null;

        try {
            jsonObj = new JSONObject();
            jsonObj.put("username", username);
            jsonObj.put("password", password);
        } catch (JSONException e) {
            kcb.onFailure(e);
        }

        post("login", jsonObj.toString(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                kcb.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    try {
                        JSONObject json = new JSONObject(resp);
                        kcb.onSuccess(Boolean.parseBoolean(json.get("success").toString()));
                    } catch (JSONException e) {
                        kcb.onSuccess(false);
                    }
                }
            }
        });
    }

    private Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

}
