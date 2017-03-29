package ru.kulikovman.calcus;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String RESULT_FIELD = "result";
    public static final String CALCULATION_FIELD = "calculation";
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
                if (isZero()) toJoin(".1");
                else if (!isClosingBracket()) toJoin("1");
                break;
            case R.id.btn_2:
                if (isResult) removeResult();
                if (isZero()) toJoin(".2");
                else if (!isClosingBracket()) toJoin("2");
                break;
            case R.id.btn_3:
                if (isResult) removeResult();
                if (isZero()) toJoin(".6");
                else if (!isClosingBracket()) toJoin("3");
                break;
            case R.id.btn_4:
                if (isResult) removeResult();
                if (isZero()) toJoin(".4");
                else if (!isClosingBracket()) toJoin("4");
                break;
            case R.id.btn_5:
                if (isResult) removeResult();
                if (isZero()) toJoin(".5");
                else if (!isClosingBracket()) toJoin("5");
                break;
            case R.id.btn_6:
                if (isResult) removeResult();
                if (isZero()) toJoin(".6");
                else if (!isClosingBracket()) toJoin("6");
                break;
            case R.id.btn_7:
                if (isResult) removeResult();
                if (isZero()) toJoin(".7");
                else if (!isClosingBracket()) toJoin("7");
                break;
            case R.id.btn_8:
                if (isResult) removeResult();
                if (isZero()) toJoin(".8");
                else if (!isClosingBracket()) toJoin("8");
                break;
            case R.id.btn_9:
                if (isResult) removeResult();
                if (isZero()) toJoin(".9");
                else if (!isClosingBracket()) toJoin("9");
                break;
            case R.id.btn_0:
                if (isResult) removeResult();
                if (isZero()) toJoin(".0");
                else if (!isClosingBracket()) toJoin("0");
                break;
            case R.id.btn_000:
                if (isResult) removeResult();
                if (isZero()) toJoin(".000");
                else if (isNumber() || isDot()) toJoin("000");
                else if (!isClosingBracket()) toJoin("0.00");
                break;
            case R.id.btn_dot:
                if (isResult) removeResult();
                if (isNull()) toJoin("0.");
                else if (isNumber() && !getLastElement().contains(".")) toJoin(".");
                break;
            case R.id.btn_pi:
                if (isResult) removeResult();
                if (isNull() || isOperation()) toJoin("3.14159265359");
                break;
            case R.id.btn_e:
                if (isResult) removeResult();
                if (isNull() || isOperation()) toJoin("2.71828182846");
                break;

            //базовые операции
            case R.id.btn_reset:
                removeResult();
                break;
            case R.id.btn_delete:
                if (isResult) removeResult();
                if (getResult().length() > 1) {
                    if (isOperation()) removeLastOperation();
                    else removeLastSymbol();
                } else {
                    resultField.setText(" ");
                }
                break;
            case R.id.btn_addition:
                removeExcessElements();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" + ");
                    isResult = false;
                }
                break;
            case R.id.btn_subtraction:
                removeExcessElements();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" - ");
                    isResult = false;
                }
                break;
            case R.id.btn_multiplication:
                removeExcessElements();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" × ");
                    isResult = false;
                }
                break;
            case R.id.btn_devision:
                removeExcessElements();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" / ");
                    isResult = false;
                }
                break;
            case R.id.btn_power:
                removeExcessElements();
                if (isNumber() || isClosingBracket()) {
                    toJoin(" ^ ");
                    isResult = false;
                }
                break;

            //дополнительные операции
            case R.id.btn_percent:
                removeExcessElements();
                if (getNumberOfElements() > 1) {
                    calculationField.setText(String.valueOf(getResult() + "% ="));
                    resultField.setText("RESULT");
                    isResult = true;
                }
                break;
            case R.id.btn_square_root:
                removeExcessElements();
                if (getNumberOfElements() >= 1 && !isZero()) {
                    calculationField.setText(String.valueOf("√ (" + getResult() + ") ="));
                    resultField.setText("RESULT");
                    isResult = true;
                }
                break;
            case R.id.btn_sinus:
                removeExcessElements();
                if (getNumberOfElements() >= 1) {
                    calculationField.setText(String.valueOf("sin (" + getResult() + ") ="));
                    resultField.setText("RESULT");
                    isResult = true;
                }
                break;
            case R.id.btn_cosine:
                removeExcessElements();
                if (getNumberOfElements() >= 1) {
                    calculationField.setText(String.valueOf("cos (" + getResult() + ") ="));
                    resultField.setText("RESULT");
                    isResult = true;
                }
                break;
            case R.id.btn_tangent:
                removeExcessElements();
                if (getNumberOfElements() >= 1) {
                    calculationField.setText(String.valueOf("tan (" + getResult() + ") ="));
                    resultField.setText("RESULT");
                    isResult = true;
                }
                break;
            case R.id.btn_natural_logarithm:
                removeExcessElements();
                if (getNumberOfElements() >= 1) {
                    calculationField.setText(String.valueOf("ln (" + getResult() + ") ="));
                    resultField.setText("RESULT");
                    isResult = true;
                }
                break;
            case R.id.btn_logarithm:
                removeExcessElements();
                if (getNumberOfElements() >= 1) {
                    calculationField.setText(String.valueOf("log (" + getResult() + ") ="));
                    resultField.setText("RESULT");
                    isResult = true;
                }
                break;


            case R.id.btn_opening_bracket:

                break;
            case R.id.btn_closing_bracket:

                break;

            //математические операции


            //окончательный рассчет
            case R.id.resultField:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(RESULT_FIELD, getResult());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, R.string.copy_co_clipboard, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_calculate:
                removeExcessElements();
                if (getNumberOfElements() > 1) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    resultField.setText("RESULT");
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

    private void removeExcessElements() {
        if (getLastElement().contains(".")) {
            while (getLastSymbol().equals("0")) {
                removeLastSymbol();
            }
        }
        if (isDot()) removeLastSymbol();
        if (isOperation()) removeLastOperation();
    }

    private boolean isNull() {
        return getResult().equals("") || getResult().equals(" ");
    }

    private boolean isZero() {
        return getLastElement().equals("0");
    }

    private boolean isDot() {
        return getLastSymbol().equals(".");
    }

    private boolean isSpace() {
        return getLastSymbol().equals(" ");
    }

    private boolean isPercent() {
        return getLastSymbol().equals("%");
    }

    private boolean isNumber() {
        try {
            int number = Integer.parseInt(getLastSymbol());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean openingBracket() {
        return getLastSymbol().equals("(");
    }

    private boolean isClosingBracket() {
        return getLastSymbol().equals(")");
    }

    private boolean isOperation() {
        return getLastElement().equals("+") || getLastElement().equals("-")
                || getLastElement().equals("×") || getLastElement().equals("/")
                || getLastElement().equals("^");
    }

    private void toJoin(String s) {
        String temp = resultField.getText().toString() + s;
        resultField.setText(temp);
    }

    private void toCalculate(String s) {
        if (getNumberOfElements() > 1) {
            calculationField.setText(String.valueOf(getResult() + s));
            resultField.setText("RESULT");
            isResult = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RESULT_FIELD, resultField.getText().toString());
        outState.putString(CALCULATION_FIELD, calculationField.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resultField.setText(savedInstanceState.getString(RESULT_FIELD));
        calculationField.setText(savedInstanceState.getString(CALCULATION_FIELD));
    }
}
