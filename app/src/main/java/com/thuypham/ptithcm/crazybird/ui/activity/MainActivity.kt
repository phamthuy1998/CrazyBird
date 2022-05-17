package com.thuypham.ptithcm.crazybird.ui.activity

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import com.thuypham.ptithcm.crazybird.R
import com.thuypham.ptithcm.crazybird.base.BaseActivity
import com.thuypham.ptithcm.crazybird.databinding.ActivityMainBinding
import com.thuypham.ptithcm.crazybird.extension.gone
import com.thuypham.ptithcm.crazybird.extension.hideSystemBars
import com.thuypham.ptithcm.crazybird.extension.setOnSingleClickListener
import com.thuypham.ptithcm.crazybird.extension.show

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private var touchSoundPool: SoundPool? = null
    private var touchSoundId: Int = 0
    private var diedMedia: MediaPlayer? = null
    private var scoreMedia: MediaPlayer? = null
    private var hitMedia: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemBars()
    }

    override fun setupLogic() {
        super.setupLogic()
        setupAudio()
    }

    override fun setupView() {
        updateScore(0)
        setupEvent()
    }

    private fun setupAudio() {
        touchSoundPool = SoundPool.Builder().setMaxStreams(1).build()
        touchSoundId = touchSoundPool!!.load(this, R.raw.audio_wing, 1)
        diedMedia = MediaPlayer.create(this, R.raw.audio_died)
        scoreMedia = MediaPlayer.create(this, R.raw.audio_point)
        hitMedia = MediaPlayer.create(this, R.raw.audio_hit)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupEvent() {
        binding.apply {
            gameView.setOnScoreUpdateListener { score ->
                scoreMedia?.start()
                updateScore(score)
            }

            gameView.setOnTouchListener { p0, p1 ->
                touchSoundPool?.play(touchSoundId, 1f, 1f, 1, 0, 1f)
                gameView.setOnTouchEvent(p1)
                true
            }
            gameView.setOnGameOverListener { score ->
                hitMedia?.start()
                diedMedia?.start()
                gameOver()
            }

            btnStart.setOnSingleClickListener {
                startGame()
            }

            layoutEndGame.root.setOnSingleClickListener {
                restartGame()
            }

        }
    }

    private fun gameOver() {
        binding.apply {
            btnStart.gone()
            layoutEndGame.root.show()
        }
    }

    private fun restartGame() {
        binding.apply {
            gameView.resetGame()
            prepareToStartGame()
        }
    }

    private fun startGame() {
        binding.apply {
            prepareToStartGame()
            gameView.startGame()
        }

    }

    private fun prepareToStartGame() {
        binding.apply {
            updateScore(0)
            tvScore.show()
            layoutEndGame.root.gone()
            btnStart.gone()
        }
    }

    private fun updateScore(score: Int) {
        binding.tvScore.text = getString(R.string.score, score)
    }

    override fun onPause() {
        super.onPause()
        pauseAudio()
    }

    private fun pauseAudio() {
        touchSoundPool?.pause(touchSoundId)
        scoreMedia?.pause()
        diedMedia?.pause()
        hitMedia?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseAudio()
    }

    private fun releaseAudio() {
        scoreMedia?.release()
        touchSoundPool?.release()
        diedMedia?.release()
        hitMedia?.release()
        scoreMedia = null
        touchSoundPool = null
        diedMedia = null
        hitMedia = null
    }
}