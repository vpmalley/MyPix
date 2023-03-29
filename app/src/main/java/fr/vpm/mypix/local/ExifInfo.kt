package fr.vpm.mypix.local

data class ExifInfo(
    val name: String,
    val extension: String,
    val make: String?,
    val model: String?,
    val focalLength: Double,
    val focalLength35: Int
) {
    fun toCsvString(): String {
        return listOf(
            name,
            extension,
            make ?: "",
            model ?: "",
            focalLength.toString(),
            focalLength35.toString()
        ).joinToString(",", postfix = "\n")
    }
}