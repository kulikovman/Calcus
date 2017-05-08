package ru.kulikovman.calcus;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView numberField, historyField, memoryField, operationField,
            memoryMarker, historyOperation;

    private boolean isResult = false;
    private boolean isPercentCalculation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberField = (TextView) findViewById(R.id.numberField);
        historyField = (TextView) findViewById(R.id.historyField);
        memoryField = (TextView) findViewById(R.id.memoryField);
        operationField = (TextView) findViewById(R.id.operationField);

        memoryMarker = (TextView) findViewById(R.id.memoryMarker);
        historyOperation = (TextView) findViewById(R.id.historyOperation);
    }

    public void onClick(View view) {
        view.setHapticFeedbackEnabled(true);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        switch (view.getId()) {
            case R.id.btn_1:
                addNumber("1");
                break;
            case R.id.btn_2:
                addNumber("2");
                break;
            case R.id.btn_3:
                addNumber("3");
                break;
            case R.id.btn_4:
                addNumber("4");
                break;
            case R.id.btn_5:
                addNumber("5");
                break;
            case R.id.btn_6:
                addNumber("6");
                break;
            case R.id.btn_7:
                addNumber("7");
                break;
            case R.id.btn_8:
                addNumber("8");
                break;
            case R.id.btn_9:
                addNumber("9");
                break;


            case R.id.btn_0:
                if (isResult || isError()) clearCalculation();
                if (!isEmpty(operationField)) moveToHistory();
                if (getCountNumbers(numberField) < 11) {
                    if (getNumberField().equals("0")) {
                        numberField.setText("0.0");
                    } else addText("0");
                }
                break;
            case R.id.btn_dot:
                if (isResult || isError()) clearCalculation();
                if (!isEmpty(operationField)) moveToHistory();

                if (isEmpty(numberField)) addText("0.");
                else if (!getNumberField().contains(".") && getCountNumbers(numberField) < 11)
                    addText(".");
                break;


            case R.id.btn_reset:
                clearCalculationWithAnimation();
                break;
            case R.id.btn_delete:
                if (isResult || isError()) clearCalculationWithAnimation();
                else {
                    if (!isEmpty(operationField)) {
                        operationField.setText(" ");
                    } else {
                        int length = numberField.getText().toString().trim().length();
                        int minus = numberField.getText().toString().trim().replaceAll("\\d", "").replace(".", "").length();

                        if (length > 1 + minus) {
                            String s = numberField.getText().toString().trim();
                            numberField.setText(s.substring(0, s.length() - 1));
                        } else numberField.setText(" ");
                    }
                }
                break;


            case R.id.btn_addition:
                setOperation("+");
                break;
            case R.id.btn_subtraction:
                setOperation("-");
                break;
            case R.id.btn_multiplication:
                setOperation("×");
                break;
            case R.id.btn_division:
                setOperation("÷");
                break;


            case R.id.btn_percent:
                if (!isResult && isEmpty(operationField)
                        && !isEmpty(numberField) && !isEmpty(historyField)
                        && isNumber(numberField) && isNumber(historyField)) {
                    removeExcessSymbol(numberField);
                    isPercentCalculation = true;
                    toCalculate();
                }
                break;


            case R.id.btn_MR:
                if (!isEmpty(numberField) && !isError()) {
                    removeExcessSymbol(numberField);
                    memoryField.setText(getNumberField());
                    memoryMarker.setText(R.string.circle);
                }
                break;
            case R.id.btn_MC:
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.dissolution);
                animation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        memoryField.setText(" ");
                        memoryMarker.setText(" ");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                memoryField.startAnimation(animation);
                memoryMarker.startAnimation(animation);
                break;
            case R.id.btn_M_addition:
                if (!isEmpty(numberField) && !isError()) {
                    removeExcessSymbol(numberField);

                    double first, second;
                    if (isEmpty(memoryField)) first = 0.0;
                    else first = Double.parseDouble(memoryField.getText().toString().trim());
                    second = Double.parseDouble(numberField.getText().toString().trim());

                    String result = roundResult(first + second, 11);

                    memoryField.setText(result);
                    memoryMarker.setText(R.string.circle);
                }
                break;
            case R.id.btn_M_subtraction:
                if (!isEmpty(numberField) && !isError()) {
                    removeExcessSymbol(numberField);

                    double first, second;
                    if (isEmpty(memoryField)) first = 0.0;
                    else first = Double.parseDouble(memoryField.getText().toString().trim());
                    second = Double.parseDouble(numberField.getText().toString().trim());

                    String result = roundResult(first - second, 11);

                    memoryField.setText(result);
                    memoryMarker.setText(R.string.circle);
                }
                break;


            case R.id.numberField:
                if (!isEmpty(numberField)) {
                    ClipData clipNumber = ClipData.newPlainText("numberField", getNumberField());
                    clipboard.setPrimaryClip(clipNumber);
                    Toast.makeText(this, R.string.copy_co_clipboard, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.memoryField:
                if (!isEmpty(memoryField)) {
                    ClipData clipMemory = ClipData.newPlainText("memoryField", memoryField.getText().toString());
                    clipboard.setPrimaryClip(clipMemory);
                    Toast.makeText(this, R.string.copy_co_clipboard, Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.btn_calculate:
                toCalculate();
                break;
        }
    }

    private void setOperation(String operation) {
        if (!isError()) {
            removeExcessSymbol(numberField);
            if (!isEmpty(numberField)) {
                toCalculate();
                isResult = false;
                operationField.setText(operation);
            }
        }
    }

    private void addNumber(String s) {
        if (isResult || isError()) clearCalculation();
        if (!isEmpty(operationField)) moveToHistory();

        if (getCountNumbers(numberField) < 11) {
            if (getNumberField().equals("0")) {
                String temp = "0." + s;
                numberField.setText(temp);
            } else {
                String temp = getNumberField() + s;
                numberField.setText(temp);
            }
        }
    }

    private void moveToHistory() {
        historyField.setText(getNumberField());
        historyOperation.setText(operationField.getText().toString());
        numberField.setText(" ");
        operationField.setText(" ");
    }

    private boolean isEmpty(TextView textView) {
        String s = textView.getText().toString();
        return s.equals("") || s.equals(" ");
    }

    private void addText(String s) {
        String temp = numberField.getText().toString() + s;
        numberField.setText(temp);
    }

    private boolean isNumber(TextView textView) {
        try {
            double number = Double.parseDouble(textView.getText().toString().trim());
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private boolean isError() {
        return getNumberField().contains("Error") || getNumberField().contains("Too long result")
                || getNumberField().contains("Infinity");
    }

    private int getCountNumbers(TextView textView) {
        return textView.getText().toString().replaceAll("\\D", "").length();
    }

    private void clearCalculation() {
        numberField.setText(" ");
        operationField.setText(" ");
        historyField.setText(" ");
        historyOperation.setText(" ");

        isPercentCalculation = false;
        isResult = false;
    }

    private void clearCalculationWithAnimation() {
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.dissolution);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                numberField.setText(" ");
                operationField.setText(" ");
                historyField.setText(" ");
                historyOperation.setText(" ");

                isPercentCalculation = false;
                isResult = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        numberField.startAnimation(animation);
        operationField.startAnimation(animation);
        historyField.startAnimation(animation);
        historyOperation.startAnimation(animation);
    }

    private void removeExcessSymbol(TextView textView) {
        String s = textView.getText().toString().trim();
        if (s.contains(".")) {
            while (s.endsWith("0")) {
                s = s.substring(0, s.length() - 1);
            }
            if (s.endsWith(".")) {
                s = s.substring(0, s.length() - 1);
            }
            textView.setText(s);
        }
    }

    private String getNumberField() {
        return numberField.getText().toString().trim();
    }

    public String roundResult(double value, int places) {
        if (value == 0) return "0";

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        String result = bd.toPlainString();

        int numbers = result.replaceAll("\\D", "").length();
        int minus = result.replaceAll("\\d", "").replace(".", "").length();
        int dot = result.replaceAll("\\d", "").replace("-", "").length();

        if (dot == 1) {
            int dotPosition = result.indexOf(".");

            if (dotPosition <= 10 + minus) {
                result = result.substring(0, 12 + minus);

                while (result.endsWith("0")) {
                    result = result.substring(0, result.length() - 1);
                }
                if (result.endsWith(".")) {
                    result = result.substring(0, result.length() - 1);
                }
                if (!result.equals("0")) {
                    return result;
                }
            }
            if (dotPosition == 11 + minus) {
                result = result.substring(0, 11 + minus);
                return result;
            }

            return "Too long result";

        } else if (numbers <= 11) return result;
        else return "Too long result";
    }

    private void toCalculate() {
        if (!isEmpty(numberField) && !isEmpty(historyField) && isNumber(numberField) && isNumber(historyField)) {
            removeExcessSymbol(numberField);
            String result = "Error";

            String operation = historyOperation.getText().toString().trim();
            String firstNumber = historyField.getText().toString().trim();
            String secondNumber = numberField.getText().toString().trim();

            double first = Double.parseDouble(firstNumber);
            double second = Double.parseDouble(secondNumber);

            String historyResult = firstNumber + " " + operation + " " + secondNumber;
            if (isPercentCalculation) historyResult += "%";
            historyField.setText(historyResult);
            historyOperation.setText("=");

            if (isPercentCalculation) {
                switch (operation) {
                    case "+":
                        result = roundResult(first + (first / 100 * second), 11);
                        break;
                    case "-":
                        result = roundResult(first - (first / 100 * second), 11);
                        break;
                    case "×":
                        if (first != 0 && second != 0) {
                            result = roundResult(first * (first / 100 * second), 11);
                        } else result = "0";
                        break;
                    case "÷":
                        if (first != 0 && second != 0) {
                            result = roundResult(first / (first / 100 * second), 11);
                        } else if (first == 0 && second != 0) result = "0";
                        else result = "Infinity";
                        break;
                }
            } else {
                switch (operation) {
                    case "+":
                        result = roundResult(first + second, 11);
                        break;
                    case "-":
                        result = roundResult(first - second, 11);
                        break;
                    case "×":
                        if (first != 0 && second != 0) {
                            result = roundResult(first * second, 11);
                        } else result = "0";
                        break;
                    case "÷":
                        if (first != 0 && second != 0) {
                            result = roundResult(first / second, 11);
                        } else if (first == 0 && second != 0) result = "0";
                        else result = "Infinity";
                        break;
                }
            }

            numberField.setText(result);
            isPercentCalculation = false;
            isResult = true;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("numberField", numberField.getText().toString());
        outState.putString("operationField", operationField.getText().toString());
        outState.putString("historyField", historyField.getText().toString());
        outState.putString("historyOperation", historyOperation.getText().toString());
        outState.putString("memoryField", memoryField.getText().toString());
        outState.putString("memoryMarker", memoryMarker.getText().toString());

        outState.putBoolean("isResult", isResult);
        outState.putBoolean("isPercentCalculation", isPercentCalculation);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberField.setText(savedInstanceState.getString("numberField"));
        operationField.setText(savedInstanceState.getString("operationField"));
        historyField.setText(savedInstanceState.getString("historyField"));
        historyOperation.setText(savedInstanceState.getString("historyOperation"));
        memoryField.setText(savedInstanceState.getString("memoryField"));
        memoryMarker.setText(savedInstanceState.getString("memoryMarker"));

        isResult = savedInstanceState.getBoolean("isResult");
        isPercentCalculation = savedInstanceState.getBoolean("isPercentCalculation");
    }
}
