package com.example.vanessa.rockpaperscissors;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import static com.example.vanessa.rockpaperscissors.GameOptions.ROCK;
import static com.example.vanessa.rockpaperscissors.GameOptions.PAPER;
import static com.example.vanessa.rockpaperscissors.GameOptions.SCISSORS;
import static com.example.vanessa.rockpaperscissors.SharedPreference.*;

public class GamePage extends AppCompatActivity {

    final String _ROCK = "ROCK";
    final String _PAPER = "PAPER";
    final String _SCISSORS = "SCISSORS";

    Button rock, paper, scissors, exit;
    ImageView userChoice, computerChoice;
    TextView log, showUserName;

    int userScore, computerScore;

    // created a variable for checking the request code state
    private static final int REQUEST_CODE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        rock = findViewById(R.id.b_rock);
        paper = findViewById(R.id.b_paper);
        scissors = findViewById(R.id.b_scissors);
        exit = findViewById(R.id.exit_btn);
        log = findViewById(R.id.win_log);

        String user = getUsername();
        showUserName = findViewById(R.id.show_user_name);
        showUserName.setText(user + " choice");

        userChoice = findViewById(R.id.user_choice);
        computerChoice = findViewById(R.id.computer_choice);

        // External storage
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GamePage.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // If external storage write permission is not granted.
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(GamePage.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);

        rock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice.setImageResource(R.drawable.you_rock);
                String logMessage = computerChoice(ROCK.name());
                log.setText(logMessage);
            }
        });

        paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice.setImageResource(R.drawable.you_paper);
                String logMessage = computerChoice(PAPER.name());
                log.setText(logMessage);
            }
        });

        scissors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userChoice.setImageResource(R.drawable.you_scis);
                String logMessage = computerChoice(SCISSORS.name());
                log.setText(logMessage);
            }
        });

        exit.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(GamePage.this);
                String user = getUsername();
                adb.setTitle(user + " score: " + userScore + "\n" + "Computer score: " + computerScore);
                String message = (computerScore > userScore) ? "YOU LOSE" : "YOU WIN";
                adb.setMessage(message);

                adb.setPositiveButton("Back to game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                adb.setNegativeButton("Finish game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onSave();
                        backToHomePage();
                    }
                });

                adb.setNeutralButton("Scores", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GamePage.this);
                        alertDialog.setTitle("Previous games with scores");

                        // showing /storage/emulated/0/DCIM folder files
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            File externalDirectory = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DCIM)));
                            String data = ShowDirectoryFiles(externalDirectory);
                            alertDialog.setMessage(data);
                        }
                        alertDialog.show();
                    }
                });

                adb.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You granted write external storage permission"
                        , Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getApplicationContext(), "You denied write external storage permission"
                        , Toast.LENGTH_LONG).show();
        }
    }

    // method for showing the save directory file listing, so it would be easier to know what the file names are
    public String ShowDirectoryFiles(File externalDirectory) {
        StringBuilder storageData = new StringBuilder();
        File listFile[] = externalDirectory.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    ShowDirectoryFiles(listFile[i]);
                } else storageData.append(listFile[i].getName()).append("\n");
            }
        } else {
            storageData.append("No data to show");
        }
        return storageData.toString();
    }

    // method for checking if the external storage is writable
    private boolean isExternalStorageWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "Writable");
            return true;
        } else return false;
    }

    // method for saving the entered text message under a specific file name
    public void onSave() {
        if (isExternalStorageWritable()) {
            // Save file to /storage/emulated/0/DCIM folder
            String userData = "NAME: " + getUsername() + " SCORE: " + userScore;
            File textFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), userData);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(textFile);
                fileOutputStream.write(userData.getBytes());
                fileOutputStream.close();

                Toast.makeText(this, "File saved", Toast.LENGTH_LONG).show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else Toast.makeText(this, "External storage isn't mounted", Toast.LENGTH_LONG).show();
    }

    private String computerChoice(String userOption) {
        String[] options = {ROCK.name(), PAPER.name(), SCISSORS.name()};
        Random r = new Random();
        int index = r.nextInt(3);
        String compDesc = options[index];

        switch (compDesc) {
            case _ROCK:
                computerChoice.setImageResource(R.drawable.comp_rock);
                break;
            case _PAPER:
                computerChoice.setImageResource(R.drawable.comp_paper);
                break;
            case _SCISSORS:
                computerChoice.setImageResource(R.drawable.comp_scis);
                break;
        }

        if (userOption.equals(compDesc)) {
            return "TIE. Nobody win.";
        } else if (userOption.equals(ROCK.name()) && compDesc.equals(SCISSORS.name())) {
            userScore++;
            return "Rock crushes Scissors. YOU WIN!";
        } else if (userOption.equals(ROCK.name()) && compDesc.equals(PAPER.name())) {
            computerScore++;
            return "Paper covers Rock. COMPUTER WINS!";
        } else if (userOption.equals(PAPER.name()) && compDesc.equals(SCISSORS.name())) {
            computerScore++;
            return "Scissors cuts Paper. COMPUTER WINS!";
        } else if (userOption.equals(SCISSORS.name()) && compDesc.equals(ROCK.name())) {
            computerScore++;
            return "Rock crushes Scissors. COMPUTER WINS!";
        } else if (userOption.equals(SCISSORS.name()) && compDesc.equals(PAPER.name())) {
            userScore++;
            return "Scissors cuts Paper. YOU WIN!";
        } else if (userOption.equals(PAPER.name()) && compDesc.equals(ROCK.name())) {
            userScore++;
            return "Paper covers Rock. YOU WIN!";
        } else return "Not Sure";
    }

    private String getUsername() {
        SharedPreferences usernameData = getSharedPreferences(TAG.name(), Context.MODE_PRIVATE);
        return usernameData.getString(TAG_NAME.name(), "");
    }

    private void backToHomePage() {
        Intent homePage = new Intent(GamePage.this, MainActivity.class);
        startActivity(homePage);
    }
}
