package com.example.btbs_tuan9

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.btbs_tuan9.adapter.EmpAdapter
import com.example.btbs_tuan9.databinding.ActivityFetchingBinding
import com.google.firebase.database.*

class FetchingActivity : AppCompatActivity() {
    private lateinit var ds: ArrayList<EmployeeModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var binding: ActivityFetchingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFetchingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvEmp.layoutManager = LinearLayoutManager(this)
        binding.rvEmp.setHasFixedSize(true)
        ds = arrayListOf<EmployeeModel>()
        GetThongTin()
    }

    private fun GetThongTin() {
        binding.rvEmp.visibility = View.GONE
        binding.txtLoadingData.visibility = View.VISIBLE
        dbRef = FirebaseDatabase.getInstance().getReference("Employees")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ds.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(EmployeeModel::class.java)
                        ds.add(empData!!)
                    }
                    val mAdapter = EmpAdapter(ds)
                    binding.rvEmp.adapter = mAdapter


                    //__________________________click item________________________________

                    mAdapter.setOnItemClickListener(object : EmpAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent =
                                Intent(this@FetchingActivity, EmployeeDetailsActivity::class.java)

                            intent.putExtra("empId",ds[position].empId)
                            intent.putExtra("empName",ds[position].empName)
                            intent.putExtra("empAge",ds[position].empAge)
                            intent.putExtra("empSalary",ds[position].empSalary)



                            startActivity(intent)
                        }
                    })

                    //____________________________________________________________________
                    binding.rvEmp.visibility = View.VISIBLE
                    binding.txtLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}