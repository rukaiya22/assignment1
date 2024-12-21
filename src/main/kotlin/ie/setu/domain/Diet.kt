package ie.setu.domain

data class Diet (
    val id: Int = 0,
    val userId:Int = 0,
    val carbohydrate:Double = 0.0,
    val protein:Double = 0.0,
    val fat:Double = 0.0,
)