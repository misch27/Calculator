package com.example.misch.calculator;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private TextView strResult;
    private int lastActivityId;
    private boolean isFirstNumInput = true;
    private boolean isAnswer = false;
    private double firstNum = 0;

    private final String notNumber = "не число";

    private NumberFormat nf = new DecimalFormat("#.########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        strResult = findViewById(R.id.txtView);
    }

    public void btnClick(View view) throws InterruptedException {
        if ((view.getId() == R.id.btnPl) || (view.getId() == R.id.btnMin)
                || (view.getId() == R.id.btnDiv) || (view.getId() == R.id.btnMul)) {
            mainMathAct(view);
        } else {
            switch (view.getId()){
                //очистка
                case R.id.btnClear: {
                    firstNum = 0;
                    isFirstNumInput = true;
                    isAnswer = false;
                    lastActivityId = 0;
                    strResult.setText("");
                    break;
                }
                case R.id.btnPoint: {
                    String curStatment = (String) strResult.getText();
                    //оптимистично верим, что все уже на новых версиях
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (curStatment.chars().filter(num -> num == '.').count() < 1) {
                            strResult.setText(strResult.getText() + view.getContentDescription().toString());
                        }
                    }
                    break;
                }
                //удаление последнего числа
                case R.id.btnDel: {
                    String curStatment =  strResult.getText().toString();
                    if (curStatment.length() == 1 || curStatment.length() == 0 ||
                            (curStatment.charAt(0) == '-' && curStatment.length() == 2) ||
                            (curStatment.equals(notNumber))){
                        curStatment = "";
                        firstNum = 0;
                    } else {
                        curStatment = curStatment.substring(0, curStatment.length() - 1);
                        firstNum = Double.parseDouble(curStatment);
                    }
                    strResult.setText(curStatment);
                    break;
                }
                //равенство
                case R.id.btnEq: {
                    isFirstNumInput = false;
                    mainMathAct(view);
                    break;
                }

                //ввод чисел
                default: {
                    if (isAnswer) {
                        strResult.setText("");
                        isAnswer = false;
                    }
                    strResult.setText(strResult.getText() + view.getContentDescription().toString());

                    break;
                }
            }
        }
        TimeUnit.MICROSECONDS.sleep(10);
    }

    private void mainMathAct(View view) {
        try {
            if (isFirstNumInput) {
                firstNum = Double.parseDouble((String) strResult.getText());
                isFirstNumInput = false;
                isAnswer = false;
                lastActivityId = view.getId();
                strResult.setText("");
            } else {
                switch (lastActivityId) {
                    case R.id.btnPl: {
                        firstNum = firstNum + Double.parseDouble((String) strResult.getText());
                        break;
                    }
                    case R.id.btnMin: {
                        firstNum = firstNum - Double.parseDouble((String) strResult.getText());
                        break;
                    }
                    case R.id.btnMul: {
                        firstNum = firstNum * Double.parseDouble((String) strResult.getText());
                        break;
                    }
                    case R.id.btnDiv: {
                        firstNum = firstNum / Double.parseDouble((String) strResult.getText());
                        break;
                    }

                }
                if (view.getId() == R.id.btnEq) {
                    lastActivityId = 0;
                } else {
                    lastActivityId = view.getId();
                }
                //только точки, иначе ошибки лететь будут
                strResult.setText(nf.format(firstNum).replaceAll(",", "."));
                isAnswer = true;
            }
        } catch (Exception npx){}

    }

}
