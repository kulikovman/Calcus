package ru.kulikovman.calcus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String TEXT_RESULT = "result";
    public static final String TEXT_CALCULATION = "text_Calculation";
    private boolean isResult = false;
    private TextView resultField, calculationField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = (TextView) findViewById(R.id.resultField);
        calculationField = (TextView) findViewById(R.id.calculationField);
    }

    public void onClick(View view) {
        view.setHapticFeedbackEnabled(true);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        switch (view.getId()) {
            //базовые кнопки
            case R.id.btn_1:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("1");
                break;
            case R.id.btn_2:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("2");
                break;
            case R.id.btn_3:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("3");
                break;
            case R.id.btn_4:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("4");
                break;
            case R.id.btn_5:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("5");
                break;
            case R.id.btn_6:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("6");
                break;
            case R.id.btn_7:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("7");
                break;
            case R.id.btn_8:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("8");
                break;
            case R.id.btn_9:
                if (isResult) removeResult();
                if (!isClosingBracket()) toJoin("9");
                break;
            case R.id.btn_0:
                if (isResult) removeResult();
                if (getResult().equals("0")) toJoin(".0");
                else if (!isPercent() && !isClosingBracket()) toJoin("0");
                break;
            case R.id.btn_000:
                if (isResult) removeResult();
                if (isNull()) toJoin("0.00");
                else if (getResult().equals("0")) toJoin(".000");
                else if (isNumber()) toJoin("000");
                break;
            case R.id.btn_dot:
                if (isResult) removeResult();
                if (isNull()) toJoin("0.");
                else if (isNumber() && !getLastElement().contains(".")) toJoin(".");
                break;

            //базовые операции
            case R.id.btn_reset:
                removeResult();
                break;
            case R.id.btn_delete:
                if (isResult) removeResult();
                if (getResult().length() > 1) {
                    if (isSpace()) removeLastOperation();
                    else removeLastSymbol();
                } else {
                    resultField.setText(" ");
                }
                break;
            case R.id.btn_addition:
                removeExcessZeroAfterDot();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" + ");
                    isResult = false;
                }
                break;
            case R.id.btn_subtraction:
                removeExcessZeroAfterDot();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" - ");
                    isResult = false;
                }
                break;
            case R.id.btn_multiplication:
                removeExcessZeroAfterDot();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" * ");
                    isResult = false;
                }
                break;
            case R.id.btn_devision:
                removeExcessZeroAfterDot();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" / ");
                    isResult = false;
                }
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
                if (getNumberOfElements() > 1) {
                    if (isOperation()) removeLastOperation();
                    if (isDot()) removeLastSymbol();
                    removeExcessZeroAfterDot();

                    String temp = getResult() + " =";
                    calculationField.setText(temp);
                    resultField.setText("125");
                    isResult = true;
                }
                break;

        }
    }

    private String getResult() {
        return resultField.getText().toString().trim();
    }

    private String getLastSymbol() {
        if (isNull()) return "";
        else {
            String temp = resultField.getText().toString();
            return temp.substring(temp.length() - 1);
        }
    }

    private String getLastElement() {
        String[] temp = getResult().split(" ");
        return temp[temp.length - 1];
    }

    // TODO: 28.03.2017 исправить
    private int getNumberOfElements() {
        String[] temp = getResult().split(" ");
        if (isOperation()) return temp.length - 1;
        else return temp.length;
    }

    private void removeLastSymbol() {
        String deleteLastOperation = getResult().substring(0, getResult().length() - 1);
        resultField.setText(deleteLastOperation);
    }

    private void removeLastOperation() {
        String deleteLastOperation = getResult().substring(0, getResult().length() - 2);
        resultField.setText(deleteLastOperation);
    }

    private void removeResult() {
        resultField.setText(" ");
        isResult = false;
    }

    private void removeExcessZeroAfterDot() {
        if (getLastElement().contains(".")){
            while (getLastSymbol().equals("0")) {
                removeLastSymbol();
            }
        }

        if (isDot()) removeLastSymbol();
    }

    private boolean isNull() {
        return getResult().equals("") || getResult().equals(" ");
    }

    private boolean isDot() {
        return getLastSymbol().equals(".");
    }

    private boolean isSpace() {
        return getLastSymbol().equals(" ");
    }

    private boolean isNumber() {
        try {
            int number = Integer.parseInt(getLastSymbol());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPercent() {
        return getLastSymbol().equals("%");
    }

    private boolean isClosingBracket() {
        return getLastSymbol().equals(")");
    }

    private boolean isOperation() {
        return getLastElement().equals("+") || getLastElement().equals("-")
                || getLastElement().equals("*") || getLastElement().equals("/")
                || getLastElement().equals("^");
    }

    private void toJoin(String s) {
        String temp = resultField.getText().toString() + s;
        resultField.setText(temp);
    }














    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TEXT_RESULT, resultField.getText().toString());
        outState.putString(TEXT_CALCULATION, calculationField.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resultField.setText(savedInstanceState.getString(TEXT_RESULT));
        calculationField.setText(savedInstanceState.getString(TEXT_CALCULATION));
    }
}
