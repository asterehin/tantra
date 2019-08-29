package com.tantra.tantrayoga.model

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * out T - https://kotlinlang.ru/docs/reference/generics.html
 */
open class Event<out T>(private val content: T, private val action: Action = Action.NONE) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): ActionProgramm<T>? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            ActionProgramm(content, action)
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    data class ActionProgramm<out T>(val content: T, val action: Action)

    enum class Action {
        SELECT,
        DELETE,
        EDIT,
        NONE
    }
}