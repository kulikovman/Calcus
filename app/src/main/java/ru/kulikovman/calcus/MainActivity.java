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
    private TextView resultField, calculationField;
    private boolean isResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultField = (TextView) findViewById(R.id.resultField);
        calculationField = (TextView) findViewById(R.id.calculationField);
    }


    //обработка нажатий кнопок
    public void onClick(View view) {
        view.setHapticFeedbackEnabled(true);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

        switch (view.getId()) {
            //базовые кнопки
            case R.id.btn_1:
                if (isResult) removeResult();
                if (isZero()) toAdd(".1");
                else toAdd("1");
                break;
            case R.id.btn_2:
                if (isResult) removeResult();
                if (isZero()) toAdd(".2");
                else toAdd("2");
                break;
            case R.id.btn_3:
                if (isResult) removeResult();
                if (isZero()) toAdd(".6");
                else toAdd("3");
                break;
            case R.id.btn_4:
                if (isResult) removeResult();
                if (isZero()) toAdd(".4");
                else toAdd("4");
                break;
            case R.id.btn_5:
                if (isResult) removeResult();
                if (isZero()) toAdd(".5");
                else toAdd("5");
                break;
            case R.id.btn_6:
                if (isResult) removeResult();
                if (isZero()) toAdd(".6");
                else toAdd("6");
                break;
            case R.id.btn_7:
                if (isResult) removeResult();
                if (isZero()) toAdd(".7");
                else toAdd("7");
                break;
            case R.id.btn_8:
                if (isResult) removeResult();
                if (isZero()) toAdd(".8");
                else toAdd("8");
                break;
            case R.id.btn_9:
                if (isResult) removeResult();
                if (isZero()) toAdd(".9");
                else toAdd("9");
                break;
            case R.id.btn_0:
                if (isResult) removeResult();
                if (isZero()) toAdd(".0");
                else toAdd("0");
                break;
            case R.id.btn_000:
                if (isResult) removeResult();
                if (isZero()) toAdd(".000");
                else if (isNumber() || isDot()) toAdd("000");
                else toAdd("0.00");
                break;
            case R.id.btn_dot:
                if (isResult) removeResult();
                if (isNull() || isEndOperation()) toAdd("0.");
                else if (isNumber() && !getLastElement().contains(".")) toAdd(".");
                break;
            case R.id.btn_pi:
                if (isResult) removeResult();
                if (isNull() || isEndOperation()) toAdd("3.14159265359");
                break;
            case R.id.btn_e:
                if (isResult) removeResult();
                if (isNull() || isEndOperation()) toAdd("2.71828182846");
                break;


            //очистка поля результата
            case R.id.btn_reset:
                removeResult();
                break;
            case R.id.btn_delete:
                if (isResult) removeResult();
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
                if (isComplete()) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    resultField.setText(String.valueOf(getCalculate()));
                    removeExcessElements();
                    toAdd(" + ");
                } else if (!isNull()) toAdd(" + ");
                isResult = false;
                break;
            case R.id.btn_subtraction:
                removeExcessElements();
                if (isComplete()) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    resultField.setText(String.valueOf(getCalculate()));
                    removeExcessElements();
                    toAdd(" - ");
                } else if (!isNull()) toAdd(" - ");
                isResult = false;
                break;
            case R.id.btn_multiplication:
                removeExcessElements();
                if (isComplete()) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    resultField.setText(String.valueOf(getCalculate()));
                    removeExcessElements();
                    toAdd(" × ");
                } else if (!isNull()) toAdd(" × ");
                isResult = false;
                break;
            case R.id.btn_devision:
                removeExcessElements();
                if (isComplete()) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    resultField.setText(String.valueOf(getCalculate()));
                    removeExcessElements();
                    toAdd(" / ");
                } else if (!isNull()) toAdd(" / ");
                isResult = false;
                break;
            case R.id.btn_power:
                removeExcessElements();
                if (isComplete()) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    resultField.setText(String.valueOf(getCalculate()));
                    removeExcessElements();
                    toAdd(" ^ ");
                } else if (!isNull()) toAdd(" ^ ");
                isResult = false;
                break;


            //дополнительные операции
            case R.id.btn_percent:
                removeExcessElements();
                if (isComplete() && (getOperation().equals("+") || getOperation().equals("-")
                        || getOperation().equals("×") || getOperation().equals("/"))) {

                    double result = 0;
                    switch (getOperation()) {
                        case "+":
                            result = getFirst() + (getFirst() / 100 * getSecond());
                            break;
                        case "-":
                            result = getFirst() - (getFirst() / 100 * getSecond());
                            break;
                        case "×":
                            result = getFirst() * (getFirst() / 100 * getSecond());
                            break;
                        case "/":
                            result = getFirst() / (getFirst() / 100 * getSecond());
                            break;
                    }

                    calculationField.setText(String.valueOf(getResult() + "% ="));
                    resultField.setText(String.valueOf(result));
                    removeExcessElements();
                    isResult = true;
                }
                break;
            case R.id.btn_square_root:
                removeExcessElements();
                if (!isNull() && !isComplete()) {
                    calculationField.setText(String.valueOf("√ " + getResult() + " ="));
                    resultField.setText(String.valueOf(Math.sqrt(getFirst())));
                    removeExcessElements();
                    isResult = true;
                }
                break;
            case R.id.btn_sinus:
                removeExcessElements();
                if (!isNull() && !isComplete()) {
                    calculationField.setText(String.valueOf("sin (" + getResult() + ") ="));
                    resultField.setText(String.valueOf(Math.sin(getFirst())));
                    removeExcessElements();
                    isResult = true;
                }
                break;
            case R.id.btn_cosine:
                removeExcessElements();
                if (!isNull() && !isComplete()) {
                    calculationField.setText(String.valueOf("cos (" + getResult() + ") ="));
                    resultField.setText(String.valueOf(Math.cos(getFirst())));
                    removeExcessElements();
                    isResult = true;
                }
                break;
            case R.id.btn_tangent:
                removeExcessElements();
                if (!isNull() && !isComplete()) {
                    calculationField.setText(String.valueOf("tan (" + getResult() + ") ="));
                    resultField.setText(String.valueOf(Math.tan(getFirst())));
                    removeExcessElements();
                    isResult = true;
                }
                break;
            case R.id.btn_natural_logarithm:
                removeExcessElements();
                if (!isNull() && !isComplete()) {
                    calculationField.setText(String.valueOf("ln (" + getResult() + ") ="));
                    resultField.setText(String.valueOf(Math.log(getFirst())));
                    removeExcessElements();
                    isResult = true;
                }
                break;
            case R.id.btn_logarithm:
                removeExcessElements();
                if (!isNull() && !isComplete()) {
                    calculationField.setText(String.valueOf("log (" + getResult() + ") ="));
                    resultField.setText(String.valueOf(Math.log10(getFirst())));
                    removeExcessElements();
                    isResult = true;
                }
                break;


            case R.id.btn_memory_copy:

                break;
            case R.id.btn_memory_past:

                break;


            //копирование результата в буфер
            case R.id.resultField:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(RESULT_FIELD, getResult());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, R.string.copy_co_clipboard, Toast.LENGTH_SHORT).show();
                break;


            //окончательный рассчет
            case R.id.btn_calculate:
                removeExcessElements();
                if (isComplete()) {
                    calculationField.setText(String.valueOf(getResult() + " ="));
                    resultField.setText(String.valueOf(getCalculate()));
                    removeExcessElements();
                    isResult = true;
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

    private boolean isZero() {
        return getLastElement().equals("0");
    }

    private boolean isDot() {
        return getResult().endsWith(".");
    }

    private boolean isNumber() {
        try {
            int number = Integer.parseInt(getLastSymbol());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isEndOperation() {
        return getResult().endsWith("+") || getResult().endsWith("-")
                || getResult().endsWith("×") || getResult().endsWith("/")
                || getResult().endsWith("^");
    }


    //удаление данных
    private void removeLastSymbol() {
        String deleteLastOperation = getResult().substring(0, getResult().length() - 1);
        resultField.setText(deleteLastOperation);
    }

    private void removeOperation() {
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
        if (isEndOperation()) removeOperation();
    }


    //получение данных
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

    public double getFirst() {
        removeExcessElements();
        String[] temp = getResult().split(" ");

        double numberFirst;
        if (temp[0].contains(".")) numberFirst = Double.parseDouble(temp[0]);
        else numberFirst = Integer.parseInt(temp[0]);

        return numberFirst;
    }

    public double getSecond() {
        removeExcessElements();
        String[] temp = getResult().split(" ");

        double numberSecond;
        if (temp[2].contains(".")) numberSecond = Double.parseDouble(temp[2]);
        else numberSecond = Integer.parseInt(temp[2]);

        return numberSecond;
    }

    public String getOperation() {
        String[] temp = getResult().split(" ");
        return temp[1];
    }

    private double getCalculate() {
        double result = 0;
        switch (getOperation()) {
            case "+":
                result = getFirst() + getSecond();
                break;
            case "-":
                result = getFirst() - getSecond();
                break;
            case "×":
                result = getFirst() * getSecond();
                break;
            case "/":
                result = getFirst() / getSecond();
                break;
            case "^":
                result = Math.pow(getFirst(), getSecond());
                break;
        }

        return result;
    }


    //сохранение информации в полях
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
