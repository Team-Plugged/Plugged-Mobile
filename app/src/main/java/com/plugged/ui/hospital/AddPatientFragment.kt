package com.plugged.ui.hospital

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.plugged.R
import com.plugged.models.RegPatient
import com.plugged.utils.Constants.Companion.TAG
import com.plugged.utils.Resource
import com.plugged.viewmodel.PluggedViewModel
import com.tuyenmonkey.mkloader.MKLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_patient.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CODE_IMAGE_PICK = 100

@AndroidEntryPoint
class AddPatientFragment : Fragment() {

    private var imageUri: Uri? = null
    private val imageRef = Firebase.storage.reference
    private var imageUrl = ""
    private val viewModel: PluggedViewModel by viewModels()
    var timer = Timer()
    val DELAY: Long = 3000
    var dob = ""


//    fun upload(uri: URI){
//        val file: File = FileUtils.getFile(this, fileUri)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_patient, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner_gender = gender_spinner
        val spinner_blood_group = blood_group
        val spinner_genotype = genotype
        val progress_bar = view.findViewById<MKLoader>(R.id.progressBar)

        // Spinner For Gender
        var gender = ""
        val gender_type = resources.getStringArray(R.array.gender)
        if (spinner_gender != null) {
            val adapter =
                activity?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item,
                        gender_type
                    )
                }
            if (adapter != null) {
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            }
            spinner_gender.adapter = adapter

            spinner_gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    gender = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    gender = "Male"
                }
            }

        }

        // Spinner for BloodGroup

        var bloodGroup = ""
        val blood_type = resources.getStringArray(R.array.blood_group)
        if (spinner_blood_group != null) {
            val adapter =
                activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, blood_type) }
            if (adapter != null) {
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            }
            spinner_blood_group.adapter = adapter

            spinner_blood_group.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        bloodGroup = parent.getItemAtPosition(position).toString()

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        bloodGroup = "A+"
                    }
                }

        }

        // Spinner Genotype

        var genoType = ""
        val geno_type = resources.getStringArray(R.array.genotype)
        if (spinner_genotype != null) {
            val adapter =
                activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, geno_type) }
            if (adapter != null) {
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            }
            spinner_genotype.adapter = adapter

            spinner_genotype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    genoType = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    genoType = "AA"
                }
            }

        }


        btn_add.setOnClickListener {

            if (edit_firstName.text.isNullOrEmpty()) {
                edit_firstName.error = "First Name is Required"
                edit_firstName.requestFocus()
                return@setOnClickListener
            }

            if (edit_lastName.text.isNullOrEmpty()) {
                edit_lastName.error = "Last Name is Required"
                edit_lastName.requestFocus()
                return@setOnClickListener
            }

            if (edit_email.text.isNullOrEmpty()) {
                edit_email.error = "Email is Required"
                edit_email.requestFocus()
                return@setOnClickListener
            }

            if (edit_contact.text.isNullOrEmpty()) {
                edit_contact.error = "Contact Number is Required"
                edit_contact.requestFocus()
                return@setOnClickListener
            }
            if (edit_address.text.isNullOrEmpty()) {
                edit_address.error = "Address is Required"
                edit_address.requestFocus()
                return@setOnClickListener
            }

            if (edit_height.text.isNullOrEmpty()) {
                edit_height.error = "Height is Required"
                edit_height.requestFocus()
                return@setOnClickListener
            }

            if (edit_weight.text.isNullOrEmpty()) {
                edit_weight.error = "Weight is Required"
                edit_weight.requestFocus()
                return@setOnClickListener
            }

            val first_name = edit_firstName.text.toString()
            val last_name = edit_firstName.text.toString()
//            val age = edit_age.text.toString()
            val contact = edit_contact.text.toString()
            val address = edit_address.text.toString()
            val height = edit_height.text.toString()
            val weight = edit_weight.text.toString()
            val email = edit_email.text.toString()
            val password = edit_password.text.toString()

            val image = "http"
            val Patient = RegPatient(
                address,
                contact,
                dob,
                email,
                first_name,
                gender,
                genoType,
                height,
                imageUrl,
                last_name,
                password,
                weight
            )


            Log.d(TAG, Patient.toString())
            viewModel.RegisterPatient(Patient)
            viewModel.getPatient()


//            Observer for register Patient
            viewModel.registerPatientResponse.observe(viewLifecycleOwner, Observer { response ->

                when (response) {

                    is Resource.Loading -> {

                        progress_bar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progress_bar.visibility = View.INVISIBLE

                        response?.data.let {
                            successDialog()

                        }


                    }

                    is Resource.Error -> {
                        progress_bar.visibility = View.INVISIBLE

                        response.message?.let {
                            Toast.makeText(activity, "Error Occured: $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


                }


            })

//                Observer for Image Upload
            viewModel.uploadPic.observe(viewLifecycleOwner, Observer { response ->
                when (response) {

                    is Resource.Loading -> {


                        progress_bar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        progress_bar.visibility = View.INVISIBLE

                        response?.data.let {
                            Log.d(TAG, it.toString())

                        }

                    }

                    is Resource.Error -> {
                        progress_bar.visibility = View.INVISIBLE

                        response.message?.let {
                            Toast.makeText(activity, "Error Occured: $it", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


                }


            })


        }

        btn_dob.setOnClickListener {
            setDate()

        }

        image.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(
                    it,
                    REQUEST_CODE_IMAGE_PICK
                )
            }

        }

    }

    fun setDate() {
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "MM/dd/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                dob = sdf.format(cal.getTime())
                text_date.setText(dob)

            }

        activity?.let {
            DatePickerDialog(
                it, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {

                progressBar.visibility = View.VISIBLE
                imageUri = it
                uploadImage()
                progressBar.visibility = View.VISIBLE

//                try {
//                    data?.let {
//                        val inputStream: InputStream? =
//                            context?.contentResolver?.openInputStream(it.data!!)
//                        inputStream?.let { stream ->
//
//                            viewModel.uploadPhoto(stream)
//                        }
//                    }
//                } catch (e: FileNotFoundException) {
//                    e.printStackTrace()
//
//                }
            }
        }

    }

    fun successDialog() {
        val mDialogView = LayoutInflater.from(activity).inflate(R.layout.success_layout, null)
        //AlertDialogBuilder
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        var mAlertDialog = mBuilder?.show()!!
//        timer.schedule(object : TimerTask() {
//            override fun run() {
//            }
//        }, DELAY)
//        mAlertDialog?.dismiss()
//
//        timer.cancel()
//        timer.purge()

    }

    private fun uploadImage() {
        if (imageUri != null) {
            progressBar.visibility = View.VISIBLE
            val ref = imageRef.child("uploads/" + UUID.randomUUID().toString())
            val uploadTask = ref.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE

                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.INVISIBLE

                    imageUrl = task.result.toString()
                } else {
                    // Handle failures
                }
            }.addOnFailureListener {
                Toast.makeText(activity, "Sorry an Error Occured", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "Please Upload an Image", Toast.LENGTH_SHORT).show()

        }
    }


}