package com.example.vanessa.rockpaperscissors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    TextView log;

    int userScore, computerScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        rock = findViewById(R.id.b_rock);
        paper = findViewById(R.id.b_paper);
        scissors = findViewById(R.id.b_scissors);
        exit = findViewById(R.id.exit_btn);
        log = findViewById(R.id.win_log);

        userChoice = findViewById(R.id.user_choice);
        computerChoice = findViewById(R.id.computer_choice);

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
                        backToHomePage();
                    }
                });

                adb.show();
            }
        });
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
