package com.skateboard.blurimageview

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import java.io.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class BlurImageViewRender(private val context: Context, private var bitmap: Bitmap?) : GLSurfaceView.Renderer {

    private var srcWidth: Int = 0

    private var srcHeight: Int = 0


    private var created = false

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        val vertex = readSlgl("vertex.slgl")
        val fragment = readSlgl("fragment.slgl")
        createProgram(vertex, fragment)
        created = true
    }

    private fun readSlgl(fileName: String): String {
        val buffer = StringBuffer()
        try {
            val inReader = BufferedReader(InputStreamReader(context.assets.open(fileName)))
            var item = inReader.readLine()
            while (item != null) {
                buffer.append(item).append("\n")
                item = inReader.readLine()
            }
            inReader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return buffer.toString()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        onSizeChanged(width,height)
        prepare()
    }

    private fun prepare() {
        bitmap?.let {
            prepare(it)
        }
    }

    fun setImageBitmap(bitmap: Bitmap) {
        this.bitmap = bitmap
        if (created) {
            prepare()
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        draw()
    }

    companion object {
        init {
            System.loadLibrary("blurimageview")
        }
    }

    external fun createProgram(vertex: String, fragment: String)

    external fun onSizeChanged(scrWidth: Int, scrHeight: Int)

    external fun prepare(bitmap: Bitmap)

    external fun draw()
}