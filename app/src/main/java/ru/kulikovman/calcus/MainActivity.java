package ru.kulikovman.calcus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0,
            btn_addition, btn_subtraction, btn_multiplication, btn_devision,
            btn_dot, btn_reset, btn_delete, btn_calculate;
    private Button btn_000, btn_sinus, btn_cosine, btn_tangent, btn_power,
            btn_natural_logarithm, btn_logarithm, btn_percent, btn_pi, btn_e,
            btn_opening_bracket, btn_closing_bracket, btn_square_root;
    private TextView textResult, textCalculation;
    private int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAllItems();
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

    private void initAllItems() {
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_addition = (Button) findViewById(R.id.btn_addition);
        btn_subtraction = (Button) findViewById(R.id.btn_subtraction);
        btn_multiplication = (Button) findViewById(R.id.btn_multiplication);
        btn_devision = (Button) findViewById(R.id.btn_devision);
        btn_dot = (Button) findViewById(R.id.btn_dot);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_calculate = (Button) findViewById(R.id.btn_calculate);
        textResult = (TextView) findViewById(R.id.textResult);
        textCalculation = (TextView) findViewById(R.id.textCalculation);

        btn_000 = (Button) findViewById(R.id.btn_000);
        btn_sinus = (Button) findViewById(R.id.btn_sinus);
        btn_cosine = (Button) findViewById(R.id.btn_cosine);
        btn_tangent = (Button) findViewById(R.id.btn_tangent);
        btn_natural_logarithm = (Button) findViewById(R.id.btn_natural_logarithm);
        btn_logarithm = (Button) findViewById(R.id.btn_logarithm);
        btn_percent = (Button) findViewById(R.id.btn_percent);
        btn_pi = (Button) findViewById(R.id.btn_pi);
        btn_e = (Button) findViewById(R.id.btn_e);
        btn_power = (Button) findViewById(R.id.btn_power);
        btn_opening_bracket = (Button) findViewById(R.id.btn_opening_bracket);
        btn_closing_bracket = (Button) findViewById(R.id.btn_closing_bracket);
        btn_square_root = (Button) findViewById(R.id.btn_square_root);
    }
}
