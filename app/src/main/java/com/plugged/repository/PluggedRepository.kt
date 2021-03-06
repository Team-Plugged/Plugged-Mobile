package com.plugged.repository

import androidx.lifecycle.LiveData
import com.plugged.api.ApiHelper
import com.plugged.models.*
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class PluggedRepository@Inject constructor(private val apiHelper: ApiHelper)  {

    suspend fun register_patient(patient: RegPatient): Response<Reg_PatientResponse> = apiHelper.register_patient(patient)

    suspend fun  login_patient(login: Login): Response<LoginResponse> = apiHelper.login_Patient(login)

    suspend fun getRecords(): Response<HealthRecordsResponse> = apiHelper.getRecords()



    suspend fun register_hospital(register_hospital: RegisterHospital)=apiHelper.register_hospital(register_hospital)

    suspend fun login_hospital(login_hospital: Login)=apiHelper.login_hospital(login_hospital)

    suspend fun addRecord(addRecord: AddRecord)=apiHelper.addRecord(addRecord)


    suspend fun uplodImage(image: MultipartBody.Part) = apiHelper.uplodImage(image)

    suspend fun searchRecord(query: SearchBody)=apiHelper.searchRecord(query)


    //Token
    suspend fun insertToken(token: Token)=apiHelper.insertToken(token)
    fun getToken()=apiHelper.getToken()
    suspend fun deleteToken()=apiHelper.deleteToken()


    // Patient Data
    suspend fun insert(patient:LoginResponse)=apiHelper.insert(patient)
    fun getPatient()=apiHelper.getPatient()
    suspend fun deletePatient()=apiHelper.deletePatient()




}