package com.example.xgups_tandem.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ManagerUtils @Inject constructor(
    @ApplicationContext val context: Context
) {

    fun getString(@StringRes id: Int, vararg args: Any): String {
        return context.getString(id, *args)
    }

    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return context.resources.getStringArray(id)
    }

    @ColorInt
    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    fun getDrawable(
        @DrawableRes resId: Int,
    ): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }
}