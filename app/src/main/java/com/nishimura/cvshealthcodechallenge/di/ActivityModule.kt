package com.nishimura.cvshealthcodechallenge.di

import android.app.Activity
import com.nishimura.cvshealthcodechallenge.ui.MainActivity
import com.nishimura.cvshealthcodechallenge.network.FlickrRepositoryImpl
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModel
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ActivityModule constructor(private val mainActivity: MainActivity) {

    @Provides
    @Singleton
    fun provideApplication(): Activity {
        return mainActivity
    }

    @Provides
    @Singleton
    fun provideFlickrImageViewModel(): FlickrImageViewModel {
        return FlickrImageViewModelFactory(
            mainActivity.application, FlickrRepositoryImpl()
        ).create(FlickrImageViewModel::class.java)
    }
}