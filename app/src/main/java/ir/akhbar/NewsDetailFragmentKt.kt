package ir.akhbar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_news_detail.*

class NewsDetailFragmentKt : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsTitle.text = arguments?.getString("newsTitle") ?: "Test 123"
        newsDescription.text = arguments?.getString("newsDescription") ?: "Test123"
        Glide.with(context)
                .load(arguments?.getString("newsImage"))
                .into(newsHeaderImage)
        shareAction.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, arguments?.getString("newsUrl"))
            }.also {
                startActivity(Intent.createChooser(it, "Share with:"))
            }
        }
    }

}