package com.tlilektik.oselottest.service

import com.google.gson.annotations.SerializedName
import com.tlilektik.oselottest.entity.ItemObject

data class Response (@SerializedName("Brastlewark") var array: ArrayList<ItemObject>)