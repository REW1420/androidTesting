package com.example.firebaseapp.dataClass

class DataClass {
    var dataTitle: String? = null
        private set
    var dataDesc: String? = null
        private set
    var dataLang: String? = null
        private set
    var dataImage: String? = null
        private set
    var key: String? = null

    constructor(dataTitle: String?, dataDesc: String?, dataLang: String?, dataImage: String?) {
        this.dataTitle = dataTitle
        this.dataDesc = dataDesc
        this.dataLang = dataLang
        this.dataImage = dataImage
    }

    constructor() {}
}
