package cgy.memoriz.adapter

import cgy.memoriz.data.ClassData

interface ClassListAdapterInterface {

    fun onClick(classInfo : ClassData)

    fun onLongClick(classInfo : ClassData)
}