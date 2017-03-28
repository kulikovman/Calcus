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
            //базовые кнопки
            case R.id.btn_1:

                break;
            case R.id.btn_2:

                break;
            case R.id.btn_3:

                break;
            case R.id.btn_4:

                break;
            case R.id.btn_5:

                break;
            case R.id.btn_6:

                break;
            case R.id.btn_7:

                break;
            case R.id.btn_8:

                break;
            case R.id.btn_9:

                break;
            case R.id.btn_0:

                break;
            case R.id.btn_000:

                break;
            case R.id.btn_dot:

                break;

            //базовые операции
            case R.id.btn_reset:

                break;
            case R.id.btn_delete:

                break;
            case R.id.btn_addition:

                break;
            case R.id.btn_subtraction:

                break;
            case R.id.btn_multiplication:

                break;
            case R.id.btn_devision:

                break;

            //дополнительные операции
            case R.id.btn_percent:

                break;
            case R.id.btn_power:

                break;
            case R.id.btn_square_root:

                break;
            case R.id.btn_opening_bracket:

                break;
            case R.id.btn_closing_bracket:

                break;

            //математические операции
            case R.id.btn_sinus:

                break;
            case R.id.btn_cosine:

                break;
            case R.id.btn_tangent:

                break;
            case R.id.btn_natural_logarithm:

                break;
            case R.id.btn_logarithm:

                break;
            case R.id.btn_pi:

                break;
            case R.id.btn_e:

                break;

            //окончательный рассчет
            case R.id.btn_calculate:

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
