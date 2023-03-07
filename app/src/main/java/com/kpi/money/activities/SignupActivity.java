package com.kpi.money.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;
import com.kpi.money.utils.Helper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.OptionalPendingResult;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;
import static com.kpi.money.Config.ENABLE_EMAIL_LOGIN;
import static com.kpi.money.Config.ENABLE_FACEBOOK_LOGIN;
import static com.kpi.money.Config.ENABLE_GMAIL_LOGIN;

public class SignupActivity extends ActivityBase implements GoogleApiClient.OnConnectionFailedListener{

    EditText signupUsername, signupFullname, signupPassword, signupEmail;
    private static final int RC_SIGN_IN = 777;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    private String username, fullname, password, email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        signupUsername = findViewById(R.id.signupUsername);
        signupFullname = findViewById(R.id.signupFullname);
        signupPassword = findViewById(R.id.signupPassword);
        signupEmail = findViewById(R.id.signupEmail);

        CardView signupJoinBtn = findViewById(R.id.signupJoinBtn);

        if(!ENABLE_EMAIL_LOGIN) {

            signupUsername.setVisibility(View.GONE);
            signupFullname.setVisibility(View.GONE);
            signupPassword.setVisibility(View.GONE);
            signupEmail.setVisibility(View.GONE);
            signupJoinBtn.setVisibility(View.GONE);
        }

        App.getInstance().getCountryCode();

        signupJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = signupUsername.getText().toString();
                fullname = signupFullname.getText().toString();
                password = signupPassword.getText().toString();
                email = signupEmail.getText().toString();

                if (verifyRegForm()) {

                    signUp(fullname,email,username, password, "Manual");

                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


/*
        CardView btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        if(!ENABLE_GMAIL_LOGIN) {

            btnSignIn.setVisibility(View.GONE);

        }

        // Customizing Google button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        */
/*
        // facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton mLoginButton = findViewById(R.id.login_button);
        if(!ENABLE_FACEBOOK_LOGIN){
            mLoginButton.setVisibility(View.GONE);
        }

        mLoginButton.setReadPermissions(Arrays.asList(EMAIL));

        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject,GraphResponse response) {
                                try {

                                    String personName = jsonObject.getString("name");
                                    String email =  jsonObject.getString("email");
                                    //String id =  jsonObject.getString("id");
                                    String user = getUsername(email);

                                    signUp(personName,email,user, user, "Facebook");

                                } catch(JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

                setResult(RESULT_CANCELED);
                //AppUtils.toastShort(SignupActivity.this,"user cancelled");
            }

            @Override
            public void onError(FacebookException e) {

                // Handle exception
                AppUtils.toastShort(SignupActivity.this,"some error : " + e);

            }
        });
        */
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in success.
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            if(email != null){
                String user = getUsername(email);
                signUp(personName,email,user, user, "Google");
            }



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {

            // is Logged in Cache
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

        } else {

            // is Logged in Expired
            showpDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hidepDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An error has occurred in Google APIs
        if(DEBUG_MODE){Log.d(TAG, "onConnectionFailed:" + connectionResult);}
    }

    String getUsername(String email){

        String emailtext = email.substring(0,email.lastIndexOf("@"));

        if(emailtext.contains(".")){
            username = emailtext.replace(".", "");
        }else{
            username = emailtext;
        }

        return username;
    }

    void signUp(final String name, final String email, final String username, final String password, final String regtype){

        if (App.getInstance().isConnected()) {

            showpDialog();

            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_SIGNUP, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            if (App.getInstance().authorize(response)) {

                                Intent i = new Intent(SignupActivity.this, AccountActvity.class);
                                startActivity(i);

                                ActivityCompat.finishAffinity(SignupActivity.this);

                            } else if(App.getInstance().getErrorCode() == 699 || App.getInstance().getErrorCode() == 999){

                                Dialogs.validationError(SignupActivity.this,App.getInstance().getErrorCode());

                            } else {

                                switch (App.getInstance().getErrorCode()) {

                                    case ERROR_LOGIN_TAKEN :

                                        Dialogs.warningDialog(SignupActivity.this, getResources().getString(R.string.error_login_taken), getResources().getString(R.string.error_device_exists_description), true, false, "", getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                finish();
                                            }
                                        });

                                        break;

                                    case ERROR_EMAIL_TAKEN :

                                        Dialogs.warningDialog(SignupActivity.this, getResources().getString(R.string.error_email_taken), getResources().getString(R.string.error_device_exists_description), true, false, "", getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        });

                                        break;

                                    case ERROR_IP_TAKEN :

                                        Dialogs.warningDialog(SignupActivity.this,getResources().getString(R.string.error_device_exists),getResources().getString(R.string.error_device_exists_description),true,false,"",getResources().getString(R.string.ok),null);

                                        break;

                                    default:

                                        if(DEBUG_MODE){
                                            Log.e("Profile", "Could not parse malformed JSON: \"" + response.toString() + "\"");
                                        }else{
                                            Dialogs.serverError(SignupActivity.this,getResources().getString(R.string.ok), null);
                                        }

                                        break;
                                }
                            }

                            hidepDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Dialogs.warningDialog(SignupActivity.this,"", getResources().getString(R.string.error_data_loading),false,false,"", getResources().getString(R.string.ok), null);

                    if(DEBUG_MODE)
                    {Log.e("Profile", "Malformed JSON: " + error.getMessage());}

                    hidepDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", username);
                    params.put("fullname", name);
                    params.put("password", password);
                    params.put("email", email);
                    params.put("reg", regtype);
                    params.put("clientId", CLIENT_ID);

                    return params;
                }
            };

            App.getInstance().addToRequestQueue(jsonReq);

        } else {

            Dialogs.warningDialog(SignupActivity.this,"", getResources().getString(R.string.msg_network_error),false,false,"", getResources().getString(R.string.ok), null);

        }
    }

    public Boolean verifyRegForm() {

        signupUsername.setError(null);
        signupFullname.setError(null);
        signupPassword.setError(null);
        signupEmail.setError(null);

        Helper helper = new Helper();

        if (username.length() == 0) {

            signupUsername.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (username.length() < 5) {

            signupUsername.setError(getString(R.string.error_small_username));

            return false;
        }

        if (!helper.isValidLogin(username)) {

            signupUsername.setError(getString(R.string.error_wrong_format));

            return false;
        }

        if (fullname.length() == 0) {

            signupFullname.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() == 0) {

            signupPassword.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() < 6) {

            signupPassword.setError(getString(R.string.error_small_password));

            return false;
        }

        if (!helper.isValidPassword(password)) {

            signupPassword.setError(getString(R.string.error_wrong_format));

            return false;
        }

        if (email.length() == 0) {

            signupEmail.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (!helper.isValidEmail(email)) {

            signupEmail.setError(getString(R.string.error_wrong_format));

            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed(){

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }
}
