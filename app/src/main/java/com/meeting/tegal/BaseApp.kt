package com.meeting.tegal

import android.app.Application
import com.meeting.tegal.repository.*
import com.meeting.tegal.ui.available_room.AvailableRoomViewModel
import com.meeting.tegal.ui.company.CompanyViewModel
import com.meeting.tegal.ui.detail_harga.DetailHargaViewModel
import com.meeting.tegal.ui.food_order.SelectFoodViewModel
import com.meeting.tegal.ui.login.LoginViewModel
import com.meeting.tegal.ui.main.home.HomeViewModel
import com.meeting.tegal.ui.main.shopping.ShoppingViewModel
import com.meeting.tegal.ui.maps.MapsViewModel
import com.meeting.tegal.ui.meeting.MeetingViewModel
import com.meeting.tegal.ui.order.OrderActivityViewModel
import com.meeting.tegal.ui.register.RegisterViewModel
import com.meeting.tegal.ui.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BaseApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApp)
            modules(listOf(retrofitModules, viewModelModules, repositoryModules))
        }
    }
}

val retrofitModules = module {
    single { ApiClient.instance() }
}

val repositoryModules = module {
    factory { UserRepository(get()) }
    factory { FoodRepository(get()) }
    factory { MeetingRepository(get()) }
    factory { OrderRepository(get()) }
    factory { PartnerRepository(get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { MeetingViewModel() }
    viewModel { MapsViewModel(get()) }
    viewModel { AvailableRoomViewModel(get()) }
    viewModel { OrderActivityViewModel(get(), get()) }
    viewModel { SelectFoodViewModel(get()) }
    viewModel { DetailHargaViewModel(get()) }
    viewModel { ShoppingViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CompanyViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}