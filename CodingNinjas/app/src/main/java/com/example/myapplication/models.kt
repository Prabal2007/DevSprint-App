package com.example.myapplication

data class InsultResponse(
    val insult: String
)

data class AdviceResponse(
    val slip: AdviceSlip
)

data class AdviceSlip(
    val advice: String
)

data class KanyeResponse(
    val quote: String
)

data class YesNoResponse(
    val answer: String,
    val image: String
)