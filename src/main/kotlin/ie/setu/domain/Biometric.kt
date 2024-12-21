package ie.setu.domain

data class Biometric (
    val id: Int = 0,
    val userId:Int = 0,
    val bp_systolic:Double = 0.0,
    val bp_diastolic:Double = 0.0,
    val blood_sugar:Double = 0.0,
    val cholesterol:Double = 0.0,
)