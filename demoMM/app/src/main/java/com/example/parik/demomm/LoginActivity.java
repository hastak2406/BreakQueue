package com.example.parik.demomm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    EditText uname,pward;
    Button lbtn,spbtn,fpbtn;

    String FName = "";
    ProgressDialog pd;
    private HttpCallActivity httpCall;

    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname = findViewById(R.id.unbtn);
        pward = findViewById(R.id.pwdbtn);
        lbtn = findViewById(R.id.loginbtn);
        spbtn = findViewById(R.id.signupbtn);
        fpbtn = findViewById(R.id.forgotbtn);

        httpCall = new HttpCallActivity();

        lbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLogin(v);
            }
        });

        spbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });

        fpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        pd = new ProgressDialog(LoginActivity.this,R.style.MyTheme);
        pd.setCancelable(false);
        pd.setMessage("Please Wait...");
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    public void validateLogin(final View v){

        final String emailid = uname.getText().toString();
        String password = pward.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(uname.getText().toString());


        // Check for both field is empty or not
        if (emailid.equals("") || emailid.length() == 0
                || password.equals("") || password.length() == 0) {
            Snackbar.make(v, "Enter both credentials.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        // Check if email id is valid or not
        else if (!m.find()) {
            Snackbar.make(v, "Your Email Id is Invalid.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (password.length() <=8 ) {
            Snackbar.make(v, "Your Password is to short or Empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else {
            pd.show();

            String data = null;
            try {
                data = "email_id" + "=" + URLEncoder.encode(emailid.toLowerCase(), "utf-8");
                data += "&" + "password" + "=" + URLEncoder.encode(password.toLowerCase(), "utf-8");
                data += "&" + "action" + "=" + "chklogin";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            final String response = httpCall.callServices(data);
            final String email = emailid.toString();
            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                public void run() {

                    if (response.equals("") || response == null) {
                        pd.dismiss();
                        Snackbar.make(v, "Server Connection Failed !!!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    int jsonResult = returnParsedJsonObject(response);

                    if (jsonResult == 0) {
                        pd.dismiss();
                        Snackbar.make(v, "Invalid Email & Password.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                    if (jsonResult == 1) {
                        Snackbar.make(v, "Success - Welcome, " + FName , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        Thread t = new Thread(new Runnable() {
                            public void run() {
                                Intent ie = new Intent(LoginActivity.this, WelcomeActivity.class);
                                ie.addCategory(Intent.CATEGORY_HOME);
                                ie.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(ie);
                                finish();

                                emptyText();
                            }
                        });
                        t.start();
                    }
                }
            }, 2000);

        }
    }

    public void emptyText() {
        uname.setText("");
        pward.setText("");
    }

    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        int returnedResult = 0;
        JSONArray lstUser = null;
        try {

            lstUser = new JSONArray(result);

            for(int i=0;i<lstUser.length();i++){
                JSONObject c = lstUser.getJSONObject(i);
                String firstname = c.getString("Name");
                returnedResult = c.getInt("success");
                FName = firstname;
            }

        } catch (JSONException e) {
            try {
                resultObject = new JSONObject(result);
                returnedResult = resultObject.getInt("success");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            e.printStackTrace();
        }
        return returnedResult;
    }
}
