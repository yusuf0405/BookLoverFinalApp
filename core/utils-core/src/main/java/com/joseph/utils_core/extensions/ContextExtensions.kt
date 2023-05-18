package com.joseph.utils_core.extensions

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.joseph.utils_core.R
import com.joseph.utils_core.image.BlurTransformation
import java.io.ByteArrayOutputStream
import java.io.File


private const val SETTINGS_NAME = "settings"

val Context.dataStore by preferencesDataStore(SETTINGS_NAME)

private fun shimmerDrawable(): ShimmerDrawable {
    val shimmer =
        Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
            .setBaseAlpha(0.7f) //the alpha of the underlying children
            .setHighlightAlpha(0.6f) // the shimmer alpha amount
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()

    // This is the placeholder for the imageView
    return ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
}

fun Context.convertDrawableToByteArray(drawable: Drawable): ByteArray {
    val bitmap = (drawable as BitmapDrawable).bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

@Suppress("DEPRECATION")
fun Context.isServiceRunning(serviceClassName: String): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

    return activityManager?.getRunningServices(Integer.MAX_VALUE)
        ?.any { it.service.className == serviceClassName } ?: false
}

fun Context.getAttrColor(attrId: Int): Int {
    val attrs = intArrayOf(attrId)
    val array = this.obtainStyledAttributes(attrs)
    return array.getColor(0, 0)
}


fun Context.dpToPx(valueInDp: Float): Float {
    val metrics = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics)
}

fun Context.showRoundedImage(
    roundedSize: Int = 8.toDp,
    imageUrl: String,
    imageView: ImageView,
    @DrawableRes placeHolder: Int = R.drawable.image_placeholder
) {
    val requestOptions = RequestOptions()
        .transforms(CenterCrop(), RoundedCorners(roundedSize))
        .timeout(3000)
        .placeholder(shimmerDrawable())
    Glide.with(this)
        .load(imageUrl)
        .apply(requestOptions)
        .into(imageView)
}

fun Context.showRoundedImage(
    roundedSize: Int = 8.toDp,
    @DrawableRes imageId: Int,
    imageView: ImageView,
    @DrawableRes placeHolder: Int = R.drawable.image_placeholder
) {
    val requestOptions = RequestOptions().transforms(CenterCrop(), RoundedCorners(roundedSize))
    Glide.with(this)
        .load(imageId)
        .apply(requestOptions)
        .placeholder(shimmerDrawable())
        .into(imageView)
}


fun Context.showRoundedImage(
    roundedSize: Int,
    imageBitmap: Bitmap,
    imageView: ImageView,
    @DrawableRes placeHolder: Int = R.drawable.image_placeholder
) {
    val requestOptions = RequestOptions().transforms(CenterCrop(), RoundedCorners(roundedSize))
    Glide.with(this)
        .load(imageBitmap)
        .apply(requestOptions)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

fun Context.showRoundedImage(
    roundedSize: Int,
    file: File,
    imageView: ImageView,
) {
    val requestOptions = RequestOptions().transforms(CenterCrop(), RoundedCorners(roundedSize))
    Glide.with(this)
        .load(file)
        .apply(requestOptions)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

fun Context.showRoundedImage(
    roundedSize: Int,
    imageUri: Uri,
    imageView: ImageView,
    @DrawableRes placeHolder: Int = R.drawable.image_placeholder
) {

    val requestOptions = RequestOptions().transforms(CenterCrop(), RoundedCorners(roundedSize))
    Glide.with(this)
        .load(imageUri)
        .apply(requestOptions)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

fun Context.showBlurImage(
    blurSize: Float,
    imageUrl: String,
    imageView: ImageView
) {
    val customTarget = object : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            imageView.setImageBitmap(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) = Unit
    }
    Glide.with(this)
        .asBitmap()
        .placeholder(shimmerDrawable())
        .load(imageUrl)
        .dontAnimate()
        .transform(BlurTransformation(blurSize))
        .into(customTarget)

}

fun Context.showBlurImage(
    blurSize: Float,
    @DrawableRes imageId: Int,
    imageView: ImageView
) {
    Glide.with(this)
        .load(imageId)
        .placeholder(shimmerDrawable())
        .transform(BlurTransformation(blurSize))
        .into(imageView)
}

fun Context.showImage(@DrawableRes imageId: Int, imageView: ImageView) {
    Glide.with(this).setDefaultRequestOptions(RequestOptions())
        .load(imageId)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

fun Context.showImage(bitmap: Bitmap, imageView: ImageView) {
    Glide.with(this).setDefaultRequestOptions(RequestOptions())
        .load(bitmap)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

fun Context.showImage(uri: String?, imageView: ImageView) {
    Glide.with(this).setDefaultRequestOptions(RequestOptions())
        .load(uri)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

fun Context.showImage(file: File?, imageView: ImageView) {
    Glide.with(this).setDefaultRequestOptions(RequestOptions())
        .load(file)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

fun Context.showImage(byteArray: ByteArray?, imageView: ImageView) {
    Glide.with(this).setDefaultRequestOptions(RequestOptions())
        .load(byteArray)
        .placeholder(shimmerDrawable())
        .into(imageView)
}

