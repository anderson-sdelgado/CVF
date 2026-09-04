package br.com.usinasantafe.cvf.di.usecase

import br.com.usinasantafe.cvf.domain.usecases.note.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NoteModule {

    @Binds
    @Singleton
    fun bindGetDriver(usecase: IGetRegDriver): GetRegDriver

    @Binds
    @Singleton
    fun bindSetDriver(usecase: ISetRegDriver): SetRegDriver

    @Binds
    @Singleton
    fun bindHasSendNote(usecase: IHasSendNote): HasSendNote

    @Binds
    @Singleton
    fun bindSendNote(usecase: ISendNote): SendNote

    @Binds
    @Singleton
    fun bindCheckRegDriver(usecase: ICheckRegDriver): CheckRegDriver

    @Binds
    @Singleton
    fun bindGetNroTruck(usecase: IGetNroTruck): GetNroTruck

    @Binds
    @Singleton
    fun bindDeleteNote(usecase: IDeleteNote): DeleteNote

    @Binds
    @Singleton
    fun bindCheckNroTruck(usecase: ICheckNroTruck): CheckNroTruck

    @Binds
    @Singleton
    fun bindSetNroTruck(usecase: ISetNroTruck): SetNroTruck

    @Binds
    @Singleton
    fun bindGetNroCart(usecase: IGetNroCart): GetNroCart

    @Binds
    @Singleton
    fun bindCheckNroCart(usecase: ICheckNroCart): CheckNroCart

    @Binds
    @Singleton
    fun bindSetNroCart(usecase: ISetNroCart): SetNroCart

    @Binds
    @Singleton
    fun bindPosCart(usecase: IPosCart): PosCart

    @Binds
    @Singleton
    fun bindGetTypeTruck(usecase: IGetTypeTruck): GetTypeTruck

    @Binds
    @Singleton
    fun bindGetDescReview(usecase: IGetDescReview): GetDescReview

    @Binds
    @Singleton
    fun bindFinishNote(usecase: IFinishNote): FinishNote

    @Binds
    @Singleton
    fun bindCheckRepeatedCart(usecase: ICheckRepeatedCart): CheckRepeatedCart

    @Binds
    @Singleton
    fun bindCheckInvertedCart(usecase: ICheckInvertedCart): CheckInvertedCart

}