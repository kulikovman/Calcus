package ru.kulikovman.calcus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_0, btn_000,
            btn_addition, btn_subtraction, btn_multiplication, btn_devision,
            btn_dot, btn_reset, btn_delete, btn_calculate, btn_sinus, btn_cosine, btn_tangent,
            btn_natural_logarithm, btn_logarithm, btn_exclamation, btn_pi, btn_e, btn_circumflex,
            btn_opening_bracket, btn_closing_bracket, btn_square_root;

    private TextView textResult, textCalculation;
    private int temp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResult = (TextView) findViewById(R.id.textResult);

        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

        btn_reset.setHapticFeedbackEnabled(true);
        btn_reset.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reset) {
            temp++;
            textResult.setText(String.valueOf(temp));
        }
    }
}
