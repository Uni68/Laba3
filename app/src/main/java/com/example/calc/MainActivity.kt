package com.example.calc

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.calc.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var numberForPlusMinus = 0
    private var resultNumber = "0"
    private var currentOperation = ""
    private var lastOperation = ""
    private var tempNumber = ""
    private var result: Double = 0.0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button0.setOnClickListener {
            numberDown("0")
        }
        binding.button1.setOnClickListener {
            numberDown("1")
        }
        binding.button2.setOnClickListener {
            numberDown("2")
        }
        binding.button3.setOnClickListener {
            numberDown("3")
        }
        binding.button4.setOnClickListener {
            numberDown("4")
        }
        binding.button5.setOnClickListener {
            numberDown("5")
        }
        binding.button6.setOnClickListener {
            numberDown("6")
        }
        binding.button7.setOnClickListener {
            numberDown("7")
        }
        binding.button8.setOnClickListener {
            numberDown("8")
        }
        binding.button9.setOnClickListener {
            numberDown("9")
        }
        binding.buttonPoint.setOnClickListener {
            if(!binding.resultField.text.toString().contains(',')) {
                numberDown(",")
            }
        }

        binding.buttonAc.setOnClickListener {
            setButtonsColorOrange()
            if(binding.buttonAc.text == "AC") {
                lastOperation = ""
            }
            currentOperation = ""
            tempNumber = ""
            binding.buttonAc.text = "AC"
            setResult("0")

        }
        binding.buttonPlusminus.setOnClickListener {
            if (binding.resultField.text.toString() != "Ошибка") {
                if (currentOperation != "") {
                    if (tempNumber == "")
                        setTempNumber("-0")
                    else
                        setTempNumber(
                            correct(
                                tempNumber.replace(',', '.').toDouble() * -1
                            )
                        )
                } else {
                    binding.resultField.text = correct(
                        binding.resultField.text.toString().replace(',', '.').toDouble() * -1
                    )
                }
            }
        }
        binding.buttonPercent.setOnClickListener {
            if (currentOperation != "") {
                if (resultNumber != "Ошибка") {
                    if (tempNumber == "")
                        setTempNumber(
                            correct(
                                resultNumber.replace(',', '.').toDouble()
                                        * resultNumber.replace(',', '.').toDouble() / 100f
                            ).replace('.', ',')
                        )
                    else
                        setTempNumber(
                            correct(
                                tempNumber.replace(',', '.').toDouble()
                                        * resultNumber.replace(',', '.').toDouble() / 100f
                            ).replace('.', ',')
                        )
                }
                else
                    setTempNumber("Ошибка")
            } else {
                if (binding.resultField.text.toString() != "Ошибка") {
                    binding.resultField.text = correct(
                        binding.resultField.text.toString()
                            .replace(',', '.').toDouble() / 100f
                    ).replace('.', ',')
                }
                else
                    setResult("Ошибка")
            }
        }

        binding.buttonPlus.setOnClickListener {
            setButtonsColorOrange()
            if(tempNumber != "")
                countResult()
            setButtonColorWhite(binding.buttonPlus)
            currentOperation = "+"
        }
        binding.buttonMinus.setOnClickListener {
            setButtonsColorOrange()
            if (tempNumber != "")
                countResult()
            setButtonColorWhite(binding.buttonMinus)
            currentOperation = "-"
        }
        binding.buttonMultiply.setOnClickListener {
            setButtonsColorOrange()
            if (tempNumber != "")
                countResult()
            setButtonColorWhite(binding.buttonMultiply)
            currentOperation = "*"
        }
        binding.buttonDevide.setOnClickListener {
            setButtonsColorOrange()
            if (tempNumber != "")
                countResult()
            setButtonColorWhite(binding.buttonDevide)
            currentOperation = "/"
        }
        binding.buttonEquals.setOnClickListener {
            countResult()
        }
    }


    private fun countResult() {
        setButtonsColorOrange()
            try {
                if (currentOperation != "") {
                    if (tempNumber == "")
                        tempNumber = resultNumber
                    lastOperation = currentOperation + tempNumber
                }
                if (lastOperation != "") {
                    if(resultNumber != "Ошибка") {
                        result = if (tempNumber == "") ExpressionBuilder(
                            binding.resultField.text.toString().replace(',', '.')
                                    + lastOperation.replace(',', '.')
                        ).build().evaluate()
                        else ExpressionBuilder(
                            resultNumber.replace(',', '.')
                                    + lastOperation.replace(',', '.')
                        ).build().evaluate()
                        setResult(correct(result))
                    } else {
                        setResult("Ошибка")
                    }
                    tempNumber = ""
                    currentOperation = ""

                }
            } catch (e: Exception) {
                Log.d("Ошибка", "Сообщение: ${e.message}")
                if (e.message == "Division by zero!") {
                    tempNumber = ""
                    currentOperation = ""
                    setResult("Ошибка")
                }

        }
    }


    private fun correct(double: Double): String{
        var resultStr = double.toFloat().toString()
        if (double == double.toLong().toDouble()) {
            resultStr = double.toLong().toString()
        }
        return resultStr.replace('.', ',')
    }


    private fun setResult(str: String) {
        resultNumber = str
        binding.resultField.text = str
    }


    private fun setTempNumber(str: String) {
        tempNumber = str
        binding.resultField.text = str
    }


    private fun addResult(str: String) {
        if (resultNumber == "0") {
            if (str == ",") {
                resultNumber += str
                binding.resultField.append(str)
            }
            else {
                setResult(str)
            }
        } else {
            resultNumber += str
            binding.resultField.append(str)
        }
    }


    private  fun addTempNumber(str: String){
        if (tempNumber == "") {
            if (str == ",") {
                tempNumber = "0,"
                binding.resultField.text = "0,"
            }
            else {
                resultNumber = binding.resultField.text.toString()
                setTempNumber(str)

            }
        } else {
            tempNumber += str
            binding.resultField.append(str)
        }
    }


    private fun numberDown(str: String) {
        setButtonsColorOrange()
        if(currentOperation != "")
            addTempNumber(str)
        else
            addResult(str)
        if (binding.resultField.text.toString() !== "0") {
            binding.buttonAc.text = "C"
        }
        if (numberForPlusMinus == 1) {
            numberForPlusMinus = 0
            binding.resultField.text = "-"
        }
    }


    private fun setButtonColorWhite(button: Button) {
        button.setBackgroundColor(resources.getColor(R.color.white, applicationContext.theme))
        button.setTextColor(resources.getColor(R.color.orange, applicationContext.theme))
    }


    private fun setButtonsColorOrange() {
        setButtonColorOrange(binding.buttonPlus)
        setButtonColorOrange(binding.buttonMinus)
        setButtonColorOrange(binding.buttonMultiply)
        setButtonColorOrange(binding.buttonDevide)
    }


    private fun setButtonColorOrange(button: Button){
        button.setBackgroundColor(resources.getColor(R.color.orange, applicationContext.theme))
        button.setTextColor(resources.getColor(R.color.white, applicationContext.theme))
    }
}