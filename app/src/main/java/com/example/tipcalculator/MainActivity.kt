package com.example.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.example.tipcalculator.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENTAGE = 15
private const val INITIAL_PEOPLE_COUNT = 1
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var baseAmountInput: EditText
    private lateinit var percentageLabel: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var tipAmount: TextView
    private lateinit var totalAmount: TextView
    private lateinit var encouragementLabel: TextView
    private lateinit var peopleCountInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)

        baseAmountInput = binding.baseAmountInput
        percentageLabel = binding.percentageLabel
        seekBar = binding.percentageSeekBar
        tipAmount = binding.tipAmount
        totalAmount = binding.totalAmount
        encouragementLabel = binding.encouragementLabel
        peopleCountInput = binding.peopleCountInput

        seekBar.progress = INITIAL_TIP_PERCENTAGE
        percentageLabel.text = "%$INITIAL_TIP_PERCENTAGE"
        seekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "seekbar at $progress")
                percentageLabel.text = "%$progress"
                computeTipAndTotal()
                changeMessage(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        baseAmountInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })

        peopleCountInput.setText(INITIAL_PEOPLE_COUNT.toString())
        peopleCountInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                computeTipAndTotal()
            }

        })

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }
    }

    private fun changeMessage(percent: Int) {
        val emoji = when (percent) {
            in 0 .. 5 -> "\uD83D\uDCA9"
            in 6.. 10 -> "\uD83E\uDEE5"
            in 11..20 -> "\uD83D\uDE1A"
            in 21..25 -> "\uD83D\uDE0B"
            else -> "\uD83E\uDD29"
        }
        encouragementLabel.text = emoji
    }

    private fun computeTipAndTotal() {
        if (baseAmountInput.text.isEmpty() or peopleCountInput.text.isEmpty()) {
            tipAmount.text = "0.0"
            totalAmount.text = "0.0"
            return
        }
        val tipPercentage = seekBar.progress
        val baseAmount = baseAmountInput.text.toString().toDouble()
        val peopleCount = peopleCountInput.text.toString().toInt()
        val tip = baseAmount * tipPercentage / 100
        val total = baseAmount + tip
        val tipPerPerson = tip / peopleCount
        tipAmount.text = "%.2f".format(tipPerPerson)
        totalAmount.text = "%.2f".format(total)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}