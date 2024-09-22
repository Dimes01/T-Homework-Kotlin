package org.example.models

class HTML : TagWithText("html") {
    fun body(init: Body.() -> Unit) = initTag(Body(), init)
}