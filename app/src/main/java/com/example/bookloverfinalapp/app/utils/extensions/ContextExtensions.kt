package com.example.bookloverfinalapp.app.utils.extensions

import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.datastore.preferences.preferencesDataStore
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.BlurTransformation
import com.example.bookloverfinalapp.app.utils.cons.SETTINGS_NAME
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.joseph.ui_core.extensions.toDp

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
    Glide.with(this)
        .load(imageUrl)
        .transform(BlurTransformation(blurSize))
        .into(imageView)
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

val Context.dataStore by preferencesDataStore(SETTINGS_NAME)
