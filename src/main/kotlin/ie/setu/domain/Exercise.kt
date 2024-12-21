package ie.setu.domain

data class Exercise (
    val id: Int = 0,
    val userId:Int = 0,
    val running:Double = 0.0,
    val swimming:Double = 0.0,
    val cycling:Double = 0.0,
    val equipment_based:Double = 0.0,
)