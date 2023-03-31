package com.joseph.utils_core.image

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import java.security.MessageDigest
import kotlin.math.roundToInt

private const val BITMAP_SCALE = 0.15f
private const val FORMAT_RGBA_F16 = "RGBA_F16"

class BlurTransformation(private val blurRadius: Float) : Transformation<Bitmap> {

    override fun transform(
        context: Context,
        resource: Resource<Bitmap>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap> {
        val glideBitmapPool = Glide.get(context).bitmapPool
        val blurBitmap = blur(resource.get(), blurRadius, context)
        return BitmapResource.obtain(blurBitmap, glideBitmapPool) as Resource<Bitmap>
    }

    companion object {
        private val NULL_BLUR_BITMAP = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)

        @JvmStatic
        @JvmOverloads
        fun blur(
            toTransform: Bitmap?,
            blurRadiusParam: Float,
            context: Context,
            scale: Float = BITMAP_SCALE
        ): Bitmap =
            if (toTransform != null) {
                transform(toTransform, blurRadiusParam, scale, context)
            } else {
                NULL_BLUR_BITMAP
            }

        private fun transform(
            toTransform: Bitmap,
            blurRadiusParam: Float,
            scale: Float,
            context: Context
        ): Bitmap {
            // С сервера может прийти RGBA_F16 который имеет кодировку которая не читается Element
            if (toTransform.config.name == FORMAT_RGBA_F16) {
                toTransform.config = Bitmap.Config.ARGB_8888
            }

            var blurRadius: Float = blurRadiusParam
            val outputBitmap: Bitmap
            when {
                blurRadius == 0f -> {
                    return toTransform
                }
                blurRadius < 1 -> {
                    blurRadius = 1f
                }
                blurRadius > 25 -> {
                    blurRadius = 25f
                }
            }
            val width = (toTransform.width * scale).roundToInt()
            val height = (toTransform.height * scale).roundToInt()
            val inputBitmap = Bitmap.createScaledBitmap(toTransform, width, height, false)
            outputBitmap = Bitmap.createBitmap(inputBitmap)
            val rs = RenderScript.create(context)
            val theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
            theIntrinsic.setRadius(blurRadius)
            theIntrinsic.setInput(tmpIn)
            theIntrinsic.forEach(tmpOut)
            tmpOut.copyTo(outputBitmap)
            theIntrinsic.destroy()
            tmpIn.destroy()
            tmpOut.destroy()
            rs.destroy()
            return outputBitmap
        }

    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}