package com.oases.computingpower.data.protocol

class FunctionItem {

    var name: String
    var isSelect = false
    var imageUrl = ""


    constructor(name: String, isSelect: Boolean, imageUrl: String) {
        this.name = name
        this.isSelect = isSelect
        this.imageUrl = imageUrl
    }
}
