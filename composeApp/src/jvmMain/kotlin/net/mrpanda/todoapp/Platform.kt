package net.mrpanda.todoapp

fun getHomeDir(): String = System.getenv("HOME") ?: "."