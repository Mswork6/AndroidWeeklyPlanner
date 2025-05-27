package com.example.androidweeklyplanner.domain

interface Converter<E, S> {
    fun convertToEntity(state: S): E

    fun convertToState(entity: E): S
}
