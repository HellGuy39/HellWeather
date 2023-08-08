package com.hellguy39.hellweather.core.network.dto

data class AlertDto(
    /* Alert headline */
    val headline: String,
    /* Type of alert */
    val msgType: String,
    /* Severity of alert */
    val severity: String,
    /* Urgency */
    val urgency: String,
    /* Areas covered */
    val areas: String,
    /* Category */
    val category: String,
    /* Certainty */
    val certainty: String,
    /* Event */
    val event: String,
    /* Note */
    val note: String,
    /* Effective TODO: handle date from api in a right way */
    val effective: String,
    /* Expires */
    val expires: String,
    /* Description */
    val desc: String,
    /* Instruction */
    val instruction: String,
)
