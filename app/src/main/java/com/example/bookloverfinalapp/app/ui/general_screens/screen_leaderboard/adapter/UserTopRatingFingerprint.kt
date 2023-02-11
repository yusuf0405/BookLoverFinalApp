package com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.BaseViewHolder
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.ItemFingerprint
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models.RatingTopUsers
import com.example.bookloverfinalapp.databinding.ItemTopRatingUsersBinding

class UserTopRatingFingerprint : ItemFingerprint<ItemTopRatingUsersBinding, RatingTopUsers> {

    override fun isRelativeItem(item: Item) = item is RatingTopUsers

    override fun getLayoutId() = R.layout.item_top_rating_users

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<ItemTopRatingUsersBinding, RatingTopUsers> {
        val binding = ItemTopRatingUsersBinding.inflate(layoutInflater, parent, false)
        return UserTopRatingViewHolder(binding)
    }

    override fun getDiffUtil() = diffUtil

    private val diffUtil = object : DiffUtil.ItemCallback<RatingTopUsers>() {

        override fun areItemsTheSame(
            oldItem: RatingTopUsers,
            newItem: RatingTopUsers
        ) = oldItem.id() == newItem.id()

        override fun areContentsTheSame(
            oldItem: RatingTopUsers,
            newItem: RatingTopUsers
        ) = oldItem == newItem
    }

}

class UserTopRatingViewHolder(
    binding: ItemTopRatingUsersBinding,
) : BaseViewHolder<ItemTopRatingUsersBinding, RatingTopUsers>(binding) {

    override fun onBind(item: RatingTopUsers) {
        super.onBind(item)
        setupViews()
    }

    private fun setupViews() = with(binding) {
        firstUserName.text = "${item.firstUserName}\n${item.firstUserLastName}"
        firstUserProgress.text = item.firstUserProgress.toString()
        secondUserName.text = "${item.secondUserName}\n${item.secondUserLastName}"
        secondUserProgress.text = item.secondUserProgress.toString()
        thirdUserName.text = "${item.thirdUserName}\n${item.thirdUserLastName}"
        thirdUserProgress.text = item.thirdUserProgress.toString()
        setupImage(url = item.firstUserImageUrl, imageView = theFirstUserPoster)
        setupImage(url = item.secondUserImageUrl, imageView = theSecondUserPoster)
        setupImage(url = item.thirdUserImageUrl, imageView = theThirdUserPoster)
    }

    private fun setupImage(url: String, imageView: ImageView) =
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
}
