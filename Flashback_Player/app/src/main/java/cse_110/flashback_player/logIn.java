package cse_110.flashback_player;

import android.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class logIn extends AppCompatActivity{
    int RC_SIGN_IN = 13;
    GoogleApiClient mGoogleApiClient;

    String clientId = "381331143314-05oag0m1g4uoevvl1bhbqd7eesn1hk7l.apps.googleusercontent.com";
    String clientSecret = "tXJnjS2W4Q0AJQCBl8A8GS-9";
    String code;
    HttpTransport httpTransport;
    JacksonFactory jsonFactory;
    GoogleTokenResponse tokenResponse;
    String redirectUrl;
    static User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestServerAuthCode(clientId,false)
                .requestScopes(new Scope( "https://www.googleapis.com/auth/contacts.readonly"))
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(logIn.this)
                //.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            updateUI(account);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void updateUI(GoogleSignInAccount account){
        user.setName(account.getGivenName()+account.getFamilyName());
        try {
            setUp();
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {//Can add more as per requirement

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
            try{Thread.sleep(5000);}
            catch (InterruptedException e){
                Log.e("exception", ".");
            }
            Intent refresh = new Intent(this, LibraryActivity.class);
            startActivity(refresh);
        }catch(IOException e) {
            System.exit(1);
        }
    }

    public void setUp() throws IOException {
        httpTransport = new NetHttpTransport();
        jsonFactory = JacksonFactory.getDefaultInstance();
        //JacksonFactory jsonFactory = new JacksonFactory()
        redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    SharedPreferences shared = getSharedPreferences("token", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = shared.getString("current", "x");
                    //System.out.println("json------- "+json.equals("x"));
                    // GoogleTokenResponse checktoken = gson.fromJson(json,GoogleTokenResponse.class);
                    GoogleCredential credential;
                    if(json.equals("x")) {
                        tokenResponse =
                                new GoogleAuthorizationCodeTokenRequest(
                                        httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                                        .execute();

                        SharedPreferences sharedLocation = getSharedPreferences("token", MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = sharedLocation.edit();
                        Gson gson2 = new Gson();
                        String json2 = tokenResponse.getAccessToken();
                        editor2.putString("current", json2);
                        editor2.commit();
                        GoogleTokenResponse checktoken =tokenResponse;
                        credential = new GoogleCredential.Builder()
                                .setTransport(httpTransport)
                                .setJsonFactory(jsonFactory)
                                .setClientSecrets(clientId, clientSecret)
                                .build()
                                .setFromTokenResponse(checktoken);
                    }
                    else{
                        credential = new GoogleCredential.Builder()
                                .setTransport(httpTransport)
                                .setJsonFactory(jsonFactory)
                                .setClientSecrets(clientId, clientSecret)
                                .build()
                                //.setRefreshToken(json);
                                .setAccessToken(json);
                        //credential.refreshToken();
                    }



                    PeopleService peopleService =
                            new PeopleService.Builder(httpTransport, jsonFactory, credential)
                                    .setApplicationName("FriendList")
                                    .build();

                    ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                            .setPersonFields("names")
                            .execute();

                    List<Person> connections = response.getConnections();

                    System.out.println("--------------yolo " + connections.size());
                    ArrayList<String> friends = new ArrayList<>();
                    for(int i = 0;i<connections.size();i++){
                        String firstName = (connections.get(i).getNames().get(0).getGivenName());
                        String lastName = (connections.get(i).getNames().get(0).getFamilyName());
                        System.out.println("friends --------- " + firstName);
                        friends.add(firstName+lastName);
                    }
                    user.setFriendsList(friends);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("nothing", "handleSignInResult:" + result.isSuccess());
        // Signed in successfolly, show authenticated UI.
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            code = acct.getServerAuthCode();
            updateUI(acct);
        }
    }

    public User getUser(){
        return user;
    }

}