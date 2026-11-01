package com.v2ray.ang.ui.compose

import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.view.Surface
import android.view.TextureView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private class BackdropState {
    var player: MediaPlayer? = null
    var surface: Surface? = null
    var videoW: Int = 0
    var videoH: Int = 0
}

private fun applyCenterCrop(view: TextureView, state: BackdropState) {
    val vw = state.videoW
    val vh = state.videoH
    val viewW = view.width.toFloat()
    val viewH = view.height.toFloat()
    if (vw <= 0 || vh <= 0 || viewW <= 0f || viewH <= 0f) return
    val scale = maxOf(viewW / vw, viewH / vh)
    val matrix = Matrix()
    matrix.setScale(vw * scale / viewW, vh * scale / viewH, viewW / 2f, viewH / 2f)
    view.setTransform(matrix)
}

private fun releasePlayer(state: BackdropState) {
    state.player?.let {
        try {
            it.stop()
        } catch (e: Exception) {
        }
        try {
            it.release()
        } catch (e: Exception) {
        }
    }
    state.player = null
    state.surface?.release()
    state.surface = null
}

@Composable
fun VideoBackdrop(rawRes: Int) {
    key(rawRes) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val state = BackdropState()
                val textureView = TextureView(context)
                textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                    override fun onSurfaceTextureAvailable(
                        surfaceTexture: SurfaceTexture,
                        width: Int,
                        height: Int
                    ) {
                        releasePlayer(state)
                        val player = MediaPlayer()
                        try {
                            val afd = context.resources.openRawResourceFd(rawRes)
                            player.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                            afd.close()
                            val surface = Surface(surfaceTexture)
                            state.surface = surface
                            player.setSurface(surface)
                            player.isLooping = true
                            player.setVolume(0f, 0f)
                            player.setOnVideoSizeChangedListener { _, w, h ->
                                state.videoW = w
                                state.videoH = h
                                applyCenterCrop(textureView, state)
                            }
                            player.setOnPreparedListener { it.start() }
                            player.prepareAsync()
                            state.player = player
                        } catch (e: Exception) {
                            player.release()
                        }
                    }

                    override fun onSurfaceTextureSizeChanged(
                        surfaceTexture: SurfaceTexture,
                        width: Int,
                        height: Int
                    ) {
                        applyCenterCrop(textureView, state)
                    }

                    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                        releasePlayer(state)
                        return true
                    }

                    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
                }
                textureView
            }
        )
    }
}
