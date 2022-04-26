package com.example.cryptomaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    EditText keyText,normalText,encryptedText;
    Button normalDelete,normalCopy,encryptedDelete,encryptedCopy,encrypt,decrypt;
    TextView title,encryptTitle,charCount1,charCount2;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#36A4FB"));
        }

        context = MainActivity.this;

        normalText = findViewById(R.id.normalText);
        keyText = findViewById(R.id.key);
        encryptedText = findViewById(R.id.encryptedText);
       normalCopy = findViewById(R.id.copy_normal);
        encryptedCopy = findViewById(R.id.copy_encrypted);
        normalDelete  = findViewById(R.id.delete_normal);
        encryptedDelete = findViewById(R.id.delete_encrypted);
        decrypt = findViewById(R.id.decrypted);
        encrypt = findViewById(R.id.encrypted);
        charCount1 = findViewById(R.id.char_count);
        charCount2 = findViewById(R.id.char_count2);

        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (normalText.getText().toString().matches("") || keyText.getText().toString().matches("")){
                    App.ToastMaker(context,"Enter Text and Your key");
                }else if (keyText.getText().toString().length()!=8){
                    App.ToastMaker(context,"Please Enter 8 characters Key");
                }
                else {
                    encryptedText.setText(encrypt(normalText.getText().toString(),context));
                }
            }
        });


        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (encryptedText.getText().toString().matches("") || keyText.getText().toString().matches("")){
                    App.ToastMaker(context,"Enter Encrypted Text and Your key");
                }else if (keyText.getText().toString().length()!=8){
                    App.ToastMaker(context,"Please Enter 8 characters Key");
                }
                else {
                    normalText.setText(decrypt(encryptedText.getText().toString(),context));
                }
            }
        });

        normalCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("encrypted text",normalText.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                App.ToastMaker(context," Input text copied successfully");
            }
        });

        encryptedCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("encrypted text",encryptedText.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                App.ToastMaker(context," Encrypted text copied successfully");
            }
        });

        normalDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normalText.setText("");
            }
        });

        encryptedDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encryptedText.setText("");
            }
        });

        normalText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                charCount1.setText(normalText.getText().toString().length()+"");

            }
        });

        encryptedText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                charCount2.setText(encryptedText.getText().toString().length()+"");

            }
        });


    }



    public  String decrypt(String value, Context context){
        String coded;
        String result = null;
        if (value.startsWith("code==")){
            coded = value.substring(6,value.length()).trim();

        }else {
            coded = value.trim();
        }

        try {
            byte[] bytesDecoded = Base64.decode(coded.getBytes("UTF-8"),Base64.DEFAULT);
            SecretKeySpec keySpec = new SecretKeySpec(keyText.getText().toString().getBytes(),"DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/ZeroBytePadding");
            cipher.init(Cipher.DECRYPT_MODE,keySpec);
            byte[] textDecrypted = cipher.doFinal(bytesDecoded);
            result = new String(textDecrypted);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (Exception e){
            e.printStackTrace(); App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        return result;
    }




    public  String encrypt(String value, Context context){
        String crypted = "";
        try {
            byte[] clearText = value.getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(keyText.getText().toString().getBytes(),"DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/ZeroBytePadding");
            cipher.init(Cipher.ENCRYPT_MODE,keySpec);
            crypted = Base64.encodeToString(cipher.doFinal(clearText),Base64.DEFAULT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }
        catch (Exception e){
            e.printStackTrace(); App.DialogMaker(context,"Encrypt Error","Error found"+"\n"+e.getMessage());
            return "Encrypt Error";
        }

        return  crypted;
    }
}