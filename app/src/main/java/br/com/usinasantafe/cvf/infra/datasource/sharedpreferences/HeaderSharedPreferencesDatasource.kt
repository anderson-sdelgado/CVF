package br.com.usinasantafe.cvf.infra.datasource.sharedpreferences

import br.com.usinasantafe.cvf.infra.models.sharedpreferences.HeaderSharedPreferencesModel
import br.com.usinasantafe.cvf.utils.EmptyResult
import java.util.Date

interface HeaderSharedPreferencesDatasource {
    suspend fun getRegDriver(): Result<Long?>
    suspend fun setRegDriver(reg: Long): EmptyResult
    suspend fun clean(): EmptyResult
    suspend fun getIdTruck(): Result<Int?>
    suspend fun setTruck(id: Int, nro: Int): EmptyResult
    suspend fun get(): Result<HeaderSharedPreferencesModel>
    suspend fun has(): Result<Boolean>
    suspend fun save(model: HeaderSharedPreferencesModel): EmptyResult
    suspend fun setDate(dateHourExitMill: Date, dateHourArrivalField: Date): EmptyResult
}