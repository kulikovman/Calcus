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

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    private TextView mNumberField, mHistoryField, mMemoryField, mOperationField, mMemoryMarker, mHistoryOperation;
    private ClipboardManager mClipboardManager;

    private boolean mIsResult, mIsPercent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализируем необходимые вью элементы
        mNumberField = (TextView) findViewById(R.id.number_field);
        mOperationField = (TextView) findViewById(R.id.operation_field);
        mHistoryField = (TextView) findViewById(R.id.history_field);
        mHistoryOperation = (TextView) findViewById(R.id.history_operation);
        mMemoryField = (TextView) findViewById(R.id.memory_field);
        mMemoryMarker = (TextView) findViewById(R.id.memory_marker);

        // Получаем буфер обмена
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        // Слушатель для долгих нажатий
        mNumberField.setOnLongClickListener(this);
        mMemoryField.setOnLongClickListener(this);
    }

    // Обработка длинных нажатий на цифровые поля
    @Override
    public boolean onLongClick(View view) {
        // Подключаем виброотклик
        hapticFeedbackOn(view);

        switch (view.getId()) {
            case R.id.number_field:
                // Копируем в буфер число из основного поля
                if (!isEmpty(mNumberField)) {
                    String number = getNumberField();
                    ClipData clipData = ClipData.newPlainText("mNumberField", number);
                    mClipboardManager.setPrimaryClip(clipData);

                    // Показываем сообщение
                    Toast.makeText(this, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;

            case R.id.memory_field:
                // Копируем в буфер число из поля памяти
                if (!isEmpty(mMemoryField)) {
                    String number = mMemoryField.getText().toString();
                    ClipData clipData = ClipData.newPlainText("mMemoryField", number);
                    mClipboardManager.setPrimaryClip(clipData);

                    // Показываем сообщение
                    Toast.makeText(this, R.string.copy_to_clipboard, Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
        }
        return false;
    }

    // Обработка нажатий кнопок калькулятора
    public void onClick(View view) {
        // Подключаем виброотклик
        hapticFeedbackOn(view);

        // Нажатие цифровых кнопок
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

            // Нажатие на ноль и точку
            case R.id.btn_0:
                if (mIsResult || isError()) clearCalculation();
                if (!isEmpty(mOperationField)) moveToHistory();
                if (getCountNumbers(mNumberField) < 11) {
                    if (getNumberField().equals("0")) {
                        mNumberField.setText("0.0");
                    } else if (getNumberField().equals("-0")) {
                        mNumberField.setText("-0.0");
                    } else {
                        addText("0");
                    }
                }
                break;
            case R.id.btn_dot:
                if (mIsResult || isError()) clearCalculation();
                if (!isEmpty(mOperationField)) moveToHistory();

                if (isEmpty(mNumberField)) addText("0.");
                else if (!getNumberField().contains(".") && getCountNumbers(mNumberField) < 11)
                    addText(".");
                break;

            // Полный сброс и посимвольное удаление
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

            // Кнопки вычислительных операций
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

            // Кнопка вычисления процента
            case R.id.btn_percent:
                if (!mIsResult && isEmpty(mOperationField)
                        && !isEmpty(mNumberField) && !isEmpty(mHistoryField)
                        && isNumber(mNumberField) && isNumber(mHistoryField)) {
                    removeExcessSymbol(mNumberField);
                    mIsPercent = true;
                    toCalculate();
                }
                break;

            // Кнопки работы с памятью
            case R.id.btn_MR:
                if (mIsResult || isError()) clearCalculation();
                if (!isEmpty(mOperationField)) moveToHistory();
                mNumberField.setText(mMemoryField.getText().toString());
                break;
            case R.id.btn_MC:
                mMemoryField.setText(" ");
                mMemoryMarker.setText(" ");
                break;
            case R.id.btn_M_add:
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
            case R.id.btn_M_sub:
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

            // Нажатие на поле с числом
            case R.id.number_field:
                invertNumber();
                break;

            // Кнопка выполнения рассчета
            case R.id.btn_calculate:
                toCalculate();
                break;
        }
    }

    // Вычисление результата
    private void toCalculate() {
        if (!isEmpty(mNumberField) && !isEmpty(mHistoryField) && isNumber(mNumberField) && isNumber(mHistoryField)) {
            removeExcessSymbol(mNumberField);
            String result = getString(R.string.error);

            String operation = mHistoryOperation.getText().toString().trim();
            String firstNumber = mHistoryField.getText().toString().trim();
            String secondNumber = mNumberField.getText().toString().trim();

            double first = Double.parseDouble(firstNumber);
            double second = Double.parseDouble(secondNumber);

            String historyResult = firstNumber + " " + operation + " " + secondNumber;
            if (mIsPercent) historyResult += "%";
            mHistoryField.setText(historyResult);
            mHistoryOperation.setText("=");

            if (mIsPercent) {
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
                        else result = getString(R.string.infinity);
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
                        else result = getString(R.string.infinity);
                        break;
                }
            }

            mNumberField.setText(result);
            mIsPercent = false;
            mIsResult = true;
        }
    }

    // Обработка и округление полученного результата
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
            else return getString(R.string.too_long_result);
        }
    }

    //
    // Вспомогательные методы
    //

    // Добавляет цифру в поле
    private void addNumber(String s) {
        if (mIsResult || isError()) clearCalculation();
        if (!isEmpty(mOperationField)) moveToHistory();

        if (getCountNumbers(mNumberField) < 11) {
            if (getNumberField().equals("0")) {
                String temp = "0." + s;
                mNumberField.setText(temp);
            } else if (getNumberField().equals("-0")) {
                String temp = "-0." + s;
                mNumberField.setText(temp);
            } else {
                String temp = getNumberField() + s;
                mNumberField.setText(temp);
            }
        }
    }

    // Заносит знак вычислительной операции в соответствующее поле
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

    // Переносит число и знак вычислительной операции в нижние поля
    private void moveToHistory() {
        mHistoryField.setText(getNumberField());
        mHistoryOperation.setText(mOperationField.getText().toString());
        mNumberField.setText(" ");
        mOperationField.setText(" ");
    }

    // Если в поле есть точка, то удаляет нули в конце числа
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

    // Получение строки из основного числового поля
    private String getNumberField() {
        return mNumberField.getText().toString().trim();
    }

    // Проверяет наличие сообщения об ошибке
    private boolean isError() {
        String temp = getNumberField();
        return temp.equals(getString(R.string.error)) ||
                temp.equals(getString(R.string.too_long_result)) ||
                temp.equals(getString(R.string.infinity));
    }

    // Считает количество цифр в числе
    private int getCountNumbers(TextView textView) {
        return textView.getText().toString().replaceAll("\\D", "").length();
    }

    // Очищает результат расчетов
    private void clearCalculation() {
        mNumberField.setText(" ");
        mOperationField.setText(" ");
        mHistoryField.setText(" ");
        mHistoryOperation.setText(" ");

        mIsPercent = false;
        mIsResult = false;
    }

    // Проверка на наличие числа в поле
    private boolean isNumber(TextView textView) {
        try {
            double number = Double.parseDouble(textView.getText().toString().trim());
            return true;
        } catch (NumberFormatException ignored) {
        }

        return false;
    }

    // Проверка поля на наличие данных
    private boolean isEmpty(TextView textView) {
        String s = textView.getText().toString();
        return s.equals("") || s.equals(" ");
    }

    // Присоединяет текст к уже существующему в основном числовом поле
    private void addText(String s) {
        String temp = mNumberField.getText().toString() + s;
        mNumberField.setText(temp);
    }

    // Смена знака числа
    public void invertNumber() {
        // Если поле не пустое, является числом и не является ошибкой
        if (!isEmpty(mNumberField) && isNumber(mNumberField) && !isError()) {
            String number = getNumberField();

            if (number.startsWith("-")) {
                number = number.substring(1, number.length());
            } else {
                number = "-" + number;
            }
            mNumberField.setText(number);
        }
    }

    // Подключение виброотклика к указанному вью
    private void hapticFeedbackOn(View view) {
        view.setHapticFeedbackEnabled(true);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
    }
}