package cgy.memoriz.adapter

import cgy.memoriz.data.SlideData

interface SlideAdapterInterface {

    fun onClick(slide : SlideData)

    fun onLongClick(slide : SlideData)
}