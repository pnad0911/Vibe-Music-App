package cse_110.flashback_player;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class logIn extends AppCompatActivity{
    int RC_SIGN_IN = 13;
    GoogleApiClient mGoogleApiClient;

    String clientId = "381331143314-o3f86fnls6l787276v9rghph4eat6p8v.apps.googleusercontent.com";
    String clientSecret = "_BAEhqmE5xmcGVtqNljvgWCB";
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

        user.setFirstName(account.getGivenName());
        user.setLastName(account.getFamilyName());
        try {
            setUp();
        }catch(IOException e) {
            System.exit(1);
        }
    }

    public void setUp() throws IOException {
        httpTransport = new NetHttpTransport();
        jsonFactory = JacksonFactory.getDefaultInstance();
        //JacksonFactory jsonFactory = new JacksonFactory();

        redirectUrl = "urn:ietf:wg:oauth:2.0:oob";

        System.out.println("yolo " + code);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    tokenResponse =
                            new GoogleAuthorizationCodeTokenRequest(
                                    httpTransport, jsonFactory, clientId, clientSecret, code, redirectUrl)
                                    .execute();

                    GoogleCredential credential = new GoogleCredential.Builder()
                            .setTransport(httpTransport)
                            .setJsonFactory(jsonFactory)
                            .setClientSecrets(clientId, clientSecret)
                            .build()
                            .setFromTokenResponse(tokenResponse);

                    PeopleService peopleService =
                            new PeopleService.Builder(httpTransport, jsonFactory, credential)
                                    .setApplicationName("FriendList")
                                    .build();

                    ListConnectionsResponse response = peopleService.people().connections().list("people/me")
                            .setPersonFields("names")
                            .execute();

                    List<Person> connections = response.getConnections();
                    ArrayList<Friend> friends = new ArrayList<Friend>();
                    for(int i = 0;i<connections.size();i++){
                        Friend friend = new Friend();
                        friend.setFirstName(connections.get(i).getNames().get(0).getGivenName());
                        friend.setLastName(connections.get(i).getNames().get(0).getFamilyName());
                        friends.add(friend);
                    }

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
        if (result.isSuccess()) {
            // Signed in successfolly, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            code = acct.getServerAuthCode();
            System.out.println("codeeeeee" + code);
            System.out.println("name" + acct.getDisplayName());

            updateUI(acct);
        }
    }

}
