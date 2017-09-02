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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView mNumberField, mHistoryField, mMemoryField, mOperationField, mMemoryMarker, mHistoryOperation;

    private boolean mIsResult = false;
    private boolean mIsPercentCalculation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberField = (TextView) findViewById(R.id.number_field);
        mOperationField = (TextView) findViewById(R.id.operation_field);
        mHistoryField = (TextView) findViewById(R.id.history_field);
        mHistoryOperation = (TextView) findViewById(R.id.history_operation);
        mMemoryField = (TextView) findViewById(R.id.memory_field);
        mMemoryMarker = (TextView) findViewById(R.id.memory_marker);
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
                if (mIsResult || isError()) clearCalculation();
                if (!isEmpty(mOperationField)) moveToHistory();
                if (getCountNumbers(mNumberField) < 11) {
                    if (getNumberField().equals("0")) {
                        mNumberField.setText("0.0");
                    } else addText("0");
                }
                break;
            case R.id.btn_dot:
                if (mIsResult || isError()) clearCalculation();
                if (!isEmpty(mOperationField)) moveToHistory();

                if (isEmpty(mNumberField)) addText("0.");
                else if (!getNumberField().contains(".") && getCountNumbers(mNumberField) < 11)
                    addText(".");
                break;


            case R.id.btn_reset:
                clearCalculation();
                break;
            case R.id.btn_delete:
                if (mIsResult || isError()) clearCalculation();
                else {
                    if (!isEmpty(mOperationField)) {
                        mOperationField.setText(" ");
                    } else {
                        int length = mNumberField.getText().toString().trim().length();
                        int minus = mNumberField.getText().toString().trim().replaceAll("\\d", "").replace(".", "").length();

                        if (length > 1 + minus) {
                            String s = mNumberField.getText().toString().trim();
                            mNumberField.setText(s.substring(0, s.length() - 1));
                        } else mNumberField.setText(" ");
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
                if (!mIsResult && isEmpty(mOperationField)
                        && !isEmpty(mNumberField) && !isEmpty(mHistoryField)
                        && isNumber(mNumberField) && isNumber(mHistoryField)) {
                    removeExcessSymbol(mNumberField);
                    mIsPercentCalculation = true;
                    toCalculate();
                }
                break;


            case R.id.btn_MR:
                if (mIsResult || isError()) clearCalculation();
                if (!isEmpty(mOperationField)) moveToHistory();
                mNumberField.setText(mMemoryField.getText().toString());
                break;
            case R.id.btn_MC:
                mMemoryField.setText(" ");
                mMemoryMarker.setText(" ");
                break;
            case R.id.btn_M_addition:
                if (!isEmpty(mNumberField) && !isError()) {
                    removeExcessSymbol(mNumberField);

                    double first, second;
                    if (isEmpty(mMemoryField)) first = 0.0;
                    else first = Double.parseDouble(mMemoryField.getText().toString().trim());
                    second = Double.parseDouble(mNumberField.getText().toString().trim());

                    String result = roundResult(first + second);

                    mMemoryField.setText(result);
                    mMemoryMarker.setText(R.string.red_dot);
                }
                break;
            case R.id.btn_M_subtraction:
                if (!isEmpty(mNumberField) && !isError()) {
                    removeExcessSymbol(mNumberField);

                    double first, second;
                    if (isEmpty(mMemoryField)) first = 0.0;
                    else first = Double.parseDouble(mMemoryField.getText().toString().trim());
                    second = Double.parseDouble(mNumberField.getText().toString().trim());

                    String result = roundResult(first - second);

                    mMemoryField.setText(result);
                    mMemoryMarker.setText(R.string.red_dot);
                }
                break;


            case R.id.number_field:
                if (!isEmpty(mNumberField)) {
                    ClipData clipNumber = ClipData.newPlainText("mNumberField", getNumberField());
                    clipboard.setPrimaryClip(clipNumber);
                    Toast.makeText(this, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.memory_field:
                if (!isEmpty(mMemoryField)) {
                    ClipData clipMemory = ClipData.newPlainText("mMemoryField", mMemoryField.getText().toString());
                    clipboard.setPrimaryClip(clipMemory);
                    Toast.makeText(this, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.btn_calculate:
                toCalculate();
                break;
        }
    }

    private void setOperation(String operation) {
        if (!isError()) {
            removeExcessSymbol(mNumberField);
            if (!isEmpty(mNumberField)) {
                toCalculate();
                mIsResult = false;
                mOperationField.setText(operation);
            }
        }
    }

    private void addNumber(String s) {
        if (mIsResult || isError()) clearCalculation();
        if (!isEmpty(mOperationField)) moveToHistory();

        if (getCountNumbers(mNumberField) < 11) {
            if (getNumberField().equals("0")) {
                String temp = "0." + s;
                mNumberField.setText(temp);
            } else {
                String temp = getNumberField() + s;
                mNumberField.setText(temp);
            }
        }
    }

    private void moveToHistory() {
        mHistoryField.setText(getNumberField());
        mHistoryOperation.setText(mOperationField.getText().toString());
        mNumberField.setText(" ");
        mOperationField.setText(" ");
    }

    private boolean isEmpty(TextView textView) {
        String s = textView.getText().toString();
        return s.equals("") || s.equals(" ");
    }

    private void addText(String s) {
        String temp = mNumberField.getText().toString() + s;
        mNumberField.setText(temp);
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
        mNumberField.setText(" ");
        mOperationField.setText(" ");
        mHistoryField.setText(" ");
        mHistoryOperation.setText(" ");

        mIsPercentCalculation = false;
        mIsResult = false;
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
        return mNumberField.getText().toString().trim();
    }


    // Получение результата операции
    // Результат округляется и обрабатывается в этом методе
    public String roundResult(double value) {
        if (value == 0) return "0";

        BigDecimal bd = new BigDecimal(value);
        String subResult = bd.toPlainString();

        int minus = 0;
        if (subResult.startsWith("-")) minus = 1;
        int dotPosition = subResult.indexOf(".");

        if (dotPosition > 0 && dotPosition <= 11 + minus) {
            int countNumbers = subResult.substring(0, dotPosition).length() - minus;
            int round = 11 - countNumbers;
            bd = bd.setScale(round, RoundingMode.HALF_UP);
            return String.valueOf(bd.doubleValue());
        } else {
            int numbers = subResult.replaceAll("\\D", "").length();

            if (numbers <= 11) return subResult;
            else return "Too long result";
        }
    }

    private void toCalculate() {
        if (!isEmpty(mNumberField) && !isEmpty(mHistoryField) && isNumber(mNumberField) && isNumber(mHistoryField)) {
            removeExcessSymbol(mNumberField);
            String result = "Error";

            String operation = mHistoryOperation.getText().toString().trim();
            String firstNumber = mHistoryField.getText().toString().trim();
            String secondNumber = mNumberField.getText().toString().trim();

            double first = Double.parseDouble(firstNumber);
            double second = Double.parseDouble(secondNumber);

            String historyResult = firstNumber + " " + operation + " " + secondNumber;
            if (mIsPercentCalculation) historyResult += "%";
            mHistoryField.setText(historyResult);
            mHistoryOperation.setText("=");

            if (mIsPercentCalculation) {
                switch (operation) {
                    case "+":
                        result = roundResult(first + (first / 100 * second));
                        break;
                    case "-":
                        result = roundResult(first - (first / 100 * second));
                        break;
                    case "×":
                        if (first != 0 && second != 0) {
                            result = roundResult(first * (first / 100 * second));
                        } else result = "0";
                        break;
                    case "÷":
                        if (first != 0 && second != 0) {
                            result = roundResult(first / (first / 100 * second));
                        } else if (first == 0 && second != 0) result = "0";
                        else result = "Infinity";
                        break;
                }
            } else {
                switch (operation) {
                    case "+":
                        result = roundResult(first + second);
                        break;
                    case "-":
                        result = roundResult(first - second);
                        break;
                    case "×":
                        if (first != 0 && second != 0) {
                            result = roundResult(first * second);
                        } else result = "0";
                        break;
                    case "÷":
                        if (first != 0 && second != 0) {
                            result = roundResult(first / second);
                        } else if (first == 0 && second != 0) result = "0";
                        else result = "Infinity";
                        break;
                }
            }

            mNumberField.setText(result);
            mIsPercentCalculation = false;
            mIsResult = true;
        }
    }
}
