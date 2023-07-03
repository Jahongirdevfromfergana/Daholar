package uz.fergana.daholar.models

import java.io.Serializable

class Adib : Serializable {
    var uid: String? = null
    var name: String? = null
    var birthYear: String? = null
    var diedYear: String? = null
    var type: String? = null
    var about: String? = null
    var isSave: Boolean? = null
    var photoUrl: String? = null

    constructor()
    constructor(
        uid: String?,
        name: String?,
        birthYear: String?,
        diedYear: String?,
        type: String?,
        about: String?,
        isSave: Boolean?,
        photoUrl: String?
    ) {
        this.uid = uid
        this.name = name
        this.birthYear = birthYear
        this.diedYear = diedYear
        this.type = type
        this.about = about
        this.isSave = isSave
        this.photoUrl = photoUrl
    }

    override fun toString(): String {
        return "Adib(uid=$uid, name=$name, birthYear=$birthYear, diedYear=$diedYear, type=$type, about=$about, isSave=$isSave, photoUrl=$photoUrl)"
    }
}
