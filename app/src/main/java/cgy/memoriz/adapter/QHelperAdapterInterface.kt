package cgy.memoriz.adapter

import cgy.memoriz.data.QHelperData

interface QHelperAdapterInterface {

    fun onClick(question : QHelperData)

    fun onLongClick(question : QHelperData)
}