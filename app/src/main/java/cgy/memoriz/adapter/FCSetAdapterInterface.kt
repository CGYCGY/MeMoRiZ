package cgy.memoriz.adapter

import cgy.memoriz.data.FCSetData

interface FCSetAdapterInterface {

    fun onClick(fcSetInfo : FCSetData)

    fun onLongClick(fcSetInfo : FCSetData)
}