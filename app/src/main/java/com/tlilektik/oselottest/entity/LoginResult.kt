package com.tlilektik.oselottest.entity

import com.tlilektik.oselottest.entity.LoggedInUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
