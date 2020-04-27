package com.kraftwerk28.pocketcms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.kraftwerk28.pocketcms.databinding.ActivityCookieBinding

class CookieActivity : AppCompatActivity() {
    private val TAG = CookieActivity::class.java.name
    private lateinit var binding: ActivityCookieBinding
    private val gameState = GameState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            gameState.clicked = it.getInt("clicked", 0)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cookie)
        binding.gameState = gameState
        binding.shreckly.setOnClickListener {
            gameState.clicked++
            binding.invalidateAll()
        }
        binding.restartButton.setOnClickListener {
            gameState.clicked = 0
            binding.invalidateAll()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("clicked", gameState.clicked)
        Log.d(TAG, gameState.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu1, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.menu_share -> {
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "The score is ${gameState.clicked} points!"
                )
                sendIntent.type = "text/plain"
                val shareIntent = Intent.createChooser(sendIntent, "Chooser title")
                startActivity(shareIntent)
            }
        }
        return true
    }

}

enum class Rank {
    NEWBIE,
    MEDIUM,
    PRO,
}

class GameState {
    var rank = Rank.NEWBIE
    var clicked = 0
        set(value) {
            rank = when (field) {
                0 -> Rank.NEWBIE
                20 -> Rank.MEDIUM
                40 -> Rank.PRO
                else -> rank
            }
            field = value
        }

    fun gameScoreText() = "Clicked ${clicked} times."
}
