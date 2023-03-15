package com.example.btbs_tuan9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.btbs_tuan9.databinding.ActivityInsertionBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertionBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("Employees")

        binding.btnSave.setOnClickListener {
            saveEmployeeData()
        }


    }

    private fun saveEmployeeData() {
        val empName = binding.edtEmpName.text.toString()
        val empAge = binding.edtEmpAge.text.toString()
        val empSalary = binding.edtEmpSalary.text.toString()

        //push data to firebase

        val empId = dbRef.push().key!!
        val employee = EmployeeModel(empId, empName, empAge, empSalary)

        // check null
        if (empName.isEmpty()){
             binding.edtEmpName.error = "Hay nhap ten"
            return
        }
        if(empAge.isEmpty()){
            binding.edtEmpAge.error = "Hay nhap tuoi"
            return
        }
        if(empSalary.isEmpty()){
            binding.edtEmpSalary.error = "Hay nhap so luong"
            return
        }


        dbRef.child(empId).setValue(employee)
            .addOnCompleteListener{
                Toast.makeText(this, "Them data thanh cong",Toast.LENGTH_SHORT).show()
                binding.edtEmpName.setText("")
                binding.edtEmpAge.setText("")
                binding.edtEmpSalary.setText("")
            }
            .addOnFailureListener{
                err -> Toast.makeText(this, "Them data that bai ${err.message}",Toast.LENGTH_SHORT).show()
            }

    }


}