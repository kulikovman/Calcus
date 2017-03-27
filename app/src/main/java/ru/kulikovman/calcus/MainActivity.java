package ru.kulikovman.calcus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textResult, textCalculation;
    private int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initItems();
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

    private void initItems() {
        textResult = (TextView) findViewById(R.id.textResult);
        textResult.setText(R.string.textResult);
        textCalculation = (TextView) findViewById(R.id.textCalculation);
        textCalculation.setText(R.string.calculateExample);
    }
}
