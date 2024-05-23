package org.libreapps.projetqcm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private Quizz randomQuiz;
    private TextView questionTextView;
    private Button reponse1Button, reponse2Button, reponse3Button, reponse4Button;
    private ArrayList<Quizz> quizzList = new ArrayList<>();

    private int counter = 30;
    private TextView countdownText;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        countdownText = findViewById(R.id.countdownText);
        handler.post(runnable);

        reponse1Button = findViewById(R.id.reponse1);
        reponse2Button = findViewById(R.id.reponse2);
        reponse3Button = findViewById(R.id.reponse3);
        reponse4Button = findViewById(R.id.reponse4);

        questionTextView = findViewById(R.id.question);

        quizzList = Param.getInstance().getListQizz();

        displayRandomQuestion();

        reponse1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAnswer("choixA");
            }
        });

        reponse2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAnswer("choixB");
            }
        });

        reponse3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAnswer("choixC");
            }
        });

        reponse4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAnswer("choixD");
            }
        });
    }

    private void displayRandomQuestion() {
        if (!quizzList.isEmpty()) {
            if (quizzList.size() == 3) {
                Intent intent = new Intent(QuestionActivity.this, BienJouerActivity.class);
                startActivity(intent);
                finish(); // Pour fermer cette activité et empêcher l'utilisateur de revenir en arrière
            } else {
                Random random = new Random();
                int randomIndex = random.nextInt(quizzList.size());
                randomQuiz = quizzList.get(randomIndex);

                questionTextView.setText(randomQuiz.getQuestion());
                reponse1Button.setText(randomQuiz.getChoixA());
                reponse2Button.setText(randomQuiz.getChoixB());
                reponse3Button.setText(randomQuiz.getChoixC());
                reponse4Button.setText(randomQuiz.getChoixD());

                // Remove the question from the list
                quizzList.remove(randomIndex);
            }
        } else {
            // If all questions have been asked, reset the list
            quizzList = Param.getInstance().getListQizz();
        }
    }

    private void verifyAnswer(String choix) {
        Intent intent;
        if (randomQuiz.getReponse().equals(choix)) {
            intent = new Intent(QuestionActivity.this, BonneReponseActivity.class);
        } else {
            intent = new Intent(QuestionActivity.this, MauvaiseReponseActivity.class);
        }
        startActivityForResult(intent, 1);
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (counter > 0) {
                countdownText.setText(String.valueOf(counter));
                counter--;
                handler.postDelayed(this, 1000);
            } else {
                countdownText.setText("Terminé!");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || requestCode == 2) {
            if (resultCode == RESULT_OK) {
                // If the user answered correctly or incorrectly, display the next question
                displayRandomQuestion();
            }
        }
    }
}
