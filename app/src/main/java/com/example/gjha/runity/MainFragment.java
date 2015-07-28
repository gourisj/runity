package com.example.gjha.runity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    private Profile profile;
    private View view;
    private TextView mTextDetails;
    private CallbackManager mCallbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private AccessToken accessToken;
private FacebookCallback mFacebookCallback;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Runity", "onSuccess");
                accessToken = loginResult.getAccessToken();

             // String name = profile.getName();
                Intent intent = new Intent(getActivity(), MainActivity2Activity.class);
                getActivity().startActivity(intent);

            }

            @Override
            public void onCancel() {
                Log.d("Runity", "onCancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Runity", "onError " + e);
                String item = "Unable to Log you into Facebook as of now. Please proceed by Clicking on ELEVATE LIFE Button";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getActivity() , item , duration);
                toast.show();
            }

        });


        final AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                accessToken = newToken;

            }

        };
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                constructWelcomeMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
     /*   if (profile==null){
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
            profile = Profile.getCurrentProfile();
        }*/

     //   mTextDetails.setText(constructWelcomeMessage(profile));
    }

    @Override
    public void onStop() {
        super.onStop();
       // accessTokenTracker.stopTracking();
       // profileTracker.stopTracking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton mButtonLogin = (LoginButton) view.findViewById(R.id.login_button);
        mButtonLogin.setFragment(this);
        mButtonLogin.setReadPermissions("user_friends");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }

    public void proceedAnyway (View view) {
        Intent intent = new Intent(getActivity(), MainActivity2Activity.class);
        getActivity().startActivity(intent);
    }

}
