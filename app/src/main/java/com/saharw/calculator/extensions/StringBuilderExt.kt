package com.saharw.calculator.extensions

fun StringBuilder.clear() {
    this.delete(0, this.length)
}