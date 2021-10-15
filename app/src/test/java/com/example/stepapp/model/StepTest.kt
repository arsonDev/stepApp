package com.example.stepapp.model

import junit.framework.TestCase
import org.junit.Test

class StepTest : TestCase() {

    private lateinit var step : Step

    public override fun setUp() {
        super.setUp()

        step = Step()
    }

    @Test
    fun testSetCount() {
        step.count = 5000

        assertEquals(step.count,5000)
        assertEquals(step.progress,50)
    }

    @Test
    fun testSetCountMoreThan10000() {
        step.count = 15000

        assertEquals(step.count,15000)
        assertEquals(step.progress,50)
    }

    @Test
    fun testSetCountMoreThan20000() {
        step.count = 25000

        assertEquals(step.count,25000)
        assertEquals(step.progress,50)
    }


    @Test
    fun testSetCountEqual10000() {
        step.count = 10000

        assertEquals(step.count,10000)
        assertEquals(step.progress,100)
    }

    @Test
    fun testSetCountEqual10001() {
        step.count = 10001

        assertEquals(step.count,10001)
        assertEquals(step.progress,1)
    }

    @Test
    fun testSetCountEqual1() {
        step.count = 1

        assertEquals(step.count,1)
        assertEquals(step.progress,1)
    }

    @Test
    fun testSetCountEqual100() {
        step.count = 100

        assertEquals(step.count,100)
        assertEquals(step.progress,1)
    }


    @Test
    fun testSetCountEqual1000() {
        step.count = 1000

        assertEquals(step.count,1000)
        assertEquals(step.progress,10)
    }
}