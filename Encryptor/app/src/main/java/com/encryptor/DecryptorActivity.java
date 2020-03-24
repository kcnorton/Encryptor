package com.encryptor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DecryptorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText encryptionInput;
    private Button decryptButton;
    private Button encryptorActivityButton;
    private TextView decryptText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decryptor);

        encryptionInput = (EditText)findViewById(R.id.encryptionInput);
        decryptButton = (Button)findViewById(R.id.decryptButton);
        encryptorActivityButton = (Button)findViewById(R.id.encryptorActivityButton);
        decryptText = (TextView) findViewById(R.id.decryptText);

        //set onClickListener to encryptButton
        decryptButton.setOnClickListener(this);
        encryptorActivityButton.setOnClickListener(this);

    }

    //method for clicking decryptButton
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.decryptButton: {
                //initialize error flag
                boolean error = false;
                //check if encryptionInput is an empty string
                if (encryptionInput.getText().toString().trim().length() == 0) {
                    //error message
                    encryptionInput.setError("Missing Message");
                    error = true;
                } else {
                    int count = 0;
                    //if not, check if a letter is present
                    for (int i = 0; i < encryptionInput.length(); i++) {
                        char c = encryptionInput.getText().toString().charAt(i);
                        if (Character.isLetter(c)) {
                            //increment count
                            count++;
                        }
                    }
                    //check count
                    if (count < 1) {
                        //error message
                        encryptionInput.setError("Invalid Message");
                        error = true;
                    }
                }

                String message = encryptionInput.getText().toString();

                //if error is false -> call encrypt
                if (!error) {
                    //get alphabet string
                    String alphabetStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                    //decrypt input
                    decryptText.setText(decrypt(message, alphabetStr));
                }
                break;
            }
            case R.id.encryptorActivityButton: {
                Intent intent = new Intent(DecryptorActivity.this, EncryptorActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    //cracking encryption
    public int[] countLetters(String message, String alphStr){
        alphStr = alphStr.toLowerCase();
        int[] counts = new int[26];
        for(int k=0; k < message.length(); k++){
            int dex = alphStr.indexOf(Character.toLowerCase(message.charAt(k)));
            if (dex != -1){
                counts[dex] += 1;
            }
        }
        return counts;
    }

    public int maxIndex(int[] vals){
        int maxDex = 0;
        for(int k=0; k < vals.length; k++){
            if (vals[k] > vals[maxDex]){
                maxDex = k;
            }
        }
        return maxDex;
    }

    public int getKey(String encrypted, String alphStr){
        int[] freqs = countLetters(encrypted, alphStr);
        int maxDex = maxIndex(freqs);
        int mostCommonPos = 'e' - 'a';
        int dkey = maxDex - mostCommonPos;
        if (maxDex < mostCommonPos) {
            dkey = 26 - (mostCommonPos-maxDex);
        }
        return dkey;
    }

    public String getShiftedAlphabet(int key, String alphStr) {
        String shiftedAlphabet = alphStr.substring(key) +
                alphStr.substring(0,key);
        //alphStr = alphStr + alphStr.toLowerCase();
        shiftedAlphabet = shiftedAlphabet + shiftedAlphabet.toLowerCase();
        return shiftedAlphabet;
    }

    private char transformLetter(char c, String from, String to) {
        int idx = from.indexOf(c);
        if (idx != -1) {
            return to.charAt(idx);
        }
        return c;
    }

    private String transform(String input, String from, String to){
        StringBuilder sb = new StringBuilder(input);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            c = transformLetter(c, from, to);
            sb.setCharAt(i, c);
        }
        return sb.toString();
    }

    public String decrypt(String input, String alphStr) {
        int key = getKey(input, alphStr);
        String shiftedAlphabet = getShiftedAlphabet(key, alphStr);
        alphStr = alphStr + alphStr.toLowerCase();

        return transform(input, shiftedAlphabet, alphStr);
    }

}
