package cgy.memoriz.adapter

import cgy.memoriz.data.ReportData

interface ReportAdapterInterface {

    fun onClick(report : ReportData)

    fun onLongClick(report : ReportData)
}