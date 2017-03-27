package ru.kulikovman.calcus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TEXT_RESULT = "result";
    public static final String TEXT_CALCULATION = "text_Calculation";
    private TextView textResult, textCalculation;
    private int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResult = (TextView) findViewById(R.id.textResult);
        textCalculation = (TextView) findViewById(R.id.textCalculation);
    }

    public void onClick(View view) {
        view.setHapticFeedbackEnabled(true);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        switch (view.getId()) {
            case R.id.btn_1:
                temp += 1;
                textResult.setText(String.valueOf(temp));
                break;
            case R.id.btn_2:
                temp += 2;
                textResult.setText(String.valueOf(temp));
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_RESULT, textResult.getText().toString());
        outState.putString(TEXT_CALCULATION, textCalculation.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textResult.setText(savedInstanceState.getString(TEXT_RESULT));
        textCalculation.setText(savedInstanceState.getString(TEXT_CALCULATION));
    }
}
