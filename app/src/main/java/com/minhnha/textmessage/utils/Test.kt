package com.minhnha.textmessage.utils

import com.minhnha.textmessage.utils.Test.A


class Test {
    sealed class A {

        class B : A()

        class C : A()
        {
            class E : A() //this works.
        }

        init {
            println("Sealed class A")
        }

    }
}