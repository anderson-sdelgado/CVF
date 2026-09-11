package br.com.usinasantafe.cvf.infra.repositories.variable

import br.com.usinasantafe.cvf.domain.entities.variable.Cart
import br.com.usinasantafe.cvf.domain.repositories.variable.NoteRepository
import br.com.usinasantafe.cvf.infra.datasource.retrofit.variable.NoteRetrofitDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.variable.CartRoomDatasource
import br.com.usinasantafe.cvf.infra.datasource.room.variable.HeaderRoomDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.HeaderSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.datasource.sharedpreferences.CartSharedPreferencesDatasource
import br.com.usinasantafe.cvf.infra.models.retrofit.variable.roomModelToRetrofitModel
import br.com.usinasantafe.cvf.infra.models.room.variable.sharedPreferencesModelToRoomModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.entityToSharedPreferencesModel
import br.com.usinasantafe.cvf.infra.models.sharedpreferences.sharedPreferencesModelToEntity
import br.com.usinasantafe.cvf.lib.StatusSend
import br.com.usinasantafe.cvf.utils.EmptyResult
import br.com.usinasantafe.cvf.utils.call
import br.com.usinasantafe.cvf.utils.dateOneMonthAgo
import br.com.usinasantafe.cvf.utils.getClassAndMethod
import br.com.usinasantafe.cvf.utils.required
import javax.inject.Inject

class INoteRepository @Inject constructor(
    private val headerSharedPreferencesDatasource: HeaderSharedPreferencesDatasource,
    private val cartSharedPreferencesDatasource: CartSharedPreferencesDatasource,
    private val headerRoomDatasource: HeaderRoomDatasource,
    private val cartRoomDatasource: CartRoomDatasource,
    private val noteRetrofitDatasource: NoteRetrofitDatasource
): NoteRepository {

    override suspend fun hasSend(): Result<Boolean> =
        call(getClassAndMethod()) {
            headerRoomDatasource.hasByStatusSend(StatusSend.SEND).getOrThrow()
        }

    override suspend fun send(token: String, idConfigServ: Int): EmptyResult =
        call(getClassAndMethod()) {
            val headerRoomModelList = headerRoomDatasource.listByStatusSend(StatusSend.SEND).getOrThrow()
            val headerRetrofitModelOutputList = headerRoomModelList.map {
                val cartRoomModelList = cartRoomDatasource.listByIdHeader(it.id!!).getOrThrow()
                val cartRetrofitModelList = cartRoomModelList.map { cartRoomModel ->
                    cartRoomModel.roomModelToRetrofitModel()
                }
                it.roomModelToRetrofitModel(idConfigServ, cartRetrofitModelList)
            }
            val headerRetrofitModelInputList = noteRetrofitDatasource.send(token, headerRetrofitModelOutputList).getOrThrow()
            headerRoomModelList.forEach { roomModel ->
                headerRetrofitModelInputList.find { it.id == roomModel.id }?.let {
                    roomModel.idServ = it.idServ
                }
            }
            headerRoomDatasource.updateStatusSend(StatusSend.SENT, headerRoomModelList).getOrThrow()
        }

    override suspend fun getRegDriver(): Result<Long?> =
        call(getClassAndMethod()) {
            headerSharedPreferencesDatasource.getRegDriver().getOrThrow()
        }

    override suspend fun setRegDriver(reg: Long): EmptyResult =
        call(getClassAndMethod()) {
            headerSharedPreferencesDatasource.setRegDriver(reg).getOrThrow()
        }

    override suspend fun deleteNote(): EmptyResult =
        call(getClassAndMethod()) {
            cartSharedPreferencesDatasource.clean().getOrThrow()
            headerSharedPreferencesDatasource.clean().getOrThrow()
        }

    override suspend fun finish(): EmptyResult =
        call(getClassAndMethod()) {
            val headerSharedPreferencesModel = headerSharedPreferencesDatasource.get().getOrThrow()
            val id = headerRoomDatasource.save(headerSharedPreferencesModel.sharedPreferencesModelToRoomModel()).getOrThrow()
            val cartSharedPreferencesModelList = cartSharedPreferencesDatasource.list().getOrThrow()
            cartSharedPreferencesModelList.forEach {
                cartRoomDatasource.save(it.sharedPreferencesModelToRoomModel(id)).getOrThrow()
            }
            headerSharedPreferencesDatasource.clean().getOrThrow()
            cartSharedPreferencesDatasource.clean().getOrThrow()
        }

    override suspend fun getIdTruck(): Result<Int?> =
        call(getClassAndMethod()) {
            headerSharedPreferencesDatasource.getIdTruck().getOrThrow()
        }

    override suspend fun cartList(): Result<List<Cart>> =
        call(getClassAndMethod()) {
            val list = cartSharedPreferencesDatasource.list().getOrThrow()
            list.map { it.sharedPreferencesModelToEntity() }
        }

    override suspend fun setCart(entity: Cart): EmptyResult =
        call(getClassAndMethod()) {
            cartSharedPreferencesDatasource.add(entity.entityToSharedPreferencesModel()).getOrThrow()
        }

    override suspend fun setIdTruck(id: Int): EmptyResult =
        call(getClassAndMethod()) {
            headerSharedPreferencesDatasource.setIdTruck(id).getOrThrow()
        }

    override suspend fun clean(): EmptyResult =
        call(getClassAndMethod()) {
            val list = headerRoomDatasource.listByStatusSend(StatusSend.SENT).getOrThrow()
            list.filter{ it.dateHour  < dateOneMonthAgo() }.forEach{
                val id = it::id.required()
                cartRoomDatasource.deleteByIdHeader(id).getOrThrow()
                headerRoomDatasource.delete(id).getOrThrow()
            }
            headerSharedPreferencesDatasource.clean().getOrThrow()
            cartSharedPreferencesDatasource.clean().getOrThrow()
        }

}