package ru.netology.nework.service

import android.os.Bundle
import ru.netology.nework.dto.Job
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object JobArg: ReadWriteProperty<Bundle, Job?> {

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Job? =
        thisRef.getParcelable(property.name)

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Job?) {
        thisRef.putParcelable(property.name, value)
    }
}