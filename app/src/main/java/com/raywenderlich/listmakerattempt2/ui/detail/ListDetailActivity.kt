package com.raywenderlich.listmakerattempt2.ui.detail

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.preference.PreferenceManager
import com.raywenderlich.listmakerattempt2.MainActivity
import com.raywenderlich.listmakerattempt2.R
import com.raywenderlich.listmakerattempt2.databinding.ListDetailActivityBinding
import com.raywenderlich.listmakerattempt2.models.TaskList
import com.raywenderlich.listmakerattempt2.ui.detail.ui.detail.ListDetailFragment
import com.raywenderlich.listmakerattempt2.ui.detail.ui.detail.ListDetailViewModel
import com.raywenderlich.listmakerattempt2.ui.detail.ui.detail.ListItemsRecyclerViewAdapter
import com.raywenderlich.listmakerattempt2.ui.main.MainViewModel
import com.raywenderlich.listmakerattempt2.ui.main.MainViewModelFactory

class ListDetailActivity : AppCompatActivity() {

    lateinit var binding: ListDetailActivityBinding

    lateinit var viewModel: MainViewModel

    lateinit var fragment: ListDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ListDetailActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addTaskButton.setOnClickListener {
            showCreateTaskDialog()
        }

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        ).get(MainViewModel::class.java)
        viewModel.list = intent.getParcelableExtra(MainActivity.INTENT_LIST_KEY)!!

        title = viewModel.list.name

        fragment = ListDetailFragment.newInstance()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_container, fragment)
                .commitNow()
        }
    }

    override fun onBackPressed() {
        val bundle = Bundle()
        bundle.putParcelable(MainActivity.INTENT_LIST_KEY, viewModel.list)

        val intent = Intent()
        intent.putExtras(bundle)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }

    private fun showCreateTaskDialog() {
        //1
        val taskEditText = EditText(this)
        taskEditText.inputType = InputType.TYPE_CLASS_TEXT

        //2
        AlertDialog.Builder(this)
            .setTitle(R.string.task_to_add)
            .setView(taskEditText)
            .setPositiveButton(R.string.add_task) { dialog, _ ->
                // 3
                val task = taskEditText.text.toString()
                viewModel.addTask(task)
                //5
                dialog.dismiss()
            }
            //6
            .create()
            .show()
    }}
