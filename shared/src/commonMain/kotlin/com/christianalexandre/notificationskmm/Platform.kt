package com.christianalexandre.notificationskmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform