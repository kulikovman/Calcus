package ru.kulikovman.calcus;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView numberField, historyField, memoryField, operationField,
            memoryMarker, historyOperation;

    private boolean isResult = false;
    private boolean isOperation = false;
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

        switch (view.getId()) {
            case R.id.btn_1:
                toAddNumber("1");
                break;
            case R.id.btn_2:
                toAddNumber("2");
                break;
            case R.id.btn_3:
                toAddNumber("3");
                break;
            case R.id.btn_4:
                toAddNumber("4");
                break;
            case R.id.btn_5:
                toAddNumber("5");
                break;
            case R.id.btn_6:
                toAddNumber("6");
                break;
            case R.id.btn_7:
                toAddNumber("7");
                break;
            case R.id.btn_8:
                toAddNumber("8");
                break;
            case R.id.btn_9:
                toAddNumber("9");
                break;


            case R.id.btn_0:
                if (isError()) clearCalculation();
                if (!isEmpty(operationField)) moveToHistory();
                if (isEmpty(numberField)) toAdd("0.");
                else if (getLength(numberField) < 13) toAdd("0");
                break;
            case R.id.btn_dot:
                if (isError()) clearCalculation();
                if (!isEmpty(operationField)) moveToHistory();
                if (isEmpty(numberField)) toAdd("0.");
                else if (!getNumber().contains(".") && getLength(numberField) < 12) toAdd(".");
                break;


            case R.id.btn_reset:
                clearCalculation();
                break;
            case R.id.btn_delete:
                if (isResult || isError()) clearCalculation();
                if (!isEmpty(operationField)) {
                    operationField.setText(" ");
                } else if (getLength(numberField) > 1) removeLastSymbol();
                else numberField.setText(" ");
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
                    removeExcessSymbolOfNumberField();
                    isPercentCalculation = true;
                    toCalculate();
                }
                break;


            case R.id.btn_MR:
                if (!isEmpty(numberField)) {
                    removeExcessSymbolOfNumberField();
                    memoryField.setText(getNumber());
                    memoryMarker.setText(R.string.circle);
                }
                break;
            case R.id.btn_MC:
                memoryField.setText(" ");
                memoryMarker.setText(" ");
                break;
            case R.id.btn_M_addition:
                if (!isEmpty(numberField)) {
                    removeExcessSymbolOfNumberField();
                    double first, second;

                    if (!isEmpty(memoryField)) {
                        first = Double.parseDouble(memoryField.getText().toString());
                    } else first = 0.0;
                    second = Double.parseDouble(numberField.getText().toString());

                    String result = String.valueOf(toRound(first + second, 11));

                    result = correctionResult(result);
                    memoryField.setText(result);
                    memoryMarker.setText(R.string.circle);
                }
                break;
            case R.id.btn_M_subtraction:
                if (!isEmpty(numberField)) {
                    removeExcessSymbolOfNumberField();
                    double first, second;

                    if (!isEmpty(memoryField)) {
                        first = Double.parseDouble(memoryField.getText().toString());
                    } else first = 0.0;
                    second = Double.parseDouble(numberField.getText().toString());

                    String result = String.valueOf(toRound(first - second, 11));

                    result = correctionResult(result);
                    memoryField.setText(result);
                    memoryMarker.setText(R.string.circle);
                }
                break;


            case R.id.numberField:
                ClipboardManager clipboardNumber = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipNumber = ClipData.newPlainText("numberField", getNumber());
                clipboardNumber.setPrimaryClip(clipNumber);

                Toast.makeText(this, R.string.copy_co_clipboard, Toast.LENGTH_SHORT).show();
                break;
            case R.id.memoryField:
                ClipboardManager clipboardMemory = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipMemory = ClipData.newPlainText("memoryField", memoryField.getText().toString());
                clipboardMemory.setPrimaryClip(clipMemory);

                Toast.makeText(this, R.string.copy_co_clipboard, Toast.LENGTH_SHORT).show();
                break;


            case R.id.btn_calculate:
                toCalculate();
                break;
        }
    }

    private String correctionResult(String result) {
        //меняем название ошибки
        if (result.equals("NaN")) {
            result = "Not a number";
        }

        //сокращаем длину результата
        if (result.length() > 12) {
            if (result.toLowerCase().contains("e")) {
                result = "Too long result";
            } else {
                result = result.substring(0, 12);
            }
        }

        //избавляем результат от лишних нулей
        if (result.contains(".")) {
            while (result.endsWith("0")) {
                result = result.substring(0, result.length() - 1);
            }
            if (result.endsWith(".")) {
                result = result.substring(0, result.length() - 1);
            }
        }

        return result;
    }

    private void setOperation(String operation) {
        removeExcessSymbolOfNumberField();
        if (!isEmpty(numberField)) {
            toCalculate();
            isResult = false;
            operationField.setText(operation);
        }
    }

    private void toAddNumber(String s) {
        if (isResult || isError()) {
            clearCalculation();
            isResult = false;
        }
        if (!isEmpty(operationField)) moveToHistory();
        if (getLength(numberField) < 12) {
            String temp = getNumber() + s;
            numberField.setText(temp);
        }
    }

    private void moveToHistory() {
        historyField.setText(getNumber());
        historyOperation.setText(operationField.getText().toString());
        numberField.setText(" ");
        operationField.setText(" ");
        isOperation = false;
    }

    private boolean isEmpty(TextView textView) {
        String s = textView.getText().toString();
        return s.equals("") || s.equals(" ");
    }

    private void toAdd(String s) {
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
        return getNumber().contains("Error") || getNumber().contains("Too long result")
                || getNumber().contains("Not a number");
    }

    private int getLength(TextView textView) {
        return textView.getText().toString().length();
    }

    private void removeLastSymbol() {
        numberField.setText(getNumber().substring(0, getNumber().length() - 1));
    }

    private void clearCalculation() {
        numberField.setText(" ");
        operationField.setText(" ");
        historyField.setText(" ");
        historyOperation.setText(" ");
        isResult = false;
        isOperation = false;
    }

    private void removeExcessSymbolOfNumberField() {
        if (getNumber().contains(".")) {
            while (getNumber().endsWith("0")) {
                removeLastSymbol();
            }
        }
        if (getNumber().endsWith(".")) removeLastSymbol();
    }


    //получение данных
    private String getNumber() {
        return numberField.getText().toString().trim();
    }

    public static double toRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private void toCalculate() {
        if (!isEmpty(numberField) && !isEmpty(historyField) && isNumber(numberField) && isNumber(historyField)) {
            removeExcessSymbolOfNumberField();
            String result = "Error";

            String operation = historyOperation.getText().toString();
            String firstNumber = historyField.getText().toString();
            String secondNumber = numberField.getText().toString();

            double first = Double.parseDouble(firstNumber);
            double second = Double.parseDouble(secondNumber);

            String historyResult = firstNumber + " " + operation + " " + secondNumber;
            if (isPercentCalculation) historyResult += "%";
            historyField.setText(historyResult);
            historyOperation.setText("=");

            if (isPercentCalculation) {
                switch (operation) {
                    case "+":
                        result = String.valueOf(toRound(first + (first / 100 * second), 11));
                        break;
                    case "-":
                        result = String.valueOf(toRound(first - (first / 100 * second), 11));
                        break;
                    case "×":
                        result = String.valueOf(toRound(first * (first / 100 * second), 11));
                        break;
                    case "÷":
                        result = String.valueOf(toRound(first / (first / 100 * second), 11));
                        break;
                }
            } else {
                switch (operation) {
                    case "+":
                        result = String.valueOf(toRound(first + second, 11));
                        break;
                    case "-":
                        result = String.valueOf(toRound(first - second, 11));
                        break;
                    case "×":
                        result = String.valueOf(toRound(first * second, 11));
                        break;
                    case "÷":
                        result = String.valueOf(toRound(first / second, 11));
                        break;
                }
            }

            result = correctionResult(result);

            numberField.setText(result);
            isPercentCalculation = false;
            isResult = true;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("numberField", numberField.getText().toString());
        outState.putString("historyField", historyField.getText().toString());
        outState.putBoolean("isResult", isResult);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        numberField.setText(savedInstanceState.getString("numberField"));
        historyField.setText(savedInstanceState.getString("historyField"));
        isResult = savedInstanceState.getBoolean("isResult");
    }
}
