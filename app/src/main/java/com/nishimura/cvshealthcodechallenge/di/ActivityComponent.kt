package com.nishimura.cvshealthcodechallenge.di

import com.nishimura.cvshealthcodechallenge.ui.MainActivity
import dagger.Component
import javax.inject.Singleton


@Component(modules = [ActivityModule::class])
@Singleton
interface ActivityComponent {

    fun inject(application: MainActivity)

}