package ru.kulikovman.calcus;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import java.math.BigDecimal;
import java.math.RoundingMode;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView resultField, calculationField, memoryField;
    private boolean isResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = (TextView) findViewById(R.id.resultField);
        calculationField = (TextView) findViewById(R.id.calculationField);
        memoryField = (TextView) findViewById(R.id.memoryField);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }


    //обработка нажатий кнопок
    public void onClick(View view) {
        view.setHapticFeedbackEnabled(true);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        switch (view.getId()) {
            //базовые кнопки
            case R.id.btn_1:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("1");
                break;
            case R.id.btn_2:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("2");
                break;
            case R.id.btn_3:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("3");
                break;
            case R.id.btn_4:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("4");
                break;
            case R.id.btn_5:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("5");
                break;
            case R.id.btn_6:
                if (isResult) removeResult();
                if (getLenght() < 13) toAdd("6");
                break;
            case R.id.btn_7:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("7");
                break;
            case R.id.btn_8:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("8");
                break;
            case R.id.btn_9:
                if (isResult || isError()) removeResult();
                if (getLenght() < 13) toAdd("9");
                break;
            case R.id.btn_0:
                if (isResult || isError()) removeResult();
                if (isNull()) toAdd("0.");
                else if (getLastElement().equals("0")) toAdd(".0");
                else if (getLenght() < 13) toAdd("0");
                break;
            case R.id.btn_000:
                if (isResult || isError()) removeResult();
                if (isNull() || isEndOperation()) toAdd("0.00");
                else if (getLenght() < 11) toAdd("000");
                break;
            case R.id.btn_pi:
                if (isResult || isError()) removeResult();
                if (isNull() || isEndOperation()) toAdd("3.14159265359");
                break;
            case R.id.btn_e:
                if (isResult || isError()) removeResult();
                if (isNull() || isEndOperation()) toAdd("2.71828182846");
                break;
            case R.id.btn_dot:
                if (isResult || isError()) removeResult();
                if (isNull() || isEndOperation()) toAdd("0.");
                else if (isNumber() && !getLastElement().contains(".")) toAdd(".");
                break;


            //очистка поля результата
            case R.id.btn_reset:
                removeResult();
                break;
            case R.id.btn_delete:
                if (isResult || isError()) removeResult();
                if (getResult().length() > 1) {
                    if (isEndOperation()) removeOperation();
                    else removeLastSymbol();
                } else {
                    resultField.setText(" ");
                }
                break;


            //базовые операции
            case R.id.btn_addition:
                removeExcessElements();
                if (!isError()) {
                    if (isComplete() && isNumber()) {
                        calculationField.setText(String.valueOf(getResult() + " ="));
                        toCalculate();
                        toAdd(" + ");
                    } else if (!isNull()) toAdd(" + ");
                    isResult = false;
                }
                break;
            case R.id.btn_subtraction:
                removeExcessElements();
                if (!isError()) {
                    if (isComplete() && isNumber()) {
                        calculationField.setText(String.valueOf(getResult() + " ="));
                        toCalculate();
                        toAdd(" - ");
                    } else if (!isNull()) toAdd(" - ");
                    isResult = false;
                }
                break;
            case R.id.btn_multiplication:
                removeExcessElements();
                if (!isError()) {
                    if (isComplete() && isNumber()) {
                        calculationField.setText(String.valueOf(getResult() + " ="));
                        toCalculate();
                        toAdd(" × ");
                    } else if (!isNull()) toAdd(" × ");
                    isResult = false;
                }
                break;
            case R.id.btn_devision:
                removeExcessElements();
                if (!isError()) {
                    if (isComplete() && isNumber()) {
                        calculationField.setText(String.valueOf(getResult() + " ="));
                        toCalculate();
                        toAdd(" ÷ ");
                    } else if (!isNull()) toAdd(" ÷ ");
                    isResult = false;
                }
                break;
            case R.id.btn_power:
                removeExcessElements();
                if (!isError()) {
                    if (isComplete() && isNumber()) {
                        calculationField.setText(String.valueOf(getResult() + " ="));
                        toCalculate();
                        toAdd(" ^ ");
                    } else if (!isNull()) toAdd(" ^ ");
                    isResult = false;
                }
                break;


            //дополнительные операции
            case R.id.btn_percent:
                removeExcessElements();
                String[] temp = getResult().split(" ");
                if (isComplete() && !isError()) {
                    if (temp[1].equals("+") || temp[1].equals("-")
                            || temp[1].equals("×") || temp[1].equals("÷")) {
                        calculationField.setText(String.valueOf(getResult() + "% ="));
                        toCalculate();
                    }
                }
                break;
            case R.id.btn_square_root:
                removeExcessElements();
                if (!isNull() && !isComplete() && isNumber()) {
                    calculationField.setText(String.valueOf("√ " + getResult() + " ="));
                    toCalculate();
                }
                break;
            case R.id.btn_sinus:
                removeExcessElements();
                if (!isNull() && !isComplete() && isNumber()) {
                    calculationField.setText(String.valueOf("sin (" + getResult() + ") ="));
                    toCalculate();
                }
                break;
            case R.id.btn_cosine:
                removeExcessElements();
                if (!isNull() && !isComplete() && isNumber()) {
                    calculationField.setText(String.valueOf("cos (" + getResult() + ") ="));
                    toCalculate();
                }
                break;
            case R.id.btn_tangent:
                removeExcessElements();
                if (!isNull() && !isComplete() && isNumber()) {
                    calculationField.setText(String.valueOf("tan (" + getResult() + ") ="));
                    toCalculate();
                }
                break;
            case R.id.btn_natural_logarithm:
                removeExcessElements();
                if (!isNull() && !isComplete() && isNumber()) {
                    calculationField.setText(String.valueOf("ln (" + getResult() + ") ="));
                    toCalculate();
                }
                break;
            case R.id.btn_logarithm:
                removeExcessElements();
                if (!isNull() && !isComplete() && isNumber()) {
                    calculationField.setText(String.valueOf("log (" + getResult() + ") ="));
                    toCalculate();
                }
                break;


            //работа с памятью
            case R.id.btn_memory_copy:
                removeExcessElements();
                if (!isNull() && !isComplete() && isNumber()) memoryField.setText(getResult());
                else if (isNull()) memoryField.setText(" ");
                break;
            case R.id.btn_memory_past:
                if (isResult) removeResult();
                if (isNull() || isEndOperation()) toAdd(memoryField.getText().toString());
                break;


            //копирование результата в буфер
            case R.id.resultField:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("resultField", getResult());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, R.string.copy_co_clipboard, Toast.LENGTH_SHORT).show();
                break;


            //окончательный рассчет
            case R.id.btn_calculate:
                removeExcessElements();
                if (isComplete()) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    toCalculate();
                }
                break;
        }
    }


    //присоединение информации к строке
    private void toAdd(String s) {
        String temp = resultField.getText().toString() + s;
        resultField.setText(temp);
    }

    //всевозможные проверки
    private boolean isComplete() {
        String[] temp = getResult().split(" ");
        return temp.length == 3;
    }

    private boolean isNull() {
        return getResult().equals("") || getResult().equals(" ");
    }

    private boolean isDot() {
        return getResult().endsWith(".");
    }

    private boolean isNumber() {
        return getLastSymbol().replaceAll("\\d", "num").contains("num");
    }

    private boolean isError() {
        return getResult().contains("Error") || getResult().contains("Too long result")
                || getResult().contains("Infinity") || getResult().contains("NaN");
    }

    private boolean isEndOperation() {
        return getResult().endsWith("+") || getResult().endsWith("-")
                || getResult().endsWith("×") || getResult().endsWith("÷")
                || getResult().endsWith("^");
    }

    private int getLenght() {
        return getLastElement().length();
    }

    //удаление данных
    private void removeLastSymbol() {
        resultField.setText(getResult().substring(0, getResult().length() - 1));
    }

    private void removeOperation() {
        resultField.setText(getResult().substring(0, getResult().length() - 2));
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
        if (isEndOperation()) removeOperation();
    }


    //получение данных
    private String getResult() {
        return resultField.getText().toString().trim();
    }

    private String getLastSymbol() {
        String temp = resultField.getText().toString();
        return temp.substring(temp.length() - 1);

    }

    private String getLastElement() {
        String[] temp = getResult().split(" ");
        return temp[temp.length - 1];
    }

    public static double toRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //все расчеты производятся в этом методе!
    private void toCalculate() {
        String s = calculationField.getText().toString();
        String[] temp = s.split(" ");

        String result = "Error";

        try {
            double num, first, second;

            if (s.contains("%")) {
                first = Double.parseDouble(temp[0]);
                second = Double.parseDouble(temp[2].substring(0, temp[2].length() - 1));

                switch (temp[1]) {
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


            } else if (s.contains("sin")) {
                num = Double.parseDouble(temp[1].substring(1, temp[1].length() - 1));
                result = String.valueOf(Math.sin(num));


            } else if (s.contains("cos")) {
                num = Double.parseDouble(temp[1].substring(1, temp[1].length() - 1));
                result = String.valueOf(Math.cos(num));


            } else if (s.contains("tan")) {
                num = Double.parseDouble(temp[1].substring(1, temp[1].length() - 1));
                result = String.valueOf(Math.tan(num));


            } else if (s.contains("log")) {
                num = Double.parseDouble(temp[1].substring(1, temp[1].length() - 1));
                result = String.valueOf(Math.log10(num));


            } else if (s.contains("ln")) {
                num = Double.parseDouble(temp[1].substring(1, temp[1].length() - 1));
                result = String.valueOf(Math.log(num));


            } else if (s.contains("√")) {
                num = Double.parseDouble(temp[1]);
                result = String.valueOf(Math.sqrt(num));


                //подсчет обычных чисел
            } else {
                first = Double.parseDouble(temp[0]);
                second = Double.parseDouble(temp[2]);

                switch (temp[1]) {
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
                    case "^":
                        result = String.valueOf(Math.pow(first, second));
                        break;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //сокращаем длину результата
        if (result.length() > 13) {
            if (result.toLowerCase().contains("e")) {
                result = "Too long result";
            } else {
                result = result.substring(0, 13);
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

        //записываем результат и ставим флаг
        resultField.setText(result);
        isResult = true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("resultField", resultField.getText().toString());
        outState.putString("calculationField", calculationField.getText().toString());
        outState.putBoolean("isResult", isResult);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resultField.setText(savedInstanceState.getString("resultField"));
        calculationField.setText(savedInstanceState.getString("calculationField"));
        isResult = savedInstanceState.getBoolean("isResult");
    }
}
