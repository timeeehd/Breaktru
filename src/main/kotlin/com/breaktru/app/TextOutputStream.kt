package com.breaktru.app

import java.awt.BorderLayout
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.*

//For this part I got help from the internet: https://stackoverflow.com/questions/23909469/spring-boot-jar-double-click

// class to get a console window open when starting the application
class TextAreaOutputStreamTest : JPanel() {
    private val textArea = JTextArea(15, 30)
    private val taOutputStream = TextAreaOutputStream(
            textArea, "> ")

    companion object {
        private fun createAndShowGui() {
            val frame = JFrame("Output")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.contentPane.add(TextAreaOutputStreamTest())
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }

        fun mainRunner(args: Array<String>) {
            SwingUtilities.invokeLater { createAndShowGui() }
        }
    }

    init {
        layout = BorderLayout()
        add(JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER))
        System.setOut(PrintStream(taOutputStream))
    }
}

internal class TextAreaOutputStream(private val textArea: JTextArea, private val title: String) : OutputStream() {
    private val sb = StringBuilder()
    override fun flush() {}
    override fun close() {}

    @Throws(IOException::class)
    override fun write(b: Int) {
        if (b == '\r'.toInt()) return
        if (b == '\n'.toInt()) {
            val text = """
                $sb
                
                """.trimIndent()
            SwingUtilities.invokeLater { textArea.append(text) }
            sb.setLength(0)
            sb.append(title)
            return
        }
        sb.append(b.toChar())
    }

    init {
        sb.append(title)
    }
}