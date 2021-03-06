package com.plugged.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plugged.Auth.RegistrationFragment
import com.plugged.api.ApiHelper
import com.plugged.models.*
import com.plugged.repository.PluggedRepository
import com.plugged.utils.NetWorkHelper
import com.plugged.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.InputStream


class PluggedViewModel @ViewModelInject constructor(
    private val repository: PluggedRepository,
    private val networkHelper: NetWorkHelper,
    apiHelper: ApiHelper
) : ViewModel() {

    val loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    var login_data:LoginResponse?=null

    var healthRecord:MutableLiveData<Resource<HealthRecordsResponse>> = MutableLiveData()
    var healthData:HealthRecordsResponse?=null

    var register_hospital__data:RegisterHospitalResponse?=null
    var login_hospital__data:LoginHospitalResponse?=null
    val registerPatientResponse: MutableLiveData<Resource<Reg_PatientResponse>> = MutableLiveData()
    val addRecord: MutableLiveData<Resource<AddRecordResponse>> = MutableLiveData()
    val searchRecord: MutableLiveData<Resource<SearchResponse>> = MutableLiveData()
    var searchData:SearchResponse?=null
    var registerPatient_data:Reg_PatientResponse?=null
    var upload_data:UploadImageResponse?=null
    var record_data:AddRecordResponse?=null
    val patientProfile:MutableLiveData<LoginResponse> = MutableLiveData()
    var patient_data: LiveData<LoginResponse> = MutableLiveData()
    val uploadPic:MutableLiveData<Resource<UploadImageResponse>> = MutableLiveData()//token:String,token:String,token:String,
    var registerHospital:MutableLiveData<Resource<RegisterHospitalResponse>> = MutableLiveData()
    var loginHospital:MutableLiveData<Resource<LoginHospitalResponse>> = MutableLiveData()


    private suspend fun register_hospital(register_hospital: RegisterHospital){
        registerHospital.postValue(Resource.Loading())
        try{
            if (networkHelper.isNetworkConnected())
            {
                val response = repository.register_hospital(register_hospital)
                if (response.isSuccessful)
                {
                    response.body()?.let {result->
                        register_hospital__data = result

                        registerHospital.postValue(Resource.Success(register_hospital__data ?:result))

                    }
                }
                else{
                    registerHospital.postValue( Resource.Error(response.message()))
                }
            }

            else{
                loginResponse.postValue( Resource.Error("No Internet Connection"))

            }
        }

        catch (t: Throwable) {
            when (t) {
                is IOException -> loginResponse.postValue(Resource.Error("Network Error"))
                else -> loginResponse.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun getRecords(){
        healthRecord.postValue(Resource.Loading())
        try{
            if (networkHelper.isNetworkConnected())
            {
                val response = repository.getRecords()
                if (response.isSuccessful)
                {
                    response.body()?.let {result->
                        healthData = result

                        healthRecord.postValue(Resource.Success(healthData ?:result))

                    }
                }
                else{
                    healthRecord.postValue( Resource.Error(response.message()))
                }
            }

            else{
                healthRecord.postValue( Resource.Error("No Internet Connection"))

            }
        }

        catch (t: Throwable) {
            when (t) {
                is IOException -> loginResponse.postValue(Resource.Error("Network Error"))
                else -> loginResponse.postValue(Resource.Error("Conversion Error"))
            }
        }
    }



    private suspend fun login_hospital(login_hospital: Login){
        loginHospital.postValue(Resource.Loading())
        try{
            if (networkHelper.isNetworkConnected())
            {
                val response = repository.login_hospital(login_hospital)
                if (response.isSuccessful)
                {
                    response.body()?.let {result->
                        login_hospital__data = result

                        loginHospital.postValue(Resource.Success(login_hospital__data ?:result))

                    }
                }
                else{
                    loginHospital.postValue( Resource.Error(response.message()))
                }
            }

            else{
                loginResponse.postValue( Resource.Error("No Internet Connection"))

            }
        }

        catch (t: Throwable) {
            when (t) {
                is IOException -> loginResponse.postValue(Resource.Error("Network Error"))
                else -> loginResponse.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    //Search Record takes  Patients Email as Body
    private suspend fun searchRecord(query:SearchBody)
    {

        searchRecord.postValue(Resource.Loading())
        try{
            if (networkHelper.isNetworkConnected())
            {
                val response = repository.searchRecord(query)
                if (response.isSuccessful)
                {
                    response.body()?.let {result->
                        searchData = result

                        searchRecord.postValue(Resource.Success(searchData ?:result))

                    }
                }
                else{
                    searchRecord.postValue( Resource.Error(response.message()))
                }
            }

            else{
                searchRecord.postValue( Resource.Error("No Internet Connection"))

            }
        }

        catch (t: Throwable) {
            when (t) {
                is IOException -> searchRecord.postValue(Resource.Error("Network Error"))
                else -> searchRecord.postValue(Resource.Error(t.message.toString()))
            }
        }




    }

    fun search(query: SearchBody)= viewModelScope.launch {
        searchRecord(query)
    }

    fun get_record() = viewModelScope.launch {
        getRecords()
    }



    fun LoginHospital(login_hospital: Login) = viewModelScope.launch {
        login_hospital(login_hospital)
    }
    fun RegisterHospital(register_hospital: RegisterHospital)=viewModelScope.launch {
        register_hospital(register_hospital)
    }

    //Login Patient
   private suspend fun login_patient(login: Login){
        loginResponse.postValue(Resource.Loading())
        try{
            if (networkHelper.isNetworkConnected())
            {
                val response = repository.login_patient(login)
                if (response.isSuccessful)
                {
                    response.body()?.let {result->
                        login_data = result

                        loginResponse.postValue(Resource.Success(login_data ?:result))

                    }
                }
                else{
                        loginResponse.postValue( Resource.Error(response.message()))
                    }
            }

            else{
                loginResponse.postValue( Resource.Error("No Internet Connection"))

            }
        }

        catch (t: Throwable) {
            when (t) {
                is IOException -> loginResponse.postValue(Resource.Error("Network Error"))
                else -> loginResponse.postValue(Resource.Error("Conversion Error"))
            }
        }
    }


    private suspend fun register_patient(patient: RegPatient){
        registerPatientResponse.postValue(Resource.Loading())
        try{
            if (networkHelper.isNetworkConnected())
            {
                val response = repository.register_patient(patient)
                if (response.isSuccessful)
                {
                    response.body()?.let {result->
                        registerPatient_data = result

                        registerPatientResponse.postValue(Resource.Success(registerPatient_data ?:result))

                    }
                }

                else{
                    registerPatientResponse.postValue( Resource.Error(response.message()))
                }
            }

            else{
                registerPatientResponse.postValue( Resource.Error("No Internet Connection"))

            }
        }

        catch (t: Throwable) {
            when (t) {
                is IOException -> registerPatientResponse.postValue(Resource.Error("Network Error"))
                else -> registerPatientResponse.postValue(Resource.Error("Unexpected Error"))
            }
        }
    }

    fun LoginPatient(login: Login)=viewModelScope.launch {

        login_patient(login)
    }

    fun addPatientRecord(record: AddRecord) = viewModelScope.launch {
        addRecord(record)
    }

    private suspend fun addRecord(record: AddRecord)
    {
        addRecord.postValue(Resource.Loading())
        try{
            if (networkHelper.isNetworkConnected())
            {
                val response = repository.addRecord(record)
                if (response.isSuccessful)
                {
                    response.body()?.let {result->
                        record_data = result

                        addRecord.postValue(Resource.Success(record_data ?:result))

                    }
                }

                else{
                    addRecord.postValue( Resource.Error(response.message()))
                }
            }

            else{
                addRecord.postValue( Resource.Error("No Internet Connection"))

            }
        }

        catch (t: Throwable) {
            when (t) {
                is IOException -> registerPatientResponse.postValue(Resource.Error("Network Error"))
                else -> registerPatientResponse.postValue(Resource.Error("Unexpected Error"))
            }
        }

    }

    fun RegisterPatient(patient: RegPatient) = viewModelScope.launch {
        register_patient(patient)

    }


//    upload Photo

    fun uploadPhoto(profile_pic: InputStream)
    {
        uploadPic.postValue(Resource.Loading())
        viewModelScope.launch {

            val image = MultipartBody.Part.createFormData(
                "image", "myPic.jpg", RequestBody.create(
                    "image/*".toMediaTypeOrNull(),
                    profile_pic.readBytes()
                )
            )
//            val image = MultipartBody.Part.createFormData(
//                "image", "myPic.jpg", profile_pic.readBytes()
//                    .toRequestBody(
//                        "image/*".toMediaTypeOrNull(),
//                        0
//                    )
//            )
            try {
                val response = repository.uplodImage(image)
                response.body()?.let {result->
                    upload_data = result
                    uploadPic.postValue(Resource.Success(upload_data?:result))

                }

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> uploadPic.postValue(Resource.Error(t.message.toString()))
                    else -> uploadPic.postValue(Resource.Error("Conversion Error"))
                }
            }
        }

    }


    fun savePatient(patient: LoginResponse) = viewModelScope.launch {
        repository.insert(patient)
    }

    fun deletePatient() = viewModelScope.launch {

        repository.deletePatient()
    }

    fun getPatient() = repository.getPatient()



    fun saveToken(token: Token) = viewModelScope.launch {
        repository.insertToken(token)
    }

    fun deleteToken() = viewModelScope.launch {

        repository.deleteToken()
    }

    fun getToken() = repository.getToken()







}