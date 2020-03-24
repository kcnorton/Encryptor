package com.encryptor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
import android.widget.AdapterView.*;
import android.content.Intent;

public class EncryptorActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText messageInput;
    private EditText shiftNumber;
    private Spinner targetAlphabet;
    private Button encryptButton;
    private TextView cipherText;

    private Button decryptActivityButton;

    String alphabet = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryptor);

        messageInput = (EditText)findViewById(R.id.messageInput);
        shiftNumber = (EditText)findViewById(R.id.shiftNumber);
        targetAlphabet = (Spinner)findViewById(R.id.targetAlphabet);
        encryptButton = (Button)findViewById(R.id.encryptButton);
        cipherText = (TextView) findViewById(R.id.cipherText);

        decryptActivityButton = (Button)findViewById(R.id.decryptActivityButton);

        //set onClickListener to encryptButton
        encryptButton.setOnClickListener(this);

        decryptActivityButton.setOnClickListener(this);

        //shiftNumber default is 0
        shiftNumber.setText("0");

        //targetAlphabet default is Normal
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.alphabets, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        targetAlphabet.setAdapter(adapter);

        targetAlphabet.setSelection(0);

        targetAlphabet.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        alphabet = parentView.getItemAtPosition(0).toString();
                        break;
                    case 1:
                        alphabet = parentView.getItemAtPosition(1).toString();
                        break;
                    case 2:
                        alphabet = parentView.getItemAtPosition(2).toString();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });
    }

    //method for clicking encryptor and navigate to decryptor
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.encryptButton: {
                //initialize error flag
                boolean error = false;
                //check if messageInput is an empty string
                if (messageInput.getText().toString().trim().length() == 0) {
                    //error message
                    messageInput.setError("Missing Message");
                    error = true;
                } else {
                    int count = 0;
                    //if not, check if a letter is present
                    for (int i = 0; i < messageInput.length(); i++) {
                        char c = messageInput.getText().toString().charAt(i);
                        if (Character.isLetter(c)) {
                            //increment count
                            count++;
                        }
                    }
                    //check count
                    if (count < 1) {
                        //error message
                        messageInput.setError("Invalid Message");
                        error = true;
                    }
                }

                String n = shiftNumber.getText().toString();
                //check if n is blank
                if (n.trim().isEmpty()) {
                    n = "0";
                }
                //check if shiftNumber is numeric
                if (android.text.TextUtils.isDigitsOnly(shiftNumber.getText())) {
                    int intN = Integer.parseInt(n);
                    //check if it is between 1 and 26
                    if ((1 > intN) || (intN >= 26)) {
                        //error message
                        shiftNumber.setError("Invalid Shift Number");
                        error = true;
                    }
                } else {
                    shiftNumber.setError("Invalid Shift Number");
                    error = true;
                }


                String message = messageInput.getText().toString();
                String number = shiftNumber.getText().toString();

                //if error is false -> call encrypt
                if (!error) {
                    //get alphabet string
                    String alphabetStr = alphabetStr(alphabet);
                    //get shifted alphabet
                    String shiftedAlphabet = shiftAlphabet(number, alphabetStr);
                    alphabetStr = alphabetStr + alphabetStr.toLowerCase();
                    cipherText.setText(encrypt(message, shiftedAlphabet));
                }
                break;
            }
            case R.id.decryptActivityButton: {
                Intent intent = new Intent(EncryptorActivity.this, DecryptorActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    //method to encrypt a letter of the message
    public char transformLetter(char c, String from, String to) {
        int idx = from.indexOf(c);
        if (idx != -1) {
            return to.charAt(idx);
        }
        return c;
    }

    //method for building an encrypted message, calls transformLetter
    public String transform(String message, String from, String to){
        StringBuilder sb = new StringBuilder(message);
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            c = transformLetter(c, from, to);
            sb.setCharAt(i, c);
        }
        return sb.toString();
    }

    //method to encrypt message, called method transform
    public String encrypt(String message, String shiftedAlphabet) {
        String original = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        original = original + original.toLowerCase();
        return transform(message, original, shiftedAlphabet);
    }

    //method for shifting alphabet
    public String shiftAlphabet(String number, String alphabet){
        //
        int num = Integer.parseInt(number);
        String shiftedAlphabet = alphabet.substring(num) + alphabet.substring(0,num);
        shiftedAlphabet = shiftedAlphabet + shiftedAlphabet.toLowerCase();
        return shiftedAlphabet;
    }

    //method for getting alphabet string from targetAlphabet
    public String alphabetStr(String alphabet){
            String alphabetStr = "";
            if(alphabet.equals("Normal")) {
                alphabetStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            } else if(alphabet.equals("Reverse")) {
                alphabetStr = "ZYXWVUTSRQPONMLKJIHGFEDCBA";
            } else if(alphabet.equals("PASDFG")){
                alphabetStr = "PASDFGHJKLZXCVBNMQWERTYUIO";
            }
            return alphabetStr;
    }

}
