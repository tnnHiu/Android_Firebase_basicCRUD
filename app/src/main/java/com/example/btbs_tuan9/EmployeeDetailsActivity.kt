package com.example.btbs_tuan9

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.btbs_tuan9.databinding.ActivityEmployeeDetailsBinding

import com.google.firebase.database.FirebaseDatabase

class EmployeeDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployeeDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setValueToView()

        binding.btnDelete.setOnClickListener {
            deleteRecord(intent.getStringExtra("empId").toString())
        }

        binding.btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("empId").toString(),
                intent.getStringExtra("empName").toString(),
                intent.getStringExtra("empAge").toString(),
                intent.getStringExtra("empSalary").toString()
            )
        }
    }

    private fun openUpdateDialog(
        empId: String,
        empName: String,
        empAge: String,
        empSalary: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)
        mDialog.setView(mDialogView)
        //update data
        val etEmpName = mDialogView.findViewById<EditText>(R.id.etEmpName)
        val etEmpAge = mDialogView.findViewById<EditText>(R.id.etEmpAge)
        val etEmSalary = mDialogView.findViewById<EditText>(R.id.etEmpSalary)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etEmpName.setText(intent.getStringExtra("empName").toString())
        etEmpAge.setText(intent.getStringExtra("empAge").toString())
        etEmSalary.setText(intent.getStringExtra("empSalary").toString())

        mDialog.setTitle("Updating $empName data")
        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmployeeData(
                empId,
                etEmpName.text.toString(),
                etEmpAge.text.toString(),
                etEmSalary.text.toString()
            )
            Toast.makeText(applicationContext,"Updated",Toast.LENGTH_SHORT).show()
            binding.tvEmpName.text = etEmpName.text.toString()
            binding.tvEmpAge.text = etEmpAge.text.toString()
            binding.tvEmpSalary.text = etEmSalary.text.toString()
            alertDialog.dismiss()
        }

    }

    private fun updateEmployeeData(
        empId: String,
        empName: String,
        empAge: String,
        empSalary: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(empId)
        val empInfo = EmployeeModel(empId, empName, empAge, empSalary)
        dbRef.setValue(empInfo)
    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Employees").child(id)
        val mTask = dbRef.removeValue()
        mTask.addOnSuccessListener {
            Toast.makeText(this, "Da xoa data", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { err ->
            Toast.makeText(this, "Xoa data that bai ${err.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setValueToView() {
        binding.tvEmpId.text = intent.getStringExtra("empId")
        binding.tvEmpName.text = intent.getStringExtra("empName")
        binding.tvEmpAge.text = intent.getStringExtra("empAge")
        binding.tvEmpSalary.text = intent.getStringExtra("empSalary")
    }
}