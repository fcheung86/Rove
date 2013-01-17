package com.fourkins.rove.activity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.fourkins.rove.R;
import com.fourkins.rove.application.AppPreferences;
import com.fourkins.rove.application.Rove;
import com.fourkins.rove.users.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Activity which displays a login screen to the user. (Code is from SDK template)
 */
public class LoginActivity extends Activity {
    /**
     * A dummy authentication store containing known user names and passwords. TODO: remove after connecting to a real
     * authentication system.
     */
    private static final Logger LOGGER = Logger.getLogger(LoginActivity.class.getName());

    private static final String[] DUMMY_CREDENTIALS = new String[] { "mark.sk.ho@gmail.com:password:Mark", "iceheat710@gmail.com:password:Justin",
            "fcheung86@gmail.com:password:Farran", "pthieu@gmail.com:faggot:Phong" };
    private AppPreferences mAppPrefs;
    private String userName = "";
    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mAppPrefs = new AppPreferences(getApplicationContext());

        // Set up the login form.
        mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mEmail);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form. If there are form errors (invalid email,
     * missing fields, etc.), the errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // DEMO OVERRIDE
        if (mEmail.equals("demo")) {
            cancel = false;
            // Reset errors.
            mEmailView.setError(null);
            mPasswordView.setError(null);
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private String encryptedPassword;
        private AsyncHttpClient client;
        private boolean authenticated = false;
        private String username_tmp;

        @Override
        protected Boolean doInBackground(Void... params) {
            // DEMO OVERRIDE
            if (mEmail.equals("demo") && mPassword.equals("demo")) {
                userName = "demo";
                return true;
            }
            client = new AsyncHttpClient();

            LOGGER.log(Level.INFO, "Verifying User");

            try {
                // Get "salt" for given user
                client.get(Rove.SERVER_BASE_URL + "/users?email=" + mEmail, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(String response) {
                        // this is the async callback
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            User user = new User(jsonObject);
                            username_tmp = user.getUsername();
                            encryptedPassword = user.getHash(mPassword, user.getSalt());
                            LOGGER.log(Level.INFO, "Username:" + user.getUsername());
                            LOGGER.log(Level.INFO, "Hashed Password:" + encryptedPassword);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (encryptedPassword == null) {
                            return;
                        }
                        // Verify password from server
                        client.get(Rove.SERVER_BASE_URL + "/users/verify?email=" + mEmail + "&password=" + encryptedPassword,
                                new AsyncHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(String response) {
                                        // this is the async callback
                                        LOGGER.log(Level.INFO, response);
                                        authenticated = Boolean.parseBoolean(response);
                                        userName = username_tmp;
                                    }

                                });

                    }

                });

                // Simulate network access. ****Remove when accessing real server****
                Thread.sleep(500);
            } catch (InterruptedException e) {
                return false;
            }

            /*
             * for (String credential : DUMMY_CREDENTIALS) { String[] pieces = credential.split(":"); if
             * (pieces[0].equals(mEmail)) { // Account exists, return true if the password matches. userName =
             * pieces[2]; return pieces[1].equals(mPassword); } }
             */

            return authenticated;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            // If login successful, set AppPrefernce "user", so user is permanently logged in.
            if (success) {
                mAppPrefs.saveUser(userName);
                Intent mainIntent = new Intent(LoginActivity.this, com.fourkins.rove.activity.MainActivity.class);
                finish();
                startActivity(mainIntent);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void registerUser(View view) {
        Intent newUserIntent = new Intent(this, NewUserActivity.class);
        startActivity(newUserIntent);
    }
}
