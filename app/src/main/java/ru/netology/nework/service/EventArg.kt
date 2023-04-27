package ru.netology.nework.service

import android.os.Bundle
import ru.netology.nework.dto.Event
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object EventArg: ReadWriteProperty<Bundle, Event?> {

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Event? =
        thisRef.getParcelable(property.name)

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Event?) {
        thisRef.putParcelable(property.name, value)
    }
}