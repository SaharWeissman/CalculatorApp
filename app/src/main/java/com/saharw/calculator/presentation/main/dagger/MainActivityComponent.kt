package com.saharw.calculator.presentation.main.dagger

import android.support.v7.app.AppCompatActivity
import com.saharw.calculator.presentation.base.IPresenter
import com.saharw.calculator.presentation.main.MainActivity
import com.saharw.calculator.presentation.main.mvp.MainModel
import com.saharw.calculator.presentation.main.mvp.MainPresenter
import com.saharw.calculator.presentation.main.mvp.MainView
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by saharw on 22/04/2018.
 */
@Singleton
@Component(modules = arrayOf(MainActivityComponent.MainActivityModule::class))
interface MainActivityComponent {

    fun inject(activity: MainActivity)

    @Module
    class MainActivityModule(private val activity: AppCompatActivity, private val layoutId: Int) {

        @Provides
        fun provideMainView() : MainView {
            return MainView()
        }

        @Provides
        fun provideMainModel() : MainModel {
            return MainModel(activity)
        }

        @Provides
        fun provideMainPresenter(view : MainView, model: MainModel) : IPresenter {
            return MainPresenter(activity, view, layoutId, model)
        }

    }
}